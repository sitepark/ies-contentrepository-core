package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.usecase.query.Result;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class SearchEntitiesTest {

  @Test
  void test() {

    ContentRepository repository = mock();
    Entity entity = mock();
    Result<Entity> result = new Result<>(Collections.singletonList(entity), 1, 1, 1);

    when(repository.search(any())).thenReturn(result);

    var searchEntities = new SearchEntities(repository);

    assertEquals(result, searchEntities.search(null), "unexpected entities");
  }
}
