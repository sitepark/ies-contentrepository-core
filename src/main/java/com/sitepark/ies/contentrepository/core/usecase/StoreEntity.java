package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.ChangeSet;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.entity.Entry;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.ParentMissingException;
import com.sitepark.ies.contentrepository.core.domain.service.ContentDiffer;
import com.sitepark.ies.contentrepository.core.domain.service.ReferenceExtractor;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.IdGenerator;
import com.sitepark.ies.contentrepository.core.port.ReferenceRepository;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.SectionSchemaValidator;
import com.sitepark.ies.contentrepository.core.port.SectionTypeProvider;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import com.sitepark.ies.sharedkernel.security.AccessDeniedException;
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
  private final SectionChecks sectionChecks;
  private final ReferenceRepository referenceRepository;
  private final ReferenceExtractor referenceExtractor = new ReferenceExtractor();

  @SuppressWarnings("PMD.ExcessiveParameterList")
  StoreEntity(
      ContentRepository repository,
      EntityLockManager lockManager,
      VersioningManager versioningManager,
      HistoryManager historyManager,
      AccessControl accessControl,
      IdGenerator idGenerator,
      SearchIndex searchIndex,
      ContentDiffer contentDiffer,
      SectionTypeProvider sectionTypes,
      SectionSchemaValidator schemaValidator,
      ReferenceRepository referenceRepository) {
    this.repository = repository;
    this.lockManager = lockManager;
    this.versioningManager = versioningManager;
    this.historyManager = historyManager;
    this.accessControl = accessControl;
    this.idGenerator = idGenerator;
    this.searchIndex = searchIndex;
    this.contentDiffer = contentDiffer;
    this.sectionChecks = new SectionChecks(accessControl, sectionTypes, schemaValidator);
    this.referenceRepository = referenceRepository;
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
    if (parent.isEmpty()) {
      throw new ParentMissingException();
    }

    String parentId = parent.get();

    if (!this.accessControl.isEntityCreatable(parentId)) {
      throw new AccessDeniedException("Not allowed to create entity in group " + parent);
    }

    this.checkSections(parentId, newEntity);

    String generatedId = this.idGenerator.generate();

    Entity entityWithId = newEntity.toBuilder().id(generatedId).build();

    Entity versioned = this.versioningManager.createNewVersion(entityWithId);

    assert versioned.getVersion().isPresent();

    this.repository.store(versioned);
    this.replaceReferences(versioned);
    this.historyManager.createEntry(
        generatedId, versioned.getVersion().get(), HistoryEntryType.CREATED);
    this.searchIndex.index(generatedId);

    assert versioned.getId().isPresent();
    return versioned.getId().get();
  }

  private String update(Entity updateEntity) {

    if (updateEntity.getId().isEmpty()) {
      throw new IllegalArgumentException("Update failed, identifier missing");
    }

    String id = updateEntity.getId().get();

    Optional<Entity> existsEntity = this.repository.get(id);
    if (existsEntity.isEmpty()) {
      throw new EntityNotFoundException(id);
    }

    if (!this.accessControl.isEntityWritable(id)) {
      throw new AccessDeniedException("Not allowed to update entity " + id);
    }

    this.checkSections(id, updateEntity);

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
      this.replaceReferences(versioned);
      this.historyManager.createEntry(id, versioned.getVersion().get(), HistoryEntryType.UPDATED);
      this.searchIndex.index(id);

      assert versioned.getId().isPresent();
      return versioned.getId().get();

    } finally {
      this.lockManager.unlock(id);
    }
  }

  /**
   * Checks type, grant and schema of the sections of an entry.
   *
   * @param grantScope the id the grants are checked on: the parent for a new entry, the entry
   *     itself otherwise
   */
  private void checkSections(String grantScope, Entity entity) {
    if (entity instanceof Entry entry) {
      entry.getSections().forEach(section -> this.sectionChecks.check(grantScope, section));
    }
  }

  private void replaceReferences(Entity entity) {
    if (entity instanceof Entry entry && entry.getId().isPresent()) {
      String id = entry.getId().get();
      this.referenceRepository.replace(
          id, this.referenceExtractor.extract(id, entry.getSections()));
    }
  }
}
