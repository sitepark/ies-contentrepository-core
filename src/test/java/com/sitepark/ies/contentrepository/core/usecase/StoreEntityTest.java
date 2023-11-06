package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.ChangeSet;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.ParentMissingException;
import com.sitepark.ies.contentrepository.core.domain.service.ContentDiffer;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.IdGenerator;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;

class StoreEntityTest {

	@Test
	void testParentMissing() {

		Entity entity = mock();
		when(entity.getParent()).thenReturn(Optional.empty());

		StoreEntity storeEntity = new StoreEntity(
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null);

		assertThrows(ParentMissingException.class, () -> {
			storeEntity.store(entity);
		}, "entity must have a parent");
	}

	@Test
	void testCreateAccessDenied() {

		Entity entity = mock();
		when(entity.getParent()).thenReturn(Optional.of(123L));

		AccessControl accessControl = mock();
		when(accessControl.isEntityCreateable(anyLong())).thenReturn(false);

		StoreEntity storeEntity = new StoreEntity(
				null,
				null,
				null,
				null,
				accessControl,
				null,
				null,
				null);

		assertThrows(AccessDeniedException.class, () -> {
			storeEntity.store(entity);
		}, "should be access denied");
	}

	@Test
	void testCreate() {

		Entity entity = Entity.builder()
				.parent(345L)
				.build();

		AccessControl accessControl = mock();
		when(accessControl.isEntityCreateable(anyLong())).thenReturn(true);

		ContentRepository repository = mock();
		VersioningManager versioningManager = mock();
		HistoryManager historyManager = mock();
		IdGenerator idGenerator = mock();
		SearchIndex searchIndex = mock();
		ContentDiffer contentDiffer = mock();

		when(idGenerator.generate()).thenReturn(123L);

		when(versioningManager.createNewVersion(any(Entity.class))).thenAnswer(args -> {
			Entity arg = args.getArgument(0);
			return arg.toBuilder().version(2L).build();
		});

		StoreEntity storeEntity = new StoreEntity(
				repository,
				null,
				versioningManager,
				historyManager,
				accessControl,
				idGenerator,
				searchIndex,
				contentDiffer);

		long id = storeEntity.store(entity);

		verify(repository).store(any());
		verify(historyManager).createEntry(anyLong(), anyLong(), any());
		verify(searchIndex).index(anyLong());

		assertEquals(123L, id, "unexpected generated id");
	}

	@Test
	void testUpdateNotFound() {

		Entity entity = Entity.builder()
				.id(123L)
				.parent(345L)
				.build();

		ContentRepository repository = mock();

		when(repository.get(anyLong())).thenReturn(Optional.empty());

		StoreEntity storeEntity = new StoreEntity(
				repository,
				null,
				null,
				null,
				null,
				null,
				null,
				null);

		assertThrows(EntityNotFoundException.class, () -> {
			storeEntity.store(entity);
		}, "should be not found");
	}

	@Test
	void testUpdateAccessDenied() {

		Entity entity = Entity.builder()
				.id(123L)
				.parent(345L)
				.build();

		AccessControl accessControl = mock();
		when(accessControl.isEntityWritable(anyLong())).thenReturn(false);

		ContentRepository repository = mock();

		when(repository.get(anyLong())).thenReturn(Optional.of(entity));

		StoreEntity storeEntity = new StoreEntity(
				repository,
				null,
				null,
				null,
				accessControl,
				null,
				null,
				null);

		assertThrows(AccessDeniedException.class, () -> {
			storeEntity.store(entity);
		}, "should be access denied");
	}

	@Test
	void testUpdateEntityIsLocked() {

		Entity entity = Entity.builder()
				.id(123L)
				.parent(345L)
				.build();

		AccessControl accessControl = mock();
		when(accessControl.isEntityWritable(anyLong())).thenReturn(true);

		ContentRepository repository = mock();
		when(repository.get(anyLong())).thenReturn(Optional.of(entity));

		EntityLock entityLock = mock();
		EntityLockManager lockManager = mock();
		when(lockManager.getLock(anyLong())).thenReturn(Optional.of(entityLock));


		StoreEntity storeEntity = new StoreEntity(
				repository,
				lockManager,
				null,
				null,
				accessControl,
				null,
				null,
				null);

		assertThrows(EntityLockedException.class, () -> {
			storeEntity.store(entity);
		}, "entity should be locked");
	}

	@Test
	void testUpdate() {

		Entity entity = Entity.builder()
				.id(123L)
				.parent(345L)
				.build();

		AccessControl accessControl = mock();
		when(accessControl.isEntityWritable(anyLong())).thenReturn(true);

		ContentRepository repository = mock();
		EntityLockManager lockManager = mock();
		VersioningManager versioningManager = mock();
		HistoryManager historyManager = mock();
		SearchIndex searchIndex = mock();
		ContentDiffer contentDiffer = mock();
		ChangeSet changeSet = mock();

		when(repository.get(anyLong())).thenReturn(Optional.of(entity));
		when(contentDiffer.diff(any(), any())).thenReturn(changeSet);
		when(changeSet.isEmpty()).thenReturn(false);

		when(versioningManager.createNewVersion(any(Entity.class))).thenAnswer(args -> {
			Entity arg = args.getArgument(0);
			return arg.toBuilder().version(2L).build();
		});

		StoreEntity storeEntity = new StoreEntity(
				repository,
				lockManager,
				versioningManager,
				historyManager,
				accessControl,
				null,
				searchIndex,
				contentDiffer);

		long id = storeEntity.store(entity);

		verify(repository).store(any());
		verify(historyManager).createEntry(anyLong(), anyLong(), any());
		verify(searchIndex).index(anyLong());
		verify(lockManager).unlock(anyLong());

		assertEquals(123L, id, "unexpected generated id");
	}

}
