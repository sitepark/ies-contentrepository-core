package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;

class RecoverEntityTest {

	@Test
	void testAccessDenied() {

		ContentRepository repository = mock();
		HistoryManager historyManager = mock();
		RecycleBin recycleBin = mock();
		SearchIndex searchIndex = mock();

		RecycleBinItem recycleBinItem = mock();
		when(recycleBin.get(anyLong())).thenReturn(Optional.of(recycleBinItem));

		AccessControl accessControl = mock();
		when(accessControl.isGroupCreateable(anyLong())).thenReturn(false);

		RecoverEntity recoverEntity = new RecoverEntity(
				repository,
				historyManager,
				accessControl,
				recycleBin,
				searchIndex);

		assertThrows(AccessDeniedException.class, () -> {
			recoverEntity.recover(123L);
		}, "recover should be denied access");
	}

	@Test
	void test() {

		ContentRepository repository = mock();
		HistoryManager historyManager = mock();
		RecycleBin recycleBin = mock();
		SearchIndex searchIndex = mock();

		RecycleBinItem recycleBinItem = mock();
		when(recycleBin.get(anyLong())).thenReturn(Optional.of(recycleBinItem));

		AccessControl accessControl = mock();
		when(accessControl.isGroupCreateable(anyLong())).thenReturn(true);

		RecoverEntity recoverEntity = new RecoverEntity(
				repository,
				historyManager,
				accessControl,
				recycleBin,
				searchIndex);

		recoverEntity.recover(123L);

		verify(repository).store(any());
		verify(historyManager).createEntry(anyLong(), anyLong(), any());
		verify(searchIndex).index(anyLong());
	}

}
