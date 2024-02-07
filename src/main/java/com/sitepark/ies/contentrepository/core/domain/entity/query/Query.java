package com.sitepark.ies.contentrepository.core.domain.entity.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter;
import com.sitepark.ies.contentrepository.core.domain.entity.sort.OrderBy;
import java.util.Objects;
import java.util.Optional;

@JsonDeserialize(builder = Query.Builder.class)
public class Query {

  private final Filter filterBy;

  private final OrderBy orderBy;

  private final QueryOptions options;

  protected Query(Builder<?> builder) {
    this.filterBy = builder.filterBy;
    this.orderBy = builder.orderBy;
    this.options = builder.options;
  }

  public Optional<Filter> getFilterBy() {
    return Optional.ofNullable(this.filterBy);
  }

  public OrderBy getOrderBy() {
    return this.orderBy;
  }

  public QueryOptions getOptions() {
    return this.options;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.filterBy, this.orderBy, this.options);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof Query)) {
      return false;
    }

    Query that = (Query) o;

    return that.canEqual(this)
        && Objects.equals(this.filterBy, that.filterBy)
        && Objects.equals(this.orderBy, that.orderBy)
        && Objects.equals(this.options, that.options);
  }

  /**
   * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">
   * 	How to Write an Equality Method in Java
   * </a>
   */
  public boolean canEqual(Object other) {
    return (other instanceof Query);
  }

  public static Builder<?> builder() {
    return new QueryBuilder();
  }

  public Builder<?> toBuilder() {
    return new QueryBuilder(this);
  }

  public abstract static class Builder<B extends Builder<B>> {

    protected Filter filterBy;

    protected OrderBy orderBy;

    protected QueryOptions options;

    protected Builder() {}

    protected Builder(Query query) {
      this.filterBy = query.filterBy;
      this.orderBy = query.orderBy;
      this.options = query.options;
    }

    public B filterBy(Filter filterBy) {
      this.filterBy = filterBy;
      return this.self();
    }

    public B orderBy(OrderBy orderBy) {
      Objects.requireNonNull(orderBy, "orderBy is null");
      this.orderBy = orderBy;
      return this.self();
    }

    public B options(QueryOptions options) {
      Objects.requireNonNull(options, "options is null");
      this.options = options;
      return this.self();
    }

    public abstract B self();

    public abstract Query build();
  }

  @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
  public static class QueryBuilder extends Query.Builder<QueryBuilder> {

    protected QueryBuilder() {}

    protected QueryBuilder(Query query) {
      super(query);
    }

    @Override
    public QueryBuilder self() {
      return this;
    }

    @Override
    public Query build() {
      if (this.options == null) {
        this.options = QueryOptions.builder().build();
      }
      return new Query(this);
    }
  }
}
