package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EntityTreeOutputterTest {

  @Test
  void testToStringWithIndent() {
    EntityTree tree = new EntityTree();

    tree.add(Entity.builder().id("1").build());
    tree.add(Entity.builder().id("2").build());
    tree.add(Entity.builder().id("10").parent("1").build());
    tree.add(Entity.builder().id("11").parent("1").build());
    tree.add(Entity.builder().id("20").parent("2").build());

    EntityTreeOutputter outputter = new EntityTreeOutputter(tree);
    String s = outputter.toString(3);

    assertTrue(s.contains("\n   Entity"), "indent missing");
  }
}
