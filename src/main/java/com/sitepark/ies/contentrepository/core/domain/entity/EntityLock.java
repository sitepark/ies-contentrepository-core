package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;

public final class EntityLock implements Serializable {

	private final Identifier entity;
	private final Identifier user;
	private final long created;
	private final long lastAccess;
	private final long ttl;

	private static final long serialVersionUID = 1L;

	private EntityLock(Builder builder) {
		this.entity = builder.entity;
		this.user = builder.user;
		this.created = builder.created;
		this.lastAccess = builder.lastAccess;
		this.ttl = builder.ttl;
	}

	public Identifier getEntity() {
		return entity;
	}

	public Identifier getUser() {
		return user;
	}

	public long getCreated() {
		return created;
	}

	public long getLastAccess() {
		return lastAccess;
	}

	public long getTtl() {
		return ttl;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	@Override
	public String toString() {
		return this.entity + "(" + this.user + " at " + this.created + ")";
	}

	public static class Builder {

		private Identifier entity;
		private Identifier user;
		private long created;
		private long lastAccess;
		private long ttl;

		private Builder() {
		}

		private Builder(EntityLock entityLock) {
			this.entity = entityLock.entity;
			this.user = entityLock.user;
			this.created = entityLock.created;
			this.lastAccess = entityLock.lastAccess;
			this.ttl = entityLock.ttl;
		}

		public Builder entity(Identifier entity) {
			assert entity != null;
			this.entity = entity;
			return this;
		}

		public Builder user(Identifier user) {
			assert user != null;
			this.user = user;
			return this;
		}

		public Builder created(long created) {
			assert created > 0;
			this.created = created;
			return this;
		}

		public Builder lastAccess(long lastAccess) {
			assert lastAccess > 0;
			this.lastAccess = lastAccess;
			return this;
		}

		public Builder ttl(long ttl) {
			assert ttl > 0;
			this.ttl = ttl;
			return this;
		}

		public EntityLock build() {
			return new EntityLock(this);
		}
	}
}
