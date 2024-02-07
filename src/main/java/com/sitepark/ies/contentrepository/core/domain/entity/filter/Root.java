package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Root implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final String root;

  protected Root(@JsonProperty("root") String root) {
    Objects.requireNonNull(root, "root is null");
    this.root = root;
  }

  public String getRoot() {
    return this.root;
  }
}
