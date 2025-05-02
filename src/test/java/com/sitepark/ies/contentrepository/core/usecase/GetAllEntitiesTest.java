package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class GetAllEntitiesTest {

  @Test
  void test() {

    ContentRepository repository = mock();
    Entity entity = mock();
    List<Entity> entries = Collections.singletonList(entity);

    when(repository.getAll(any())).thenReturn(entries);

    var getAllEntities = new GetAllEntities(repository);

    assertEquals(entries, getAllEntities.getAllEntities(null), "unexpected entities");
  }
}
