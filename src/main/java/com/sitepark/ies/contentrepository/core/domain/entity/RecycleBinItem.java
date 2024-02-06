package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class RecycleBinItem {

	private final String id;
	private final String parent;
	private final Entity entity;
	private final List<RecycleBinItem> children;

	private RecycleBinItem(Builder builder) {
		this.id = builder.id;
		this.parent = builder.parent;
		this.entity = builder.entity;
		this.children = builder.children;
	}

	public String getId() {
		return this.id;
	}

	public String getParent() {
		return this.parent;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public List<RecycleBinItem> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(
				this.id,
				this.parent,
				this.entity,
				this.children);
	}

	@Override
	public final boolean equals(Object o) {

		if (!(o instanceof RecycleBinItem)) {
			return false;
		}

		RecycleBinItem item = (RecycleBinItem)o;

		return Objects.equals(this.id, item.id) &&
				Objects.equals(this.parent, item.parent) &&
				Objects.equals(this.entity, item.entity) &&
				Objects.equals(this.children, item.children);
	}

	@Override
	public String toString() {
		return "RecycleBinItem [id=" + id + ", parent=" + parent + ", entity=" + entity + ", children=" + children
				+ "]";
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static final class Builder {

		private String id;
		private String parent;
		private Entity entity;
		private List<RecycleBinItem> children = new ArrayList<>();

		private Builder() {}
		private Builder(RecycleBinItem recycleBinItem) {
			this.id = recycleBinItem.id;
			this.parent = recycleBinItem.parent;
			this.entity = recycleBinItem.entity;
			this.children = new ArrayList<>(recycleBinItem.children);
		}

		public Builder id(String id) {
			Objects.requireNonNull(id, "id is null");
			if (!Identifier.isId(id)) {
				throw new IllegalArgumentException(id + " is not an id");
			}
			this.id = id;
			return this;
		}

		public Builder parent(String parent) {
			Objects.requireNonNull(parent, "parent is null");
			if (!Identifier.isId(parent)) {
				throw new IllegalArgumentException(parent + " is not an id");
			}
			this.parent = parent;
			return this;
		}

		public Builder entity(Entity entity) {
			Objects.requireNonNull(entity, "entity is null");
			this.entity = entity;
			return this;
		}

		public Builder children(List<RecycleBinItem> children) {
			Objects.requireNonNull(children, "children is null");
			this.children = new ArrayList<>(children);
			return this;
		}

		public Builder child(RecycleBinItem child) {
			Objects.requireNonNull(child, "child is null");
			this.children.add(child);
			return this;
		}

		public RecycleBinItem build() {
			return new RecycleBinItem(this);
		}
	}
}