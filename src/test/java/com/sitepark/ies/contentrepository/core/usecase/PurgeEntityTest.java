package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLocked;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.ExtensionsNotifier;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.MediaReferenceManager;
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
				null,
				null);
		assertThrows(AccessDenied.class, () -> {
			purgeEntity.purgeEntity(10L);
		});
	}

	@Test
	void testEntityIsLocked() {

		ContentRepository repository = mock(ContentRepository.class);
		when(repository.isGroup(10L)).thenReturn(false);
		AccessControl accessControl = mock(AccessControl.class);
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(true);

		EntityLockManager lockManager = mock(EntityLockManager.class);
		doThrow(new EntityLocked(EntityLock.builder().entity(10L).build())).when(lockManager).lock(anyLong());

		var purgeEntity = new PurgeEntity(
				repository,
				lockManager,
				null,
				null,
				accessControl,
				null,
				null,
				null,
				null,
				null);
		//purgeEntity.purgeEntity(10L);

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
		MediaReferenceManager mediaReferenceManager = mock(MediaReferenceManager.class);
		Publisher publisher = mock(Publisher.class);
		ExtensionsNotifier extensionsNotifier = mock(ExtensionsNotifier.class);

		PurgeEntity purgeEntity = new PurgeEntity(
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
		purgeEntity.purgeEntity(10L);

		InOrder inOrder = inOrder(
				publisher,
				searchIndex,
				mediaReferenceManager,
				repository,
				historyManager,
				versioningManager,
				recycleBin,
				extensionsNotifier
		);

		inOrder.verify(publisher).depublish(10L);
		inOrder.verify(searchIndex).remove(10L);
		inOrder.verify(mediaReferenceManager).removeByReference(10L);
		inOrder.verify(repository).removeEntity(10L);
		inOrder.verify(historyManager).purge(10L);
		inOrder.verify(versioningManager).removeAllVersions(10L);
		inOrder.verify(recycleBin).removeByObject(10L);
		inOrder.verify(extensionsNotifier).notifyPurge(10L);
	}
}
