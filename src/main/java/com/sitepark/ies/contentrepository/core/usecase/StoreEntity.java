package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.ChangeSet;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.ParentMissingException;
import com.sitepark.ies.contentrepository.core.domain.service.ContentDiffer;
import com.sitepark.ies.contentrepository.core.port.*;
import java.util.Optional;

public final class StoreEntity {

  private final ContentRepository repository;
  private final EntityLockManager lockManager;
  private final VersioningManager versioningManager;
  private final HistoryManager historyManager;
  private final AccessControl accessControl;
  private final IdGenerator idGenerator;
  private final SearchIndex searchIndex;
  private final ContentDiffer contentDiffer;

  StoreEntity(
      ContentRepository repository,
      EntityLockManager lockManager,
      VersioningManager versioningManager,
      HistoryManager historyManager,
      AccessControl accessControl,
      IdGenerator idGenerator,
      SearchIndex searchIndex,
      ContentDiffer contentDiffer) {
    this.repository = repository;
    this.lockManager = lockManager;
    this.versioningManager = versioningManager;
    this.historyManager = historyManager;
    this.accessControl = accessControl;
    this.idGenerator = idGenerator;
    this.searchIndex = searchIndex;
    this.contentDiffer = contentDiffer;
  }

  public String store(Entity entity) {
    if (entity.getId().isEmpty()) {
      return this.create(entity);
    } else {
      return this.update(entity);
    }
  }

  private String create(Entity newEntity) {

    Optional<String> parent = newEntity.getParent();
    parent.orElseThrow(ParentMissingException::new);

    String parentId = parent.get();

    if (!this.accessControl.isEntityCreatable(parentId)) {
      throw new AccessDeniedException("Not allowed to create entity in group " + parent);
    }

    String generatedId = this.idGenerator.generate();

    Entity entityWithId = newEntity.toBuilder().id(generatedId).build();

    Entity versioned = this.versioningManager.createNewVersion(entityWithId);

    assert versioned.getVersion().isPresent();

    this.repository.store(versioned);
    this.historyManager.createEntry(
        generatedId, versioned.getVersion().get(), HistoryEntryType.CREATED);
    this.searchIndex.index(generatedId);

    assert versioned.getId().isPresent();
    return versioned.getId().get();
  }

  private String update(Entity updateEntity) {

    updateEntity
        .getId()
        .orElseThrow(() -> new IllegalArgumentException("Update failed, identifier missing"));

    String id = updateEntity.getId().get();

    Optional<Entity> existsEntity = this.repository.get(id);
    existsEntity.orElseThrow(() -> new EntityNotFoundException(id));

    if (!this.accessControl.isEntityWritable(id)) {
      throw new AccessDeniedException("Not allowed to update entity " + id);
    }

    try {
      Optional<EntityLock> lock = this.lockManager.getLock(id);
      lock.ifPresent(
          l -> {
            throw new EntityLockedException(l);
          });

      ChangeSet changeSet = this.contentDiffer.diff(updateEntity, existsEntity.get());
      if (changeSet.isEmpty()) {
        return updateEntity.getId().get();
      }

      Entity versioned = this.versioningManager.createNewVersion(updateEntity);
      assert versioned.getVersion().isPresent();

      this.repository.store(versioned);
      this.historyManager.createEntry(id, versioned.getVersion().get(), HistoryEntryType.UPDATED);
      this.searchIndex.index(id);

      assert versioned.getId().isPresent();
      return versioned.getId().get();

    } finally {
      this.lockManager.unlock(id);
    }
  }
}
