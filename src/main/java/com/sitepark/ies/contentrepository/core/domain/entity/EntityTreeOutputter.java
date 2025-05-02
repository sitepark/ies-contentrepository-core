package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.Set;

public class EntityTreeOutputter {

  final EntityTree tree;

  public EntityTreeOutputter(EntityTree tree) {
    this.tree = new EntityTree(tree);
  }

  @Override
  public String toString() {
    return this.toString(0);
  }

  public String toString(int indent) {
    return this.toString(indent, null);
  }

  public String toString(int indent, String parent) {
    java.lang.StringBuilder b = new java.lang.StringBuilder();
    this.toString(indent, parent, new java.lang.StringBuilder(), b);
    return b.toString();
  }

  private void toString(
      int indent, String parent, java.lang.StringBuilder indentPrefix, java.lang.StringBuilder b) {

    if (parent == null) {
      Set<String> roots = this.tree.getRootIdList();
      for (String child : roots) {
        this.toString(indent, child, indentPrefix, b);
      }
    } else {
      b.append(indentPrefix.toString()).append(this.tree.index.get(parent)).append('\n');

      if (this.tree.hasChildren(parent)) {
        indentPrefix.append(" ".repeat(Math.max(0, indent)));
        for (String child : this.tree.children.get(parent)) {
          this.toString(indent, child, indentPrefix, b);
        }
        indentPrefix.delete(indentPrefix.length() - indent, indentPrefix.length());
      }
    }
  }
}
