package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class IsGroup implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final boolean group;

  protected IsGroup(@JsonProperty("isGroup") boolean isGroup) {
    this.group = isGroup;
  }

  public boolean isGroup() {
    return this.group;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.group);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof IsGroup that) && Objects.equals(this.group, that.group);
  }

  @Override
  public String toString() {
    return "IsGroup{" + "isGroup=" + group + '}';
  }
}
