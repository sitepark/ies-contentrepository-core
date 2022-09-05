package com.sitepark.ies.contentrepository.core.domain.entity.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Query.Builder.class)
public class CursorBasedQuery extends Query {

	private final int first;

	private final String after;

	protected CursorBasedQuery(Builder builder) {
		super(builder);
		this.first = builder.first;
		this.after = builder.after;
	}

	public int getFirst() {
		return this.first;
	}

	public String getAfter() {
		return this.after;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public Builder toBuilder() {
		return new Builder(this);
	}

	@JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
	public static class Builder extends Query.Builder<Builder> {

		private int first;

		private String after;

		private Builder() { }

		private Builder(CursorBasedQuery curserBasedQuery) {
			super(curserBasedQuery);
			this.first = curserBasedQuery.first;
			this.after = curserBasedQuery.after;
		}

		public Builder first(int first) {
			this.first = first;
			return this.self();
		}

		public Builder after(String after) {
			assert after != null;
			this.after = after;
			return this.self();
		}

		@Override
		public CursorBasedQuery build() {
			return new CursorBasedQuery(this);
		}

		@Override
		public Builder self() {
			return this;
		}
	}
}
