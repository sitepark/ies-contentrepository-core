package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class EntityTreeTest {

	@Test
	void testBuildAndGet() {
		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(10).parent(1).build());
		tree.add(Entity.builder().id(11).parent(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		List<Long> all = tree.getAll().stream()
				.map(entity -> entity.getId().get())
				.collect(Collectors.toList());

		assertThat("Unexpected entries", all, Matchers.contains(1L, 10L, 11L, 20L));
	}
}
