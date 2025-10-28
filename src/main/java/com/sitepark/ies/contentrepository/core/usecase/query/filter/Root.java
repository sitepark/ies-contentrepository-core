package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "root")
public final class Root implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final String root;

  Root(@JsonProperty("root") String root) {
    Objects.requireNonNull(root, "root is null");
    this.root = root;
  }

  public String getRoot() {
    return this.root;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.root);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Root that) && Objects.equals(this.root, that.root);
  }

  @Override
  public String toString() {
    return "Root{" + "root=" + root + '}';
  }
}
