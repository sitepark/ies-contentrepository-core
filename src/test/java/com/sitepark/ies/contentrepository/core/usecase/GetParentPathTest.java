package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GetParentPathTest {
  @Test
  void testGetParentPath() {

    ContentRepository repository = mock();
    when(repository.getParentPath(any()))
        .thenReturn(Map.of("1", List.of("2", "3"), "4", List.of("5", "6")));

    GetParentPath usecase = new GetParentPath(repository);
    Map<String, List<String>> parentPath = usecase.getParentPath(List.of("1", "4"));

    Map<String, List<String>> expected = Map.of("1", List.of("2", "3"), "4", List.of("5", "6"));
    assertEquals(expected, parentPath, "Unexpected parent path result");
  }
}
