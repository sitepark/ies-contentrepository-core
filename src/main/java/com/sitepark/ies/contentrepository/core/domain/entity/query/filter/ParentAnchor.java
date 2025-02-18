package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ParentAnchor implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final com.sitepark.ies.contentrepository.core.domain.entity.Anchor parentAnchor;

  protected ParentAnchor(
      @JsonProperty("parentAnchor")
          com.sitepark.ies.contentrepository.core.domain.entity.Anchor parentAnchor) {
    Objects.requireNonNull(parentAnchor, "parentAnchor is null");
    this.parentAnchor = parentAnchor;
  }

  public com.sitepark.ies.contentrepository.core.domain.entity.Anchor getParentAnchor() {
    return this.parentAnchor;
  }
}
