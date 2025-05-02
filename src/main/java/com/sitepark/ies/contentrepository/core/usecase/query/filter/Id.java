package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Id implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final String id;

  protected Id(@JsonProperty("id") String id) {
    Objects.requireNonNull(id, "id is null");
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Id that) && Objects.equals(this.id, that.id);
  }

  @Override
  public String toString() {
    return "Id{" + "id=" + id + '}';
  }
}
