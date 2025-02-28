package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sitepark.ies.contentrepository.core.domain.entity.ChangeSet;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.ParentMissingException;
import com.sitepark.ies.contentrepository.core.domain.service.ContentDiffer;
import com.sitepark.ies.contentrepository.core.port.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class StoreEntityTest {

  @Test
  void testParentMissing() {

    Entity entity = mock();
    when(entity.getParent()).thenReturn(Optional.empty());

    StoreEntity storeEntity = new StoreEntity(null, null, null, null, null, null, null, null);

    assertThrows(
        ParentMissingException.class, () -> storeEntity.store(entity), "entity must have a parent");
  }

  @Test
  void testCreateAccessDenied() {

    Entity entity = mock();
    when(entity.getParent()).thenReturn(Optional.of("123"));

    AccessControl accessControl = mock();
    when(accessControl.isEntityCreatable(anyString())).thenReturn(false);

    StoreEntity storeEntity =
        new StoreEntity(null, null, null, null, accessControl, null, null, null);

    assertThrows(
        AccessDeniedException.class, () -> storeEntity.store(entity), "should be access denied");
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  void testCreate() {

    Entity entity = Entity.builder().parent("345").build();

    AccessControl accessControl = mock();
    when(accessControl.isEntityCreatable(anyString())).thenReturn(true);

    ContentRepository repository = mock();
    VersioningManager versioningManager = mock();
    HistoryManager historyManager = mock();
    IdGenerator idGenerator = mock();
    SearchIndex searchIndex = mock();
    ContentDiffer contentDiffer = mock();

    when(idGenerator.generate()).thenReturn("123");

    OffsetDateTime version = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);

    when(versioningManager.createNewVersion(any(Entity.class)))
        .thenAnswer(
            args -> {
              Entity arg = args.getArgument(0);
              return arg.toBuilder().version(version).build();
            });

    StoreEntity storeEntity =
        new StoreEntity(
            repository,
            null,
            versioningManager,
            historyManager,
            accessControl,
            idGenerator,
            searchIndex,
            contentDiffer);

    String id = storeEntity.store(entity);

    verify(repository).store(any());
    verify(historyManager).createEntry(anyString(), any(), any());
    verify(searchIndex).index(anyString());

    assertEquals("123", id, "unexpected generated id");
  }

  @Test
  void testUpdateNotFound() {

    Entity entity = Entity.builder().id("123").parent("345").build();

    ContentRepository repository = mock();

    when(repository.get(anyString())).thenReturn(Optional.empty());

    StoreEntity storeEntity = new StoreEntity(repository, null, null, null, null, null, null, null);

    assertThrows(
        EntityNotFoundException.class, () -> storeEntity.store(entity), "should be not found");
  }

  @Test
  void testUpdateAccessDenied() {

    Entity entity = Entity.builder().id("123").parent("345").build();

    AccessControl accessControl = mock();
    when(accessControl.isEntityWritable(anyString())).thenReturn(false);

    ContentRepository repository = mock();

    when(repository.get(anyString())).thenReturn(Optional.of(entity));

    StoreEntity storeEntity =
        new StoreEntity(repository, null, null, null, accessControl, null, null, null);

    assertThrows(
        AccessDeniedException.class, () -> storeEntity.store(entity), "should be access denied");
  }

  @Test
  void testUpdateEntityIsLocked() {

    Entity entity = Entity.builder().id("123").parent("345").build();

    AccessControl accessControl = mock();
    when(accessControl.isEntityWritable(anyString())).thenReturn(true);

    ContentRepository repository = mock();
    when(repository.get(anyString())).thenReturn(Optional.of(entity));

    EntityLock entityLock = mock();
    EntityLockManager lockManager = mock();
    when(lockManager.getLock(anyString())).thenReturn(Optional.of(entityLock));

    StoreEntity storeEntity =
        new StoreEntity(repository, lockManager, null, null, accessControl, null, null, null);

    assertThrows(
        EntityLockedException.class, () -> storeEntity.store(entity), "entity should be locked");
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  void testUpdate() {

    Entity entity = Entity.builder().id("123").parent("345").build();

    AccessControl accessControl = mock();
    when(accessControl.isEntityWritable(anyString())).thenReturn(true);

    ContentRepository repository = mock();
    EntityLockManager lockManager = mock();
    VersioningManager versioningManager = mock();
    HistoryManager historyManager = mock();
    SearchIndex searchIndex = mock();
    ContentDiffer contentDiffer = mock();
    ChangeSet changeSet = mock();

    when(repository.get(anyString())).thenReturn(Optional.of(entity));
    when(contentDiffer.diff(any(), any())).thenReturn(changeSet);
    when(changeSet.isEmpty()).thenReturn(false);

    OffsetDateTime version = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);

    when(versioningManager.createNewVersion(any(Entity.class)))
        .thenAnswer(
            args -> {
              Entity arg = args.getArgument(0);
              return arg.toBuilder().version(version).build();
            });

    StoreEntity storeEntity =
        new StoreEntity(
            repository,
            lockManager,
            versioningManager,
            historyManager,
            accessControl,
            null,
            searchIndex,
            contentDiffer);

    String id = storeEntity.store(entity);

    verify(repository).store(any());
    verify(historyManager).createEntry(anyString(), any(), any());
    verify(searchIndex).index(anyString());
    verify(lockManager).unlock(anyString());

    assertEquals("123", id, "unexpected generated id");
  }
}
