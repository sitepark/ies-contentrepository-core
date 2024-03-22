package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.BulkOperationKey;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBulkExecution;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBulkOperation;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityTree;
import com.sitepark.ies.contentrepository.core.domain.entity.query.Query;
import com.sitepark.ies.contentrepository.core.domain.entity.query.SubTreeQuery;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.GroupNotEmptyException;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityBulkExecutor;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.ExtensionsNotifier;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.MediaReferenceManager;
import com.sitepark.ies.contentrepository.core.port.Publisher;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BulkPurge {

  private final ContentRepository repository;

  private final EntityLockManager lockManager;

  private final VersioningManager versioningManager;

  private final HistoryManager historyManager;

  private final AccessControl accessControl;

  private final RecycleBin recycleBin;

  private final SearchIndex searchIndex;

  private final MediaReferenceManager mediaReferenceManager;

  private final Publisher publisher;

  private final ExtensionsNotifier extensionsNotifier;

  private final EntityBulkExecutor entityBulkExecutor;

  private static Logger LOGGER = LogManager.getLogger();

  @Inject
  @SuppressWarnings("PMD.ExcessiveParameterList")
  protected BulkPurge(
      ContentRepository repository,
      EntityLockManager lockManager,
      VersioningManager versioningManager,
      HistoryManager historyManager,
      AccessControl accessControl,
      RecycleBin recycleBin,
      SearchIndex searchIndex,
      MediaReferenceManager mediaReferenceManager,
      Publisher publisher,
      ExtensionsNotifier extensionsNotifier,
      EntityBulkExecutor entityBulkExecutor) {

    this.repository = repository;
    this.lockManager = lockManager;
    this.historyManager = historyManager;
    this.versioningManager = versioningManager;
    this.accessControl = accessControl;
    this.recycleBin = recycleBin;
    this.searchIndex = searchIndex;
    this.mediaReferenceManager = mediaReferenceManager;
    this.publisher = publisher;
    this.extensionsNotifier = extensionsNotifier;
    this.entityBulkExecutor = entityBulkExecutor;
  }

  /**
   * Create a BulkExecution and pass it to the EntityBulkExecutor to execute the purge.
   * The return is a BulkExecution ID that can be used to track the progress.
   *
   * @param input Input argument for the bulk operations
   * @return BulkExecution ID that can be used to track the progress
   */
  public String bulkPurge(BulkPurgeInput input) {

    List<Entity> entityList = this.getEntityList(input);

    this.accessControl(entityList);

    EntityBulkOperation lock = this.buildLockOperation(entityList, false);
    EntityBulkOperation depublish = this.buildDepublishOperation(entityList);
    EntityBulkOperation purge = this.buildPurgeOperation(entityList);

    EntityBulkOperation unlock = this.buildUnlockOperation(entityList);

    EntityBulkExecution execution =
        EntityBulkExecution.builder()
            .topic("contentrepository", "purge")
            .operation(lock, depublish, purge)
            .finalizer(unlock)
            .build();

    return this.entityBulkExecutor.execute(execution);
  }

  private List<Entity> getEntityList(BulkPurgeInput input) {
    Query query = this.buildQuery(input);
    return this.repository.getAll(query);
  }

  private Query buildQuery(BulkPurgeInput input) {
    if (!input.getRootList().isEmpty()) {
      return SubTreeQuery.builder()
          .rootList(input.getRootList())
          .filterBy(input.getFilter().orElse(null))
          .build();
    }

    return Query.builder().filterBy(input.getFilter().get()).build();
  }

  private void accessControl(List<Entity> entityList) {

    entityList.stream()
        .forEach(
            entity -> {
              String id = entity.getId().get();
              if (entity.isGroup()) {
                if (!this.accessControl.isGroupRemoveable(id)) {
                  throw new AccessDeniedException("Not allowed to remove group " + id);
                }
              } else {
                if (!this.accessControl.isEntityRemovable(id)) {
                  throw new AccessDeniedException("Not allowed to remove entity " + id);
                }
              }
            });
  }

  private EntityBulkOperation buildLockOperation(List<Entity> entityList, boolean forceLock) {

    return EntityBulkOperation.builder()
        .key(BulkOperationKey.PURGE_LOCK)
        .entityList(entityList)
        .consumer(
            entity -> {
              String id = entity.getId().get();

              if (forceLock) {
                this.lockManager.forceLock(id);
              } else {
                this.lockManager.lock(id);
              }
            })
        .build();
  }

  private EntityBulkOperation buildDepublishOperation(List<Entity> entityList) {

    return EntityBulkOperation.builder()
        .key(BulkOperationKey.PURGE_DEPUBLISH)
        .entityList(entityList)
        .consumer(
            entity -> {
              String id = entity.getId().get();
              this.publisher.depublish(id);
            })
        .build();
  }

  private EntityBulkOperation buildPurgeOperation(List<Entity> entityList) {

    List<Entity> nonGroupList =
        entityList.stream().filter(entity -> !entity.isGroup()).collect(Collectors.toList());

    List<Entity> groupList =
        entityList.stream().filter(entity -> entity.isGroup()).collect(Collectors.toList());

    /*
     * Arrange the entityList so that first all group entries are deleted
     * and then the groups in the hierarchy from bottom to top.
     * This ensures that only empty pools are deleted.
     */
    List<Entity> orderedGroupList = this.orderGroupListHierarchicallyFromBottomToTop(groupList);
    List<Entity> orderedList = new ArrayList<>();
    orderedList.addAll(nonGroupList);
    orderedList.addAll(orderedGroupList);

    if (LOGGER.isDebugEnabled()) {
      orderedList.stream()
          .forEach(
              entity -> {
                LOGGER.debug("purge order: {}", entity);
              });
    }

    return EntityBulkOperation.builder()
        .key(BulkOperationKey.PURGE_PURGE)
        .entityList(orderedList)
        .consumer(
            entity -> {
              String id = entity.getId().get();

              if (this.repository.isGroup(id) && !this.repository.isEmptyGroup(id)) {
                throw new GroupNotEmptyException(id);
              }

              if (LOGGER.isInfoEnabled()) {
                LOGGER.info("purge: {}", entity);
              }

              this.searchIndex.remove(id);
              this.mediaReferenceManager.removeByReference(id);
              this.repository.removeEntity(id);
              this.historyManager.purge(id);
              this.versioningManager.removeAllVersions(id);
              this.recycleBin.removeByObject(id);
              this.extensionsNotifier.notifyPurge(id);
            })
        .build();
  }

  /**
   * Arranges the groups according to their hierarchy from bottom to top.
   */
  private List<Entity> orderGroupListHierarchicallyFromBottomToTop(List<Entity> groupList) {

    EntityTree tree = new EntityTree();
    groupList.stream().forEach(tree::add);

    List<Entity> hierarchicalOrder = tree.getAll();

    Collections.reverse(hierarchicalOrder);

    return hierarchicalOrder;
  }

  private EntityBulkOperation buildUnlockOperation(List<Entity> entityList) {

    return EntityBulkOperation.builder()
        .key(BulkOperationKey.PURGE_CLEANUP)
        .entityList(entityList)
        .consumer(
            entity -> {
              String id = entity.getId().get();
              try {
                this.lockManager.unlock(id);
              } catch (Exception e) {
                if (LOGGER.isTraceEnabled()) {
                  LOGGER.atTrace().withThrowable(e).log("Unable to unlock {}", entity);
                }
              }
            })
        .build();
  }
}
