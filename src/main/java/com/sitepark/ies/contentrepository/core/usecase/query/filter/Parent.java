package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public final class Parent implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final String parent;

  Parent(@JsonProperty("parent") String parent) {
    Objects.requireNonNull(parent, "parent is null");
    this.parent = parent;
  }

  public String getParent() {
    return this.parent;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.parent);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Parent that) && Objects.equals(this.parent, that.parent);
  }

  @Override
  public String toString() {
    return "Parent{" + "parent=" + parent + '}';
  }
}
