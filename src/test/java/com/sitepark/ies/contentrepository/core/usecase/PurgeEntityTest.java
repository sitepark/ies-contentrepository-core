package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLocked;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.MediaRepository;
import com.sitepark.ies.contentrepository.core.port.Publisher;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;

class PurgeEntityTest {

	@Test
	void testAccessDenied() {

		AccessControl accessControl = mock(AccessControl.class);
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(false);

		var purgeEntity = new PurgeEntity(
				null,
				null,
				null,
				null,
				accessControl,
				null,
				null,
				null,
				null);
		assertThrows(AccessDenied.class, () -> {
			purgeEntity.purgeEntity(10L);
		});
	}

	@Test
	void testEntityIsLocked() {

		AccessControl accessControl = mock(AccessControl.class);
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(true);

		EntityLockManager lockManager = mock(EntityLockManager.class);
		when(lockManager.getLock(anyLong()))
			.thenReturn(
					Optional.of(
							EntityLock.builder().entity(10L).build()
					)
			);

		var purgeEntity = new PurgeEntity(
				null,
				lockManager,
				null,
				null,
				accessControl,
				null,
				null,
				null,
				null);
		EntityLocked entityLocked = assertThrows(EntityLocked.class, () -> {
			purgeEntity.purgeEntity(10L);
		}, "entity should be locked");
		assertEquals(10L, entityLocked.getLock().getEntity(), "unexpected entity id");
	}

	@SuppressWarnings("PMD")
	@Test
	void testPurge() {

		AccessControl accessControl = mock(AccessControl.class);
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(true);

		ContentRepository repository = mock(ContentRepository.class);
		EntityLockManager lockManager = mock(EntityLockManager.class);
		VersioningManager versioningManager = mock(VersioningManager.class);
		HistoryManager historyManager = mock(HistoryManager.class);

		RecycleBin recycleBin = mock(RecycleBin.class);
		SearchIndex searchIndex = mock(SearchIndex.class);
		MediaRepository mediaRepository = mock(MediaRepository.class);
		Publisher publisher = mock(Publisher.class);

		PurgeEntity purgeEntity = new PurgeEntity(
				repository,
				lockManager,
				versioningManager,
				historyManager,
				accessControl,
				recycleBin,
				searchIndex,
				mediaRepository,
				publisher);
		purgeEntity.purgeEntity(10L);

		InOrder inOrder = inOrder(
				searchIndex,
				publisher,
				repository,
				historyManager,
				versioningManager,
				recycleBin,
				mediaRepository
		);

		inOrder.verify(searchIndex).remove(10L);
		inOrder.verify(publisher).depublish(10L);
		inOrder.verify(repository).removeEntity(10L);
		inOrder.verify(historyManager).purge(10L);
		inOrder.verify(versioningManager).removeAllVersions(10L);
		inOrder.verify(recycleBin).removeByObject(10L);
		inOrder.verify(mediaRepository).remove(anyList());
	}
}
