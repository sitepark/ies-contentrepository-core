package com.sitepark.ies.contentrepository.core.domain.entity.query;

import java.util.Objects;

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

	@Override
	public final int hashCode() {
		return Objects.hash(
				super.hashCode(),
				this.first,
				this.after);
	}

	@Override
	public final boolean equals(Object o) {

		if (!(o instanceof CursorBasedQuery)) {
			return false;
		}

		CursorBasedQuery that = (CursorBasedQuery)o;

		return
				that.canEqual(this) &&
				super.equals(o) &&
				Objects.equals(this.first, that.first) &&
				Objects.equals(this.after, that.after);
	}

	/**
	 * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">
	 * 	How to Write an Equality Method in Java
	 * </a>
	 */
	@Override
	public boolean canEqual(Object other) {
		return (other instanceof CursorBasedQuery);
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

		protected Builder() { }

		protected Builder(CursorBasedQuery curserBasedQuery) {
			super(curserBasedQuery);
			this.first = curserBasedQuery.first;
			this.after = curserBasedQuery.after;
		}

		public Builder first(int first) {
			this.first = first;
			return this.self();
		}

		public Builder after(String after) {
			Objects.requireNonNull(after, "after is null");
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
