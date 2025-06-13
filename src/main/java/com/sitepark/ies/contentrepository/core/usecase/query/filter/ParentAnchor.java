package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.anchor.Anchor;
import java.util.Objects;

public final class ParentAnchor implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final Anchor parentAnchor;

  ParentAnchor(@JsonProperty("parentAnchor") Anchor parentAnchor) {
    Objects.requireNonNull(parentAnchor, "parentAnchor is null");
    this.parentAnchor = parentAnchor;
  }

  public Anchor getParentAnchor() {
    return this.parentAnchor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.parentAnchor);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof ParentAnchor that) && Objects.equals(this.parentAnchor, that.parentAnchor);
  }

  @Override
  public String toString() {
    return "ParentAnchor{" + "parentAnchor=" + parentAnchor + '}';
  }
}
