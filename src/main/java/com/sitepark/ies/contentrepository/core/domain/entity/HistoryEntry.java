package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;
import java.util.Optional;

public final class HistoryEntry implements Serializable {

	private final long entity;
	private final String initiator;
	private final Long user;
	private final long timestamp;
	private final HistoryEntryType type;
	private final String comment;

	private static final long serialVersionUID = 1L;

	private HistoryEntry(Builder builder) {
		this.entity = builder.entity;
		this.initiator = builder.initiator;
		this.user = builder.user;
		this.timestamp = builder.timestamp;
		this.type = builder.type;
		this.comment = builder.comment;
	}

	public long getEntity() {
		return entity;
	}

	/**
	 * The initiator can be a user of a system-service or other subjects who
	 * could authenticate themselves to work with the system.
	 */
	public String getInitiator() {
		return initiator;
	}

	/**
	 * If the initiator is a user of the IES, this returns the ID of the user.
	 */
	public Optional<Long> getUser() {
		return Optional.ofNullable(this.user);
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

	public static final class Builder {

		private long entity;
		private String initiator;
		private Long user;
		private long timestamp;
		private HistoryEntryType type;
		private String comment;

		private Builder() { }

		private Builder(HistoryEntry historyEntry) {
			this.entity = historyEntry.entity;
			this.initiator = historyEntry.initiator;
			this.user = historyEntry.user;
			this.timestamp = historyEntry.timestamp;
			this.type = historyEntry.type;
			this.comment = historyEntry.comment;
		}

		public Builder entity(long entity) {
			this.entity = entity;
			return this;
		}

		public Builder initiator(String initiator) {
			assert initiator != null : "initiator is null";
			this.initiator = initiator;
			return this;
		}

		public Builder user(Long user) {
			this.user = user;
			return this;
		}

		public Builder timestamp(long timestamp) {
			assert timestamp > 0;
			this.timestamp = timestamp;
			return this;
		}

		public Builder type(HistoryEntryType type) {
			assert type != null : "type is null";
			this.type = type;
			return this;
		}

		public Builder comment(String comment) {
			assert comment != null : "comment is null";
			this.comment = comment;
			return this;
		}

		public HistoryEntry build() {
			return new HistoryEntry(this);
		}
	}
}