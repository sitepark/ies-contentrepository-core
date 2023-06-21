package com.sitepark.ies.contentrepository.core.domain.entity;

import java.time.LocalDateTime;
import java.util.Optional;

public final class RecycleBinItemFilter {

	private final LocalDateTime from;
	private final LocalDateTime to;
	private final long user;

	private RecycleBinItemFilter(Builder builder) {
		this.from = builder.from;
		this.to = builder.to;
		this.user = builder.user;
	}

	public Optional<LocalDateTime> getFrom() {
		return Optional.ofNullable(this.from);
	}

	public Optional<LocalDateTime> getTo() {
		return Optional.ofNullable(this.to);
	}

	public Optional<Long> getUser() {
		return Optional.ofNullable(this.user);
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static final class Builder {

		private LocalDateTime from;
		private LocalDateTime to;
		private long user;

		private Builder() {}
		private Builder(RecycleBinItemFilter recycleBinItemFilter) {
			this.from = recycleBinItemFilter.from;
			this.to = recycleBinItemFilter.to;
			this.user = recycleBinItemFilter.user;
		}

		public Builder from(LocalDateTime from) {
			assert this.from != null;
			this.from = from;
			return this;
		}

		public Builder to(LocalDateTime to) {
			assert this.to != null;
			this.to = to;
			return this;
		}

		public Builder user(long user) {
			this.user = user;
			return this;
		}

		public RecycleBinItemFilter build() {
			return new RecycleBinItemFilter(this);
		}
	}
}
