package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RecycleBinItem {

	private final long id;
	private final long parent;
	private final Entity entity;
	private final List<RecycleBinItem> children;

	private RecycleBinItem(Builder builder) {
		this.id = builder.id;
		this.parent = builder.parent;
		this.entity = builder.entity;
		this.children = builder.children;
	}

	public long getId() {
		return this.id;
	}

	public long getParent() {
		return this.parent;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public List<RecycleBinItem> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static final class Builder {

		private long id;
		private long parent;
		private Entity entity;
		private List<RecycleBinItem> children = new ArrayList<>();

		private Builder() {}
		private Builder(RecycleBinItem recycleBinItem) {
			this.id = recycleBinItem.id;
			this.parent = recycleBinItem.parent;
			this.entity = recycleBinItem.entity;
			this.children = new ArrayList<>(recycleBinItem.children);
		}

		public Builder identifier(long id) {
			this.id = id;
			return this;
		}

		public Builder parent(long parent) {
			this.parent = parent;
			return this;
		}

		public Builder entity(Entity entity) {
			assert entity != null;
			this.entity = entity;
			return this;
		}

		public Builder children(List<RecycleBinItem> children) {
			assert children != null;
			this.children = new ArrayList<>(children);
			return this;
		}

		public Builder child(RecycleBinItem child) {
			assert child != null;
			this.children.add(child);
			return this;
		}

		public RecycleBinItem build() {
			return new RecycleBinItem(this);
		}
	}
}