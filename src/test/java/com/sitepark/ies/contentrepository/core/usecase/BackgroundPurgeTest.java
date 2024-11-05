package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBackgroundExecution;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBackgroundOperation;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityBackgroundExecutor;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.ExtensionsNotifier;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.MediaReferenceManager;
import com.sitepark.ies.contentrepository.core.port.Publisher;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class BackgroundPurgeTest {

  @Test
  void testAccessDeniedForEntity() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().root(123L).build();

    Entity entity = Entity.builder().id("200").build();

    ContentRepository repository = mock();
    when(repository.getAll(any())).thenReturn(Arrays.asList(entity));

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(false);

    BackgroundPurge bulkPurge =
        new BackgroundPurge(
            repository, null, null, null, accessControl, null, null, null, null, null, null);

    assertThrows(
        AccessDeniedException.class,
        () -> {
          bulkPurge.backgroundPurge(input);
        },
        "AccessDeniedException expected");
  }

  @Test
  void testAccessDeniedForGroup() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().root(123L).build();

    Entity entity = Entity.builder().id("200").isGroup(true).build();

    ContentRepository repository = mock();
    when(repository.getAll(any())).thenReturn(Arrays.asList(entity));

    AccessControl accessControl = mock();
    when(accessControl.isGroupRemoveable(anyString())).thenReturn(false);

    BackgroundPurge bulkPurge =
        new BackgroundPurge(
            repository, null, null, null, accessControl, null, null, null, null, null, null);

    assertThrows(
        AccessDeniedException.class,
        () -> {
          bulkPurge.backgroundPurge(input);
        },
        "AccessDeniedException expected");
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void test() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().root(123L).build();

    Entity entity = Entity.builder().id("200").build();

    ContentRepository repository = mock();
    when(repository.getAll(any())).thenReturn(Arrays.asList(entity));

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    EntityBackgroundExecutor entityBulkExecutor = mock();
    when(entityBulkExecutor.execute(any(EntityBackgroundExecution.class)))
        .thenAnswer(
            args -> {
              EntityBackgroundExecution execution = args.getArgument(0);

              for (EntityBackgroundOperation operation : execution.getOperations()) {
                operation.getConsumer().accept(entity);
              }
              execution.getFinalizer().get().getConsumer().accept(entity);

              return "123";
            });

    EntityLockManager lockManager = mock();
    Publisher publisher = mock();
    SearchIndex searchIndex = mock();
    MediaReferenceManager mediaReferenceManager = mock();
    HistoryManager historyManager = mock();
    VersioningManager versioningManager = mock();
    RecycleBin recycleBin = mock();
    ExtensionsNotifier extensionsNotifier = mock();

    BackgroundPurge bulkPurge =
        new BackgroundPurge(
            repository,
            lockManager,
            versioningManager,
            historyManager,
            accessControl,
            recycleBin,
            searchIndex,
            mediaReferenceManager,
            publisher,
            extensionsNotifier,
            entityBulkExecutor);

    bulkPurge.backgroundPurge(input);

    verify(lockManager).lock(anyString());
    verify(publisher).depublish(anyString());

    verify(searchIndex).remove(anyString());
    verify(mediaReferenceManager).removeByReference(anyString());
    verify(repository).removeEntity(anyString());
    verify(historyManager).purge(anyString());
    verify(versioningManager).removeAllVersions(anyString());
    verify(recycleBin).removeByObject(anyString());
    verify(extensionsNotifier).notifyPurge(anyString());

    verify(lockManager).unlock(anyString());
  }
}
