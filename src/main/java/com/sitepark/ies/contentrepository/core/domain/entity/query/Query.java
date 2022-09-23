package com.sitepark.ies.contentrepository.core.domain.entity.query;

import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter;
import com.sitepark.ies.contentrepository.core.domain.entity.sort.OrderBy;

@JsonDeserialize(builder = Query.Builder.class)
public class Query {

	private final Filter filterBy;

	private final OrderBy orderBy;

	protected Query(Builder<?> builder) {
		this.filterBy = builder.filterBy;
		this.orderBy = builder.orderBy;
	}

	public Optional<Filter> getFilterBy() {
		return Optional.ofNullable(this.filterBy);
	}

	public OrderBy getOrderBy() {
		return this.orderBy;
	}

	public static Builder<?> builder() {
		return new QueryBuilder();
	}

	public Builder<?> toBuilder() {
		return new QueryBuilder(this);
	}

	public static abstract class Builder<B extends Builder<B>> {

		protected Filter filterBy;

		protected OrderBy orderBy;

		protected Builder() { }

		protected Builder(Query query) {
			this.filterBy = query.filterBy;
			this.orderBy = query.orderBy;
		}

		public B filterBy(Filter filterBy) {
			this.filterBy = filterBy;
			return this.self();
		}

		public B orderBy(OrderBy orderBy) {
			assert orderBy != null;
			this.orderBy = orderBy;
			return this.self();
		}

		public abstract B self();

		public abstract Query build();
	}

	@JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
	public static class QueryBuilder extends Query.Builder<QueryBuilder> {

		protected QueryBuilder() { }

		protected QueryBuilder(Query query) {
			super(query);
		}

		@Override
		public QueryBuilder self() {
			return this;
		}

		@Override
		public Query build() {
			return new Query(this);
		}
	}
}
