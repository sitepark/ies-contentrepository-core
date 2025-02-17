package com.sitepark.ies.contentrepository.core.domain.entity.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.contentrepository.core.domain.entity.query.filter.Filter;
import com.sitepark.ies.contentrepository.core.domain.entity.query.limit.Limit;
import com.sitepark.ies.contentrepository.core.domain.entity.query.sort.SortCriteria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonDeserialize(builder = Query.Builder.class)
public class Query {

  private final Filter filterBy;

  private final List<SortCriteria> sort;

  private final Limit limit;

  private final QueryOptions options;

  protected Query(Builder<?> builder) {
    this.filterBy = builder.filterBy;
    this.sort = Collections.unmodifiableList(builder.sort);
    this.limit = builder.limit;
    this.options = builder.options;
  }

  public Optional<Filter> getFilterBy() {
    return Optional.ofNullable(this.filterBy);
  }

  public List<SortCriteria> getSort() {
    return this.sort;
  }

  public QueryOptions getOptions() {
    return this.options;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.filterBy, this.sort, this.limit, this.options);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof Query)) {
      return false;
    }

    Query that = (Query) o;

    return that.canEqual(this)
        && Objects.equals(this.filterBy, that.filterBy)
        && Objects.equals(this.limit, that.limit)
        && Objects.equals(this.sort, that.sort)
        && Objects.equals(this.options, that.options);
  }

  /**
   * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java"> How to Write an Equality
   * Method in Java </a>
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

    private final List<SortCriteria> sort = new ArrayList<>();

    private Limit limit;

    protected QueryOptions options;

    protected Builder() {}

    protected Builder(Query query) {
      this.filterBy = query.filterBy;
      this.sort.addAll(query.sort);
      this.limit = query.limit;
      this.options = query.options;
    }

    public B filterBy(Filter filterBy) {
      this.filterBy = filterBy;
      return this.self();
    }

    public B sort(SortCriteria sortCriteria) {
      Objects.requireNonNull(sortCriteria, "sortCriteria is null");
      this.sort.add(sortCriteria);
      return this.self();
    }

    public B sort(SortCriteria[] sortCriterias) {
      Objects.requireNonNull(sortCriterias, "sortCriterias is null");
      for (SortCriteria criteria : sortCriterias) {
        this.sort(criteria);
      }
      return this.self();
    }

    public B sort(Collection<SortCriteria> sortCriterias) {
      Objects.requireNonNull(sortCriterias, "sortCriterias is null");
      for (SortCriteria criteria : sortCriterias) {
        this.sort(criteria);
      }
      return this.self();
    }

    public B limit(Limit limit) {
      Objects.requireNonNull(limit, "limit is null");
      this.limit = limit;
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
