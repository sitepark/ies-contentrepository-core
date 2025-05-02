package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class And implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<Filter> and;

  protected And(@JsonProperty("and") Filter... and) {
    Objects.requireNonNull(and, "and is null");
    this.and = List.of(and);
  }

  public List<Filter> getAnd() {
    return this.and;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.and);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof And that) && Objects.equals(this.and, that.and);
  }

  @Override
  public String toString() {
    return "And{" + "and=" + and + '}';
  }
}
