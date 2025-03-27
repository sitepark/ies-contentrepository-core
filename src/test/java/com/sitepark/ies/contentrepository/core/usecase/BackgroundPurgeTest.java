package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBackgroundExecution;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBackgroundOperation;
import com.sitepark.ies.contentrepository.core.port.*;
import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import com.sitepark.ies.shared.security.exceptions.AccessDeniedException;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class BackgroundPurgeTest {

  @Test
  void testAccessDeniedForEntity() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().filter(Filter.root("123")).build();

    Entity entity = Entity.builder().id("200").build();

    ContentRepository repository = mock();
    when(repository.getAll(any())).thenReturn(Collections.singletonList(entity));

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(false);

    BackgroundPurge backgroundPurge =
        new BackgroundPurge(
            repository, null, null, null, accessControl, null, null, null, null, null, null);

    assertThrows(
        AccessDeniedException.class,
        () -> backgroundPurge.backgroundPurge(input),
        "AccessDeniedException expected");
  }

  @Test
  void testAccessDeniedForGroup() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().filter(Filter.root("123")).build();

    Entity entity = Entity.builder().id("200").isGroup(true).build();

    ContentRepository repository = mock();
    when(repository.getAll(any())).thenReturn(Collections.singletonList(entity));

    AccessControl accessControl = mock();
    when(accessControl.isGroupRemovable(anyString())).thenReturn(false);

    BackgroundPurge backgroundPurge =
        new BackgroundPurge(
            repository, null, null, null, accessControl, null, null, null, null, null, null);

    assertThrows(
        AccessDeniedException.class,
        () -> backgroundPurge.backgroundPurge(input),
        "AccessDeniedException expected");
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  void test() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().filter(Filter.root("123")).build();

    Entity entity = Entity.builder().id("200").build();

    ContentRepository repository = mock();
    when(repository.getAll(any())).thenReturn(Collections.singletonList(entity));

    AccessControl accessControl = mock();
    when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

    EntityBackgroundExecutor entityBackgroundExecutor = mock();
    when(entityBackgroundExecutor.execute(any(EntityBackgroundExecution.class)))
        .thenAnswer(
            args -> {
              EntityBackgroundExecution execution = args.getArgument(0);

              for (EntityBackgroundOperation operation : execution.getOperations()) {
                operation.getConsumer().accept(entity);
              }
              assert execution.getFinalizer().isPresent();
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

    BackgroundPurge backgroundPurge =
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
            entityBackgroundExecutor);

    backgroundPurge.backgroundPurge(input);

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
