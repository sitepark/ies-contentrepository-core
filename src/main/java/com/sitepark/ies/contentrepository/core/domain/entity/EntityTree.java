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

	private final Map<String, Set<String>> children = new HashMap<>();

	private final Map<String, String> parents = new HashMap<>();

	private final Map<String, Entity> index = new HashMap<>();

	/**
	 * Returns all roots of the tree.
	 *
	 * Roots are all entries without a parent
	 */
	public List<Entity> getRoots() {
		return this.getRootIdList().stream()
				.map(id -> {
					Entity entity = this.index.get(id);
					if (entity == null) {
						throw new IllegalStateException("Entity with id " + id + " missing");
					}
					return entity;
				})
				.collect(Collectors.toList());
	}

	private Set<String> getRootIdList() {
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
	 * If no parent present, it is set as root element.
	 */
	public void add(Entity entity) {

		if (entity == null) {
			return;
		}

		this.parents.put(entity.getId().get(), entity.getParent().orElse(null));
		Set<String> siblings = this.children.get(entity.getParent().orElse(null));
		if (siblings == null) {
			siblings = new HashSet<>();
			this.children.put(entity.getParent().orElse(null), siblings);
		}
		siblings.add(entity.getId().get());
		this.index.put(entity.getId().get(), entity);
	}

	public Entity get(String id) {
		return this.index.get(id);
	}

	public List<Entity> getChildren(String parent) {
		Set<String> children = this.children.get(parent);
		return children.stream()
			.map(id -> this.index.get(id))
			.collect(Collectors.toList());
	}

	/**
	 * Liefert die Unterelemente des Parent rekusive.
	 */
	public List<Entity> getChildrenRecursive(String root) {
		Set<String> children = this.getChildrenIdListRecursive(root);
		return children.stream()
				.map(id -> this.index.get(id))
				.collect(Collectors.toList());
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

	/**
	 * Returns all elements of the tree in hierarchical order
	 */
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

	private void toString(int indent, String parent, java.lang.StringBuilder indentPrefix,
			java.lang.StringBuilder b) {

		if (parent == null) {
			Set<String> roots = this.getRootIdList();
			for (String child : roots) {
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
				for (String child : this.children.get(parent)) {
					this.toString(indent, child, indentPrefix, b);
				}
				indentPrefix.delete(indentPrefix.length() - indent, indentPrefix.length());
			}
		}
	}
}
