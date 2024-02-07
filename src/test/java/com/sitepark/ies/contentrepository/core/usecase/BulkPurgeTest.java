package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBulkExecution;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityBulkOperation;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
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

class BulkPurgeTest {

	@Test
	void testAccessDeniedForEntity() {

		BulkPurgeInput input = BulkPurgeInput.builder()
				.root(123L)
				.build();

		Entity entity = Entity.builder()
				.id("200")
				.build();

		ContentRepository repository = mock();
		when(repository.getAll(any())).thenReturn(Arrays.asList(entity));

		AccessControl accessControl = mock();
		when(accessControl.isEntityRemovable(anyString())).thenReturn(false);

		BulkPurge bulkPurge = new BulkPurge(
				repository,
				null,
				null,
				null,
				accessControl,
				null,
				null,
				null,
				null,
				null,
				null);

		assertThrows(AccessDeniedException.class, () -> {
			bulkPurge.bulkPurge(input);
		}, "AccessDeniedException expected");
	}

	@Test
	void testAccessDeniedForGroup() {

		BulkPurgeInput input = BulkPurgeInput.builder()
				.root(123L)
				.build();

		Entity entity = Entity.builder()
				.id("200")
				.isGroup(true)
				.build();

		ContentRepository repository = mock();
		when(repository.getAll(any())).thenReturn(Arrays.asList(entity));

		AccessControl accessControl = mock();
		when(accessControl.isGroupRemoveable(anyString())).thenReturn(false);

		BulkPurge bulkPurge = new BulkPurge(
				repository,
				null,
				null,
				null,
				accessControl,
				null,
				null,
				null,
				null,
				null,
				null);

		assertThrows(AccessDeniedException.class, () -> {
			bulkPurge.bulkPurge(input);
		}, "AccessDeniedException expected");
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
	void test() {

		BulkPurgeInput input = BulkPurgeInput.builder()
				.root(123L)
				.build();

		Entity entity = Entity.builder()
				.id("200")
				.build();

		ContentRepository repository = mock();
		when(repository.getAll(any())).thenReturn(Arrays.asList(entity));

		AccessControl accessControl = mock();
		when(accessControl.isEntityRemovable(anyString())).thenReturn(true);

		EntityBulkExecutor entityBulkExecutor = mock();
		when(entityBulkExecutor.execute(any(EntityBulkExecution.class))).thenAnswer(args -> {

			EntityBulkExecution execution = args.getArgument(0);

			for (EntityBulkOperation operation : execution.getOperations()) {
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

		BulkPurge bulkPurge = new BulkPurge(
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

		bulkPurge.bulkPurge(input);

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
