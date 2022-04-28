package com.sitepark.ies.contentrepository.core.domain.entity;

public final class EntityVersion {

	private final long timestamp;

	private EntityVersion(Builder builder) {
		this.timestamp = builder.timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private long timestamp;

		private Builder() {}

		private Builder(EntityVersion entityVersion) {
			this.timestamp = entityVersion.timestamp;
		}

		public Builder timestamp(long timestamp) {
			assert timestamp > 0;
			this.timestamp = timestamp;
			return this;
		}

		public EntityVersion build() {
			return new EntityVersion(this);
		}
	}
}
