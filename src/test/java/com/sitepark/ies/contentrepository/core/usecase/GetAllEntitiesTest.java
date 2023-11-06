package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;

class GetAllEntitiesTest {

	@Test
	void test() {

		ContentRepository repository = mock();
		Entity entity = mock();
		List<Entity> list = Arrays.asList(entity);
		when(repository.getAll(any())).thenReturn(list);

		var getAllEntities = new GetAllEntities(repository);

		assertEquals(list, getAllEntities.getAllEntities(null));
	}
}
