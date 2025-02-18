package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IsGroup implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final boolean isGroup;

  protected IsGroup(@JsonProperty("isGroup") boolean isGroup) {
    this.isGroup = isGroup;
  }

  public boolean isGroup() {
    return this.isGroup;
  }
}
