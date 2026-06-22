package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class ParentList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<String> parentList;

  protected ParentList(@JsonProperty("parentList") String... parentList) {
    Objects.requireNonNull(parentList, "parentList is null");
    this.parentList = List.of(parentList);
  }

  public List<String> getParentList() {
    return this.parentList;
  }
}
