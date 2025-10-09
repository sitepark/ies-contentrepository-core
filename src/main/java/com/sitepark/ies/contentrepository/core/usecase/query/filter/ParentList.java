package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.List;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "parentlist")
public final class ParentList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<String> parentList;

  ParentList(@JsonProperty("parentList") String... parentList) {
    Objects.requireNonNull(parentList, "parentList is null");
    this.parentList = List.of(parentList);
  }

  public List<String> getParentList() {
    return this.parentList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.parentList);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof ParentList that) && Objects.equals(this.parentList, that.parentList);
  }

  @Override
  public String toString() {
    return "ParentList{" + "parentList=" + parentList + '}';
  }
}
