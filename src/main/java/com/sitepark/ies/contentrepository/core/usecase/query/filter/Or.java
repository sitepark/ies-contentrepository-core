package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class Or implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<Filter> or;

  protected Or(@JsonProperty("or") Filter... or) {
    Objects.requireNonNull(or, "or is null");
    this.or = List.of(or);
  }

  public List<Filter> getOr() {
    return this.or;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.or);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Or that) && Objects.equals(this.or, that.or);
  }

  @Override
  public String toString() {
    return "Or{" + "or=" + or + '}';
  }
}
