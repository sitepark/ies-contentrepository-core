package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("PMD.UseConcurrentHashMap")
public final class EntityTree {

  final Map<@Nullable String, Set<String>> children = new HashMap<>();

  final Map<String, @Nullable String> parents = new HashMap<>();

  final Map<String, Entity> index = new HashMap<>();

  public EntityTree() {}

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public EntityTree(EntityTree tree) {
    for (Map.Entry<@Nullable String, Set<String>> entry : tree.children.entrySet()) {
      this.children.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }
    this.parents.putAll(tree.parents);

    for (Map.Entry<String, Entity> entry : tree.index.entrySet()) {
      this.index.put(entry.getKey(), entry.getValue().toBuilder().build());
    }
  }

  /**
   * Returns all roots of the tree.
   *
   * <p>Roots are all entries without a parent
   */
  public List<Entity> getRoots() {
    return this.getRootIdList().stream()
        .map(
            id -> {
              Entity entity = this.index.get(id);
              if (entity == null) {
                throw new IllegalStateException("Entity with id " + id + " missing");
              }
              return entity;
            })
        .collect(Collectors.toList());
  }

  Set<String> getRootIdList() {
    Set<String> knownRoots = this.children.get(null);
    Set<String> roots = new HashSet<>();
    if (knownRoots != null) {
      roots.addAll(knownRoots);
    }
    for (String parent : this.parents.values()) {
      if (parent != null && !this.parents.containsKey(parent)) {
        roots.add(parent);
      }
    }
    return roots;
  }

  /**
   * Places an element in the tree.
   *
   * <p>If no parent present, it is set as root element.
   */
  public void add(Entity entity) {

    if (entity == null) {
      return;
    }

    if (entity.getId().isEmpty()) {
      throw new IllegalArgumentException("Entity must have an id");
    }

    this.parents.put(entity.getId().get(), entity.getParent().orElse(null));
    Set<String> siblings =
        this.children.computeIfAbsent(entity.getParent().orElse(null), k -> new HashSet<>());
    siblings.add(entity.getId().get());
    this.index.put(entity.getId().get(), entity);
  }

  public @Nullable Entity get(String id) {
    return this.index.get(id);
  }

  public List<Entity> getChildren(String parent) {
    Set<String> children =
        Objects.requireNonNull(this.children.get(parent), "no children for parent " + parent);
    return children.stream().map(this.index::get).collect(Collectors.toList());
  }

  /** Returns the sub-elements of the parent recursive. */
  public List<Entity> getChildrenRecursive(String root) {
    Set<String> children = this.getChildrenIdListRecursive(root);
    return children.stream().map(this.index::get).collect(Collectors.toList());
  }

  private Set<String> getChildrenIdListRecursive(String root) {
    Set<String> list = new LinkedHashSet<>();
    Set<String> children = this.children.get(root);
    if (children != null) {
      list.addAll(children);
      for (String id : children) {
        Set<String> grandChildren = this.getChildrenIdListRecursive(id);
        list.addAll(grandChildren);
      }
    }

    return list;
  }

  /** Returns all elements of the tree in hierarchical order */
  public List<Entity> getAll() {
    Set<String> rootIdList = this.getRootIdList();
    List<Entity> list = new ArrayList<>();
    for (String rootId : rootIdList) {
      Entity root = this.index.get(rootId);
      if (root == null) {
        throw new IllegalStateException("Entity with id " + rootId + " missing");
      }
      list.add(root);
      List<Entity> childrenOrRoot = this.getChildrenRecursive(rootId);
      list.addAll(childrenOrRoot);
    }
    return list;
  }

  public boolean hasChildren(String parent) {
    return this.children.containsKey(parent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.children, this.parents, this.index);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof EntityTree that)) {
      return false;
    }
    return Objects.equals(this.children, that.children)
        && Objects.equals(this.parents, that.parents)
        && Objects.equals(this.index, that.index);
  }

  @Override
  public String toString() {
    return new EntityTreeOutputter(this).toString();
  }
}
