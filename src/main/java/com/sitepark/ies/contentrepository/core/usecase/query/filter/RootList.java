package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.List;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "rootlist")
public final class RootList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<String> rootList;

  RootList(@JsonProperty("rootList") String... rootList) {
    Objects.requireNonNull(rootList, "rootList is null");
    this.rootList = List.of(rootList);
  }

  public List<String> getRootList() {
    return this.rootList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rootList);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof RootList that) && Objects.equals(this.rootList, that.rootList);
  }

  @Override
  public String toString() {
    return "RootList{" + "rootList=" + rootList + '}';
  }
}
