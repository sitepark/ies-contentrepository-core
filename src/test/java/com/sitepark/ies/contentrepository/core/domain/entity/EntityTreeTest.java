package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class EntityTreeTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(EntityTree.class).verify();
  }

  @Test
  void testGetRoots() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("2").build());
    tree.add(Entity.builder().id("10").parent("1").build());
    tree.add(Entity.builder().id("11").parent("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    List<String> all =
        tree.getRoots().stream()
            .map(entity -> entity.getId().orElse(""))
            .collect(Collectors.toList());

    assertThat(all).withFailMessage("Unexpected entries").containsExactlyInAnyOrder("1", "2");
  }

  @Test
  void testGetRootsWithInvalidState() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    assertThrows(
        IllegalStateException.class,
        tree::getRoots,
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
            .map(entity -> entity.getId().orElse(""))
            .collect(Collectors.toList());

    assertThat(all).withFailMessage("Unexpected entries").containsExactlyInAnyOrder("11", "10");
  }

  @Test
  void testAddWithNull() {

    EntityTree tree = new EntityTree();
    tree.add(null);

    assertTrue(tree.getAll().isEmpty(), "tree should be empty");
  }

  @Test
  void testWithoutId() {
    EntityTree tree = new EntityTree();
    assertThrows(IllegalArgumentException.class, () -> tree.add(Entity.builder().build()));
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

    assertEquals("1", entity.getId().orElse(""), "unexpected entity");
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
        tree.getAll().stream()
            .map(entity -> entity.getId().orElse(""))
            .collect(Collectors.toList());

    assertThat(all)
        .withFailMessage("Unexpected entries")
        .containsExactlyInAnyOrder("1", "10", "11", "2", "20");
  }

  @Test
  void testGetAllWithInvalidState() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    assertThrows(
        IllegalStateException.class,
        tree::getAll,
        "Entity 2 is missing. Here an IllegalStateException should be thrown");
  }

  @Test
  void testDeepCopyConstructorWithSingleAssert() {

    Entity originalEntity = Entity.builder().id("1").build();
    EntityTree original = new EntityTree();
    original.children.put("parent", new HashSet<>(Set.of("child1", "child2")));
    original.parents.put("child1", "parent");
    original.index.put("id1", originalEntity);

    EntityTree expected = new EntityTree();
    expected.children.put("parent", new HashSet<>(Set.of("child1", "child2")));
    expected.parents.put("child1", "parent");
    expected.index.put("id1", originalEntity.toBuilder().build());

    EntityTree copy = new EntityTree(original);

    assertEquals(expected, copy, "The deep copy constructor should create an identical tree");
  }

  @Test
  void testToString() {
    EntityTree tree = new EntityTree();
    tree.add(Entity.builder().id("1").build());

    String s = tree.toString();
    assertThat(s).withFailMessage("missing id").contains("id='1',");
  }
}
