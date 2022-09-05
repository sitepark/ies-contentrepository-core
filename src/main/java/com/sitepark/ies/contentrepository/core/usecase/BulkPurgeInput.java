package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.query.Query;

public final class BulkPurgeInput {

	private final Query query;

	private final boolean forceLock;

	private BulkPurgeInput(Builder builder) {
		this.query = builder.query;
		this.forceLock = builder.forceLock;
	}

	public Query getQuery() {
		return this.query;
	}

	public boolean isForceLock() {
		return this.forceLock;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private Query query;

		private boolean forceLock;

		private Builder() { }

		private Builder(BulkPurgeInput bulkPurgeRequest) {
			this.query = bulkPurgeRequest.query;
			this.forceLock = bulkPurgeRequest.forceLock;
		}

		public void query(Query query) {
			assert query != null;
			this.query = query;
		}

		public void forceLock(boolean forceLock) {
			this.forceLock = forceLock;
		}

		public BulkPurgeInput build() {
			return new BulkPurgeInput(this);
		}
	}
}
