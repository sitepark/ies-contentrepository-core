package com.sitepark.ies.contentrepository.core.domain.entity.query;

public class QueryOptions {

	private final boolean showHidden;

	protected QueryOptions(Builder builder) {
		this.showHidden = builder.showHidden;
	}

	public boolean isShowHidden() {
		return this.showHidden;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private boolean showHidden;

		protected Builder() {
		}

		protected Builder(QueryOptions queryOptions) {
			this.showHidden = queryOptions.showHidden;
		}

		public Builder showHidden(boolean showHidden) {
			this.showHidden = showHidden;
			return this;
		}

		public QueryOptions build() {
			return new QueryOptions(this);
		}
	}
}
