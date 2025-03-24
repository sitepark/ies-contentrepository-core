package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.GroupNotEmptyException;
import com.sitepark.ies.contentrepository.core.port.*;
import com.sitepark.ies.shared.security.exceptions.AccessDeniedException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class PurgeEntityTest {

  @Test
  void testAccessDenied() {

    AccessControl accessControl = mock(AccessControl.class);
    when(accessControl.isEntityRemovable(anyString())).thenReturn(false);

    var purgeEntity =
        new PurgeEntity(null, null, null, null, accessControl, null, null, null, null, null);
    assertThrows(
        AccessDeniedException.class,
        () -> purgeEntity.purgeEntity("10"),
        "access should be denied");
  }

  @Test
  void testGroupNotEmptyException() {

    AccessControl accessControl = mock(AccessControl.class);
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    ContentRepository repository = mock(ContentRepository.class);
    when(repository.isGroup(anyString())).thenReturn(true);
    when(repository.isEmptyGroup(anyString())).thenReturn(false);

    var purgeEntity =
        new PurgeEntity(repository, null, null, null, accessControl, null, null, null, null, null);
    assertThrows(
        GroupNotEmptyException.class,
        () -> purgeEntity.purgeEntity("10"),
        "only empty groups may be purged");
  }

  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  @Test
  void testEntityIsLocked() {

    ContentRepository repository = mock(ContentRepository.class);
    when(repository.isGroup("10")).thenReturn(false);
    AccessControl accessControl = mock(AccessControl.class);
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    EntityLockManager lockManager = mock(EntityLockManager.class);
    doThrow(new EntityLockedException(EntityLock.builder().entity("10").build()))
        .when(lockManager)
        .lock(anyString());

    var purgeEntity =
        new PurgeEntity(
            repository, lockManager, null, null, accessControl, null, null, null, null, null);
    // purgeEntity.purgeEntity(10L);

    EntityLockedException entityLockedException =
        assertThrows(
            EntityLockedException.class,
            () -> purgeEntity.purgeEntity("10"),
            "entity should be locked");
    assertEquals("10", entityLockedException.getLock().getEntity(), "unexpected entity id");
  }

  @SuppressWarnings("PMD")
  @Test
  void testPurgeEntity() {

    AccessControl accessControl = mock(AccessControl.class);
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    ContentRepository repository = mock(ContentRepository.class);
    EntityLockManager lockManager = mock(EntityLockManager.class);
    VersioningManager versioningManager = mock(VersioningManager.class);
    HistoryManager historyManager = mock(HistoryManager.class);

    RecycleBin recycleBin = mock(RecycleBin.class);
    SearchIndex searchIndex = mock(SearchIndex.class);
    MediaReferenceManager mediaReferenceManager = mock(MediaReferenceManager.class);
    Publisher publisher = mock(Publisher.class);
    ExtensionsNotifier extensionsNotifier = mock(ExtensionsNotifier.class);

    PurgeEntity purgeEntity =
        new PurgeEntity(
            repository,
            lockManager,
            versioningManager,
            historyManager,
            accessControl,
            recycleBin,
            searchIndex,
            mediaReferenceManager,
            publisher,
            extensionsNotifier);
    purgeEntity.purgeEntity("10");

    InOrder inOrder =
        inOrder(
            publisher,
            searchIndex,
            mediaReferenceManager,
            repository,
            historyManager,
            versioningManager,
            recycleBin,
            extensionsNotifier);

    inOrder.verify(publisher).depublish("10");
    inOrder.verify(searchIndex).remove("10");
    inOrder.verify(mediaReferenceManager).removeByReference("10");
    inOrder.verify(repository).removeEntity("10");
    inOrder.verify(historyManager).purge("10");
    inOrder.verify(versioningManager).removeAllVersions("10");
    inOrder.verify(recycleBin).removeByObject("10");
    inOrder.verify(extensionsNotifier).notifyPurge("10");
  }

  @SuppressWarnings("PMD")
  @Test
  void testPurgeGroup() {

    AccessControl accessControl = mock(AccessControl.class);
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    ContentRepository repository = mock(ContentRepository.class);
    when(repository.isGroup(anyString())).thenReturn(true);
    when(repository.isEmptyGroup(anyString())).thenReturn(true);
    EntityLockManager lockManager = mock(EntityLockManager.class);
    VersioningManager versioningManager = mock(VersioningManager.class);
    HistoryManager historyManager = mock(HistoryManager.class);

    RecycleBin recycleBin = mock(RecycleBin.class);
    SearchIndex searchIndex = mock(SearchIndex.class);
    MediaReferenceManager mediaReferenceManager = mock(MediaReferenceManager.class);
    Publisher publisher = mock(Publisher.class);
    ExtensionsNotifier extensionsNotifier = mock(ExtensionsNotifier.class);

    PurgeEntity purgeEntity =
        new PurgeEntity(
            repository,
            lockManager,
            versioningManager,
            historyManager,
            accessControl,
            recycleBin,
            searchIndex,
            mediaReferenceManager,
            publisher,
            extensionsNotifier);
    purgeEntity.purgeEntity("10");

    InOrder inOrder =
        inOrder(
            publisher,
            searchIndex,
            mediaReferenceManager,
            repository,
            historyManager,
            versioningManager,
            recycleBin,
            extensionsNotifier);

    inOrder.verify(publisher).depublish("10");
    inOrder.verify(searchIndex).remove("10");
    inOrder.verify(mediaReferenceManager).removeByReference("10");
    inOrder.verify(repository).removeEntity("10");
    inOrder.verify(historyManager).purge("10");
    inOrder.verify(versioningManager).removeAllVersions("10");
    inOrder.verify(recycleBin).removeByObject("10");
    inOrder.verify(extensionsNotifier).notifyPurge("10");
  }
}
