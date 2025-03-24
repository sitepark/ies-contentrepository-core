package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.port.*;
import com.sitepark.ies.shared.security.exceptions.AccessDeniedException;
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
    when(accessControl.isGroupCreatable(anyString())).thenReturn(false);

    RecoverEntity recoverEntity =
        new RecoverEntity(repository, historyManager, accessControl, recycleBin, searchIndex);

    assertThrows(
        AccessDeniedException.class,
        () -> recoverEntity.recover("123"),
        "recover should be denied access");
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  void test() {

    ContentRepository repository = mock();
    HistoryManager historyManager = mock();
    RecycleBin recycleBin = mock();
    SearchIndex searchIndex = mock();

    RecycleBinItem recycleBinItem = mock();
    when(recycleBin.get(anyString())).thenReturn(Optional.of(recycleBinItem));

    AccessControl accessControl = mock();
    when(accessControl.isGroupCreatable(any())).thenReturn(true);

    RecoverEntity recoverEntity =
        new RecoverEntity(repository, historyManager, accessControl, recycleBin, searchIndex);

    recoverEntity.recover("123");

    verify(repository).store(any());
    verify(historyManager).createEntry(any(), any(), any());
    verify(searchIndex).index(anyString());
  }
}
