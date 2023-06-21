package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@SuppressWarnings("PMD.UseConcurrentHashMap")
public class EntityTree {

	private final Map<Long, Set<Long>> children = new HashMap<>();

	private final Map<Long, Long> parents = new HashMap<>();

	private final Map<Long, Entity> index = new HashMap<>();

	/**
	 * Returns all roots of the tree.
	 *
	 * Roots are all entries without a parent
	 */
	public List<Entity> getRoots() {
		return this.getRootIdList().stream()
				.map(id -> this.index.get(id))
				.collect(Collectors.toList());
	}

	private Set<Long> getRootIdList() {
		Set<Long> knownRoots = this.children.get(null);
		Set<Long> roots = new HashSet<>();
		if (knownRoots != null) {
			roots.addAll(knownRoots);
		}
		for (Long parent : this.parents.values()) {
			if (parent != null && !this.parents.containsKey(parent)) {
				roots.add(parent);
			}
		}
		return roots;
	}

	/**
	 * Places an element in the tree.
	 *
	 * If no parent present, it is set as root element.
	 */
	public void add(Entity entity) {

		if (entity == null) {
			return;
		}

		this.parents.put(entity.getId().get(), entity.getParent().orElse(null));
		Set<Long> siblings = this.children.get(entity.getParent().orElse(null));
		if (siblings == null) {
			siblings = new HashSet<>();
			this.children.put(entity.getParent().orElse(null), siblings);
		}
		siblings.add(entity.getId().get());
		this.index.put(entity.getId().get(), entity);
	}

	public Entity get(Long id) {
		return this.index.get(id);
	}

	public List<Entity> getChildren(Long parent) {
		Set<Long> children = this.children.get(parent);
		return children.stream()
			.map(id -> this.index.get(id))
			.collect(Collectors.toList());
	}

	/**
	 * Liefert die Unterelemente des Parent rekusive.
	 */
	public List<Entity> getChildrenRecursive(Long root) {
		Set<Long> children = this.getChildrenIdListRecursive(root);
		return children.stream()
				.map(id -> this.index.get(id))
				.collect(Collectors.toList());
	}

	private Set<Long> getChildrenIdListRecursive(Long root) {
		Set<Long> list = new LinkedHashSet<Long>();
		Set<Long> children = this.children.get(root);
		if (children != null) {
			list.addAll(children);
			for (Long id : children) {
				Set<Long> grandChildren = this.getChildrenIdListRecursive(id);
				list.addAll(grandChildren);
			}
		}

		return list;
	}

	/**
	 * Returns all elements of the tree in hierarchical order
	 */
	public List<Entity> getAll() {
		Set<Long> rootIdList = this.getRootIdList();
		List<Entity> list = new ArrayList<>();
		for (Long rootId : rootIdList) {
			Entity root = this.index.get(rootId);
			if (root != null) {
				list.add(root);
			}
			List<Entity> childrenOrRoot = this.getChildrenRecursive(rootId);
			list.addAll(childrenOrRoot);
		}
		return list;
	}

	public boolean hasChildren(Long parent) {
		return this.children.containsKey(parent);
	}

	@Override
	public String toString() {
		return this.toString(0);
	}

	public String toString(int indent) {
		return this.toString(indent, null);
	}

	public String toString(int indent, Long parent) {
		java.lang.StringBuilder b = new java.lang.StringBuilder();
		this.toString(indent, parent, new java.lang.StringBuilder(), b);
		return b.toString();
	}

	private void toString(int indent, Long parent, java.lang.StringBuilder indentPrefix,
			java.lang.StringBuilder b) {

		if (parent == null) {
			Set<Long> roots = this.getRootIdList();
			for (Long child : roots) {
				this.toString(indent, child, indentPrefix, b);
			}
		} else {
			b.append(indentPrefix.toString())
				.append(this.index.get(parent))
				.append('\n');

			if (this.hasChildren(parent)) {
				for (int i = 0; i < indent; i++) {
					indentPrefix.append(' ');
				}
				for (Long child : this.children.get(parent)) {
					this.toString(indent, child, indentPrefix, b);
				}
				indentPrefix.delete(indentPrefix.length() - indent, indentPrefix.length());
			}
		}
	}
}
