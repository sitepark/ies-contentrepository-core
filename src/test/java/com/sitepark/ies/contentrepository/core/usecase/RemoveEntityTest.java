package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.Publisher;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;

class RemoveEntityTest {

	@Test
	void testAccessDenied() {

		AccessControl accessControl = mock();
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(false);

		RemoveEntity removeEntity = new RemoveEntity(
				null,
				null,
				null,
				accessControl,
				null,
				null,
				null);

		assertThrows(AccessDeniedException.class, () -> {
			removeEntity.remove(123L);
		}, "remove should be denied access");
	}

	@Test
	void testLocked() {

		AccessControl accessControl = mock();
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(true);

		ContentRepository repository = mock();
		Entity entity = mock();
		when(repository.get(anyLong())).thenReturn(Optional.of(entity));

		EntityLock lock = mock();
		EntityLockManager lockManager = mock();
		when(lockManager.getLock(anyLong())).thenReturn(Optional.of(lock));

		RemoveEntity removeEntity = new RemoveEntity(
				repository,
				lockManager,
				null,
				accessControl,
				null,
				null,
				null);

		assertThrows(EntityLockedException.class, () -> {
			removeEntity.remove(123L);
		}, "entity should be locked");
	}

	@Test
	void test() {

		AccessControl accessControl = mock();
		when(accessControl.isEntityRemovable(anyLong())).thenReturn(true);

		ContentRepository repository = mock();
		Entity entity = mock();
		when(repository.get(anyLong())).thenReturn(Optional.of(entity));

		EntityLockManager lockManager = mock();
		HistoryManager historyManager = mock();
		RecycleBin recycleBin = mock();
		SearchIndex searchIndex = mock();
		Publisher publisher = mock();

		RemoveEntity removeEntity = new RemoveEntity(
				repository,
				lockManager,
				historyManager,
				accessControl,
				recycleBin,
				searchIndex,
				publisher);

		removeEntity.remove(123L);

		verify(repository).get(anyLong());
		verify(lockManager).getLock(anyLong());
		verify(searchIndex).remove(anyLong());
		verify(publisher).depublish(anyLong());
		verify(repository).removeEntity(anyLong());
		verify(historyManager).createEntry(anyLong(), anyLong(), any());
		verify(recycleBin).add(any());
		verify(lockManager).unlock(anyLong());
	}
}
