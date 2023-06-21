package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Entity.EntityBuilder.class)
public class Entity {

	private final long id;
	private final Anchor anchor;
	private final String name;
	private final long parent;
	private final long version;
	private final boolean isGroup;

	protected Entity(Builder<?> builder) {
		this.id = builder.id;
		this.anchor = builder.anchor;
		this.name = builder.name;
		this.parent = builder.parent;
		this.version = builder.version;
		this.isGroup = builder.isGroup;
	}

	public Optional<Long> getId() {
		if (this.id == 0) {
			return Optional.empty();
		} else {
			return Optional.of(this.id);
		}
	}

	public Optional<Anchor> getAnchor() {
		return Optional.ofNullable(this.anchor);
	}

	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	public Optional<Long> getParent() {
		if (this.parent == 0) {
			return Optional.empty();
		} else {
			return Optional.of(this.parent);
		}
	}

	public Optional<Long> getVersion() {
		if (this.version == 0) {
			return Optional.empty();
		} else {
			return Optional.of(this.version);
		}
	}

	public boolean isGroup() {
		return this.isGroup;
	}

	public static Builder<?> builder() {
		return new EntityBuilder();
	}

	public Builder<?> toBuilder() {
		return new EntityBuilder(this);
	}

	@Override
	public final int hashCode() {

		int hash = Long.hashCode(this.id);
		hash = (this.anchor != null) ? 31 * hash + this.anchor.hashCode() : hash;
		hash = (this.name != null) ? 31 * hash + this.name.hashCode() : hash;
		hash = 31 * hash + Long.hashCode(this.version);
		hash = 31 * hash + Long.hashCode(this.parent);
		hash = 31 * hash + Boolean.hashCode(this.isGroup);

		return hash;
	}

	@Override
	public final boolean equals(Object o) {

		if (!(o instanceof Entity)) {
			return false;
		}

		Entity entity = (Entity)o;

		if (!Objects.equals(this.id, entity.id)) {
			return false;
		} else if (!Objects.equals(this.anchor, entity.anchor)) {
			return false;
		} else if (this.isGroup != entity.isGroup) {
			return false;
		} else if (!Objects.equals(this.version, entity.version)) {
			return false;
		} else if (!Objects.equals(this.name, entity.name)) {
			return false;
		} else if (!Objects.equals(this.parent, entity.parent)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(this.getName().orElse("UNNAMED"))
				.append(" (")
				.append("id: ").append(this.id)
				.append(", ")
				.append("anchor: ").append(this.anchor)
				.append(", ")
				.append("parent: ").append(this.parent)
				.append(", ")
				.append("version: ").append(this.version)
				.append(", ")
				.append("isGroup: ").append(this.isGroup)
				.append(')').toString();
	}

	public static abstract class Builder<B extends Builder<B>> {

		private long id;
		private Anchor anchor;
		private String name;
		private long parent;
		private long version;
		private boolean isGroup;

		protected Builder() {
		}

		protected Builder(Entity entity) {
			this.id = entity.id;
			this.anchor = entity.anchor;
			this.name = entity.name;
			this.parent = entity.parent;
			this.version = entity.version;
			this.isGroup = entity.isGroup;
		}

		public B id(long id) {
			this.id = id;
			return this.self();
		}

		public B anchor(String anchor) {
			assert anchor != null;
			this.anchor = Anchor.ofString(anchor).get();
			return this.self();
		}

		public B anchor(Anchor anchor) {
			assert anchor != null;
			this.anchor = anchor;
			return this.self();
		}

		public B name(String name) {
			assert name != null;
			assert !name.isBlank();
			this.name = name;
			return this.self();
		}

		public B parent(long parent) {
			this.parent = parent;
			return this.self();
		}

		public B version(long version) {
			this.version = version;
			return this.self();
		}

		public B isGroup(boolean isGroup) {
			this.isGroup = isGroup;
			return this.self();
		}

		protected abstract B self();

		public Entity build() {
			return new Entity(this);
		}
	}

	@JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
	protected static class EntityBuilder extends Builder<EntityBuilder> {
		protected EntityBuilder() {}

		protected EntityBuilder(Entity entity) {
			super(entity);
		}

		@Override
		protected EntityBuilder self() {
			return this;
		}
	}
}
