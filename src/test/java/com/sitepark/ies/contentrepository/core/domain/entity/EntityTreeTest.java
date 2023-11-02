package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class EntityTreeTest {

	@Test
	void testGetRoots() {
		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(2).build());
		tree.add(Entity.builder().id(10).parent(1).build());
		tree.add(Entity.builder().id(11).parent(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		List<Long> all = tree.getRoots().stream()
				.map(entity -> entity.getId().get())
				.collect(Collectors.toList());

		assertThat("Unexpected entries", all, Matchers.contains(1L, 2L));
	}

	@Test
	void testGetRootsWithInvalidState() {
		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		assertThrows(IllegalStateException.class, () -> {
			tree.getRoots();
		}, "Entity 2 is missing. Here an IllegalStateException should be thrown");
	}

	@Test
	void testGetChildren() {

		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(2).build());
		tree.add(Entity.builder().id(10).parent(1).build());
		tree.add(Entity.builder().id(11).parent(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		List<Long> all = tree.getChildren(1L).stream()
				.map(entity -> entity.getId().get())
				.collect(Collectors.toList());

		assertThat("Unexpected chilldren", all, Matchers.contains(10L, 11L));
	}

	@Test
	void testaddWithNull() {

		EntityTree tree = new EntityTree();
		tree.add(null);

		assertTrue(tree.getAll().isEmpty(), "tree should be empty");
	}

	@Test
	void testHasChildren() {

		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(10).parent(1).build());

		assertTrue(tree.hasChildren(1L), "entity should have children");

	}

	@Test
	void testGet() {

		EntityTree tree = new EntityTree();
		tree.add(Entity.builder().id(1).build());

		Entity entity = tree.get(1L);

		assertEquals(1L, entity.getId().get(), "unexpected entity");
	}

	@Test
	void testGetAll() {
		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(2).build());
		tree.add(Entity.builder().id(10).parent(1).build());
		tree.add(Entity.builder().id(11).parent(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		List<Long> all = tree.getAll().stream()
				.map(entity -> entity.getId().get())
				.collect(Collectors.toList());

		assertThat("Unexpected entries", all, Matchers.contains(1L, 10L, 11L, 2L, 20L));
	}

	@Test
	void testGetAllWithInvalidState() {
		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		assertThrows(IllegalStateException.class, () -> {
			tree.getAll();
		}, "Entity 2 is missing. Here an IllegalStateException should be thrown");
	}

	@Test
	void testToString() {
		EntityTree tree = new EntityTree();
		tree.add(Entity.builder().id(1).build());

		String s = tree.toString();

		assertTrue(s.contains("id: 1,"), "entity with id 1 not found");
	}

	@Test
	void testToStringWithIndent() {
		EntityTree tree = new EntityTree();

		tree.add(Entity.builder().id(1).build());
		tree.add(Entity.builder().id(2).build());
		tree.add(Entity.builder().id(10).parent(1).build());
		tree.add(Entity.builder().id(11).parent(1).build());
		tree.add(Entity.builder().id(20).parent(2).build());

		String s = tree.toString(3);

		assertTrue(s.contains("\n   Entity"), "indent missing");
	}
}
