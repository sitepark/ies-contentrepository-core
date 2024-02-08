package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RecoverEntityTest {

  @Test
  void testAccessDenied() {

    ContentRepository repository = mock();
    HistoryManager historyManager = mock();
    RecycleBin recycleBin = mock();
    SearchIndex searchIndex = mock();

    RecycleBinItem recycleBinItem = mock();
    when(recycleBin.get(anyString())).thenReturn(Optional.of(recycleBinItem));

    AccessControl accessControl = mock();
    when(accessControl.isGroupCreateable(anyString())).thenReturn(false);

    RecoverEntity recoverEntity =
        new RecoverEntity(repository, historyManager, accessControl, recycleBin, searchIndex);

    assertThrows(
        AccessDeniedException.class,
        () -> {
          recoverEntity.recover("123");
        },
        "recover should be denied access");
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  void test() {

    ContentRepository repository = mock();
    HistoryManager historyManager = mock();
    RecycleBin recycleBin = mock();
    SearchIndex searchIndex = mock();

    RecycleBinItem recycleBinItem = mock();
    when(recycleBin.get(anyString())).thenReturn(Optional.of(recycleBinItem));

    AccessControl accessControl = mock();
    when(accessControl.isGroupCreateable(any())).thenReturn(true);

    RecoverEntity recoverEntity =
        new RecoverEntity(repository, historyManager, accessControl, recycleBin, searchIndex);

    recoverEntity.recover("123");

    verify(repository).store(any());
    verify(historyManager).createEntry(any(), any(), any());
    verify(searchIndex).index(anyString());
  }
}
