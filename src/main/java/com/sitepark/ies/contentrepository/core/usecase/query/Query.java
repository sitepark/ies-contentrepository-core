package com.sitepark.ies.contentrepository.core.usecase.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import com.sitepark.ies.contentrepository.core.usecase.query.limit.Limit;
import com.sitepark.ies.contentrepository.core.usecase.query.sort.SortCriteria;
import java.util.*;

@JsonDeserialize(builder = Query.Builder.class)
public final class Query {

  private final Filter filter;

  private final List<SortCriteria> sort;

  private final Limit limit;

  private final QueryOptions options;

  private Query(Builder builder) {
    this.filter = builder.filter;
    this.sort = List.copyOf(builder.sort);
    this.limit = builder.limit;
    this.options = builder.options;
  }

  public Optional<Filter> getFilter() {
    return Optional.ofNullable(this.filter);
  }

  public List<SortCriteria> getSort() {
    return this.sort;
  }

  public Optional<Limit> getLimit() {
    return Optional.ofNullable(this.limit);
  }

  public QueryOptions getOptions() {
    return this.options;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.filter, this.sort, this.limit, this.options);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Query that)
        && that.canEqual(this)
        && Objects.equals(this.filter, that.filter)
        && Objects.equals(this.limit, that.limit)
        && Objects.equals(this.sort, that.sort)
        && Objects.equals(this.options, that.options);
  }

  /**
   * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">How to
   *     Write an Equality Method in Java </a>
   */
  public boolean canEqual(Object other) {
    return (other instanceof Query);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    protected Filter filter;

    private final List<SortCriteria> sort = new ArrayList<>();

    private Limit limit;

    protected QueryOptions options;

    protected Builder() {}

    protected Builder(Query query) {
      this.filter = query.filter;
      this.sort.addAll(query.sort);
      this.limit = query.limit;
      this.options = query.options;
    }

    public Builder filter(Filter filter) {
      this.filter = filter;
      return this;
    }

    public Builder sort(SortCriteria... sortCriteria) {
      Objects.requireNonNull(sortCriteria, "sortCriteria is null");
      this.sort.addAll(Arrays.asList(sortCriteria));
      for (SortCriteria sortCriterion : sortCriteria) {
        Objects.requireNonNull(sortCriterion, "sortCriterion contains null");
      }
      return this;
    }

    public Builder sort(Collection<SortCriteria> sortCriteria) {
      Objects.requireNonNull(sortCriteria, "sortCriteria is null");
      for (SortCriteria sortCriterion : sortCriteria) {
        this.sort(sortCriterion);
      }
      return this;
    }

    public Builder limit(Limit limit) {
      Objects.requireNonNull(limit, "limit is null");
      this.limit = limit;
      return this;
    }

    public Builder options(QueryOptions options) {
      Objects.requireNonNull(options, "options is null");
      this.options = options;
      return this;
    }

    public Query build() {
      if (this.options == null) {
        this.options = QueryOptions.builder().build();
      }
      return new Query(this);
    }
  }
}
