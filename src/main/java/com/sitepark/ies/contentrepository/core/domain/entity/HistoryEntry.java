package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;

public final class HistoryEntry implements Serializable {

	private final long entity;
	private final long user;
	private final long timestamp;
	private final HistoryEntryType type;
	private final String comment;

	private static final long serialVersionUID = 1L;

	private HistoryEntry(Builder builder) {
		this.entity = builder.entity;
		this.user = builder.user;
		this.timestamp = builder.timestamp;
		this.type = builder.type;
		this.comment = builder.comment;
	}

	public long getEntity() {
		return entity;
	}

	public long getUser() {
		return user;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public HistoryEntryType getType() {
		return type;
	}

	public String getComment() {
		return comment;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private long entity;
		private long user;
		private long timestamp;
		private HistoryEntryType type;
		private String comment;

		private Builder() { }

		private Builder(HistoryEntry historyEntry) {
			this.entity = historyEntry.entity;
			this.user = historyEntry.user;
			this.timestamp = historyEntry.timestamp;
			this.type = historyEntry.type;
			this.comment = historyEntry.comment;
		}

		public Builder entity(long entity) {
			this.entity = entity;
			return this;
		}

		public Builder user(long user) {
			this.user = user;
			return this;
		}

		public Builder timestamp(long timestamp) {
			assert timestamp > 0;
			this.timestamp = timestamp;
			return this;
		}

		public Builder type(HistoryEntryType type) {
			assert type != null;
			this.type = type;
			return this;
		}

		public Builder comment(String comment) {
			assert comment != null;
			this.comment = comment;
			return this;
		}

		public HistoryEntry build() {
			return new HistoryEntry(this);
		}
	}
}