package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.Optional;

public class Entity {

	private final Identifier identifier;
	private final String name;
	private final Identifier parent;
	private final EntityVersion version;

	protected Entity(Builder<?> builder) {
		this.identifier = builder.identifier;
		this.name = builder.name;
		this.parent = builder.parent;
		this.version = builder.version;
	}

	public Optional<Identifier> getIdentifier() {
		return Optional.ofNullable(this.identifier);
	}

	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	public Optional<Identifier> getParent() {
		return Optional.ofNullable(this.parent);
	}

	public Optional<EntityVersion> getVersion() {
		return Optional.ofNullable(this.version);
	}

	public static Builder<?> builder() {
		return new EntityBuilder();
	}

	public Builder<?> toBuilder() {
		return new EntityBuilder(this);
	}

	public static abstract class Builder<B extends Builder<B>> {

		private Identifier identifier;
		private String name;
		private Identifier parent;
		private EntityVersion version;

		protected Builder() {
		}

		protected Builder(Entity entity) {
			this.identifier = entity.identifier;
			this.name = entity.name;
			this.parent = entity.parent;
			this.version = entity.version;
		}

		public B identifier(Identifier identifier) {
			assert identifier != null;
			this.identifier = identifier;
			return this.self();
		}

		public B name(String name) {
			assert name != null;
			assert !name.isBlank();
			this.name = name;
			return this.self();
		}

		public B parent(Identifier parent) {
			assert parent != null;
			this.parent = parent;
			return this.self();
		}

		public B version(EntityVersion version) {
			assert version != null;
			this.version = version;
			return this.self();
		}

		protected abstract B self();

		public Entity build() {
			return new Entity(this);
		}
	}

	private static class EntityBuilder extends Builder<EntityBuilder> {
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
