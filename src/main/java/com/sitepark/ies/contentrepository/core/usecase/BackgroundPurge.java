package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.*;
import com.sitepark.ies.contentrepository.core.domain.entity.query.Query;
import com.sitepark.ies.contentrepository.core.domain.entity.query.SubTreeQuery;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.GroupNotEmptyException;
import com.sitepark.ies.contentrepository.core.port.*;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackgroundPurge {

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

  private final EntityBackgroundExecutor entityBackgroundExecutor;

  private static final Logger LOGGER = LogManager.getLogger();

  @Inject
  @SuppressWarnings("PMD.ExcessiveParameterList")
  protected BackgroundPurge(
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
      EntityBackgroundExecutor entityBackgroundExecutor) {

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
    this.entityBackgroundExecutor = entityBackgroundExecutor;
  }

  /**
   * Create a BackgroundExecution and pass it to the EntityBackgroundExecutor to execute the purge.
   * The return is a BackgroundExecution ID that can be used to track the progress.
   *
   * @param input Input argument for the background operations
   * @return BackgroundExecution ID that can be used to track the progress
   */
  public String backgroundPurge(BackgroundPurgeInput input) {

    List<Entity> entityList =
        this.getEntityList(input).stream().filter(entity -> entity.getId().isPresent()).toList();

    this.checkAccessControl(entityList);

    EntityBackgroundOperation lock = this.buildLockOperation(entityList, false);
    EntityBackgroundOperation depublish = this.buildDepublishOperation(entityList);
    EntityBackgroundOperation purge = this.buildPurgeOperation(entityList);

    EntityBackgroundOperation unlock = this.buildUnlockOperation(entityList);

    EntityBackgroundExecution execution =
        EntityBackgroundExecution.builder()
            .topic("contentrepository", "purge")
            .operation(lock, depublish, purge)
            .finalizer(unlock)
            .build();

    return this.entityBackgroundExecutor.execute(execution);
  }

  private List<Entity> getEntityList(BackgroundPurgeInput input) {
    Query query = this.buildQuery(input);
    return this.repository.getAll(query);
  }

  private Query buildQuery(BackgroundPurgeInput input) {
    if (!input.getRootList().isEmpty()) {
      return SubTreeQuery.builder()
          .rootList(input.getRootList())
          .filterBy(input.getFilter().orElse(null))
          .build();
    }

    return Query.builder().filterBy(input.getFilter().orElse(null)).build();
  }

  private void checkAccessControl(List<Entity> entityList) {

    entityList.forEach(
        entity -> {
          assert entity.getId().isPresent();
          String id = entity.getId().get();
          if (entity.isGroup()) {
            if (!this.accessControl.isGroupRemovable(id)) {
              throw new AccessDeniedException("Not allowed to remove group " + id);
            }
          } else {
            if (!this.accessControl.isEntityRemovable(id)) {
              throw new AccessDeniedException("Not allowed to remove entity " + id);
            }
          }
        });
  }

  private EntityBackgroundOperation buildLockOperation(List<Entity> entityList, boolean forceLock) {

    return EntityBackgroundOperation.builder()
        .key(BackgroundOperationKey.PURGE_LOCK)
        .entityList(entityList)
        .consumer(
            entity -> {
              assert entity.getId().isPresent();
              String id = entity.getId().get();

              if (forceLock) {
                this.lockManager.forceLock(id);
              } else {
                this.lockManager.lock(id);
              }
            })
        .build();
  }

  private EntityBackgroundOperation buildDepublishOperation(List<Entity> entityList) {

    return EntityBackgroundOperation.builder()
        .key(BackgroundOperationKey.PURGE_DEPUBLISH)
        .entityList(entityList)
        .consumer(
            entity -> {
              assert entity.getId().isPresent();

              String id = entity.getId().get();
              this.publisher.depublish(id);
            })
        .build();
  }

  private EntityBackgroundOperation buildPurgeOperation(List<Entity> entityList) {

    List<Entity> nonGroupList = entityList.stream().filter(entity -> !entity.isGroup()).toList();

    List<Entity> groupList =
        entityList.stream().filter(Entity::isGroup).collect(Collectors.toList());

    /*
     * Arrange the entityList so that first all group entries are deleted and then the groups in the hierarchy from
     * bottom to top. This ensures that only empty pools are deleted.
     */
    List<Entity> orderedGroupList = this.orderGroupListHierarchicallyFromBottomToTop(groupList);
    List<Entity> orderedList = new ArrayList<>();
    orderedList.addAll(nonGroupList);
    orderedList.addAll(orderedGroupList);

    if (LOGGER.isDebugEnabled()) {
      orderedList.forEach(entity -> LOGGER.debug("purge order: {}", entity));
    }

    return EntityBackgroundOperation.builder()
        .key(BackgroundOperationKey.PURGE_PURGE)
        .entityList(orderedList)
        .consumer(
            entity -> {
              assert entity.getId().isPresent();
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

  /** Arranges the groups according to their hierarchy from bottom to top. */
  private List<Entity> orderGroupListHierarchicallyFromBottomToTop(List<Entity> groupList) {

    EntityTree tree = new EntityTree();
    groupList.forEach(tree::add);

    List<Entity> hierarchicalOrder = tree.getAll();

    Collections.reverse(hierarchicalOrder);

    return hierarchicalOrder;
  }

  private EntityBackgroundOperation buildUnlockOperation(List<Entity> entityList) {

    return EntityBackgroundOperation.builder()
        .key(BackgroundOperationKey.PURGE_CLEANUP)
        .entityList(entityList)
        .consumer(
            entity -> {
              assert entity.getId().isPresent();
              String id = entity.getId().get();
              this.lockManager.unlock(id);
            })
        .build();
  }
}
