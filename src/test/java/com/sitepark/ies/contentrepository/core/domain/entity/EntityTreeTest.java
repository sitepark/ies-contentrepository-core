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

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("2").build());
    tree.add(Entity.builder().id("10").parent("1").build());
    tree.add(Entity.builder().id("11").parent("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    List<String> all =
        tree.getRoots().stream().map(entity -> entity.getId().get()).collect(Collectors.toList());

    assertThat("Unexpected entries", all, Matchers.contains("1", "2"));
  }

  @Test
  void testGetRootsWithInvalidState() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    assertThrows(
        IllegalStateException.class,
        () -> {
          tree.getRoots();
        },
        "Entity 2 is missing. Here an IllegalStateException should be thrown");
  }

  @Test
  void testGetChildren() {

    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("2").build());
    tree.add(Entity.builder().id("10").parent("1").build());
    tree.add(Entity.builder().id("11").parent("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    List<String> all =
        tree.getChildren("1").stream()
            .map(entity -> entity.getId().get())
            .collect(Collectors.toList());

    assertThat("Unexpected chilldren", all, Matchers.contains("11", "10"));
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

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("10").parent("1").build());

    assertTrue(tree.hasChildren("1"), "entity should have children");
  }

  @Test
  void testGet() {

    EntityTree tree = new EntityTree();
    tree.add(Entity.builder().id("1").build());

    Entity entity = tree.get("1");

    assertEquals("1", entity.getId().get(), "unexpected entity");
  }

  @Test
  void testGetAll() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("2").build());
    tree.add(Entity.builder().id("10").parent("1").build());
    tree.add(Entity.builder().id("11").parent("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    List<String> all =
        tree.getAll().stream().map(entity -> entity.getId().get()).collect(Collectors.toList());

    assertThat("Unexpected entries", all, Matchers.containsInAnyOrder("1", "10", "11", "2", "20"));
  }

  @Test
  void testGetAllWithInvalidState() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    assertThrows(
        IllegalStateException.class,
        () -> {
          tree.getAll();
        },
        "Entity 2 is missing. Here an IllegalStateException should be thrown");
  }

  @Test
  void testToString() {
    EntityTree tree = new EntityTree();
    tree.add(Entity.builder().id("1").build());

    String s = tree.toString();
    assertTrue(s.contains("id=1,"), "entity with id 1 not found");
  }

  @Test
  void testToStringWithIndent() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("2").build());
    tree.add(Entity.builder().id("10").parent("1").build());
    tree.add(Entity.builder().id("11").parent("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    String s = tree.toString(3);

    assertTrue(s.contains("\n   Entity"), "indent missing");
  }
}
