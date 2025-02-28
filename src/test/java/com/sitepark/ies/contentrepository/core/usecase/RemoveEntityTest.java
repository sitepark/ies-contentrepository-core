package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.port.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RemoveEntityTest {

  @Test
  void testAccessDenied() {

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(false);

    RemoveEntity removeEntity = new RemoveEntity(null, null, null, accessControl, null, null, null);

    assertThrows(
        AccessDeniedException.class,
        () -> removeEntity.remove("123"),
        "remove should be denied access");
  }

  @Test
  void testLocked() {

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    ContentRepository repository = mock();
    Entity entity = mock();
    when(repository.get(anyString())).thenReturn(Optional.of(entity));

    EntityLock lock = mock();
    EntityLockManager lockManager = mock();
    when(lockManager.getLock(anyString())).thenReturn(Optional.of(lock));

    RemoveEntity removeEntity =
        new RemoveEntity(repository, lockManager, null, accessControl, null, null, null);

    assertThrows(
        EntityLockedException.class, () -> removeEntity.remove("123"), "entity should be locked");
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  void test() {

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    ContentRepository repository = mock();
    Entity entity = mock();
    when(repository.get(anyString())).thenReturn(Optional.of(entity));

    EntityLockManager lockManager = mock();
    HistoryManager historyManager = mock();
    RecycleBin recycleBin = mock();
    SearchIndex searchIndex = mock();
    Publisher publisher = mock();

    RemoveEntity removeEntity =
        new RemoveEntity(
            repository,
            lockManager,
            historyManager,
            accessControl,
            recycleBin,
            searchIndex,
            publisher);

    removeEntity.remove("123");

    verify(repository).get(anyString());
    verify(lockManager).getLock(anyString());
    verify(searchIndex).remove(anyString());
    verify(publisher).depublish(anyString());
    verify(repository).removeEntity(anyString());
    verify(historyManager).createEntry(anyString(), any(), any());
    verify(recycleBin).add(any());
    verify(lockManager).unlock(anyString());
  }
}
