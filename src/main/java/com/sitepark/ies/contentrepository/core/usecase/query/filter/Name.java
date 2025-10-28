package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "name")
public final class Name implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final String name;

  Name(@JsonProperty("name") String name) {
    Objects.requireNonNull(name, "name is null");
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Name that) && Objects.equals(this.name, that.name);
  }

  @Override
  public String toString() {
    return "Name{" + "name='" + name + '\'' + '}';
  }
}
