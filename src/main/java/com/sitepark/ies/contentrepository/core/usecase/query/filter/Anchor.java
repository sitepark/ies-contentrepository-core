package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Anchor implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final com.sitepark.ies.sharedkernel.anchor.domain.Anchor anchor;

  protected Anchor(
      @JsonProperty("anchor") com.sitepark.ies.sharedkernel.anchor.domain.Anchor anchor) {
    Objects.requireNonNull(anchor, "anchor is null");
    this.anchor = anchor;
  }

  public com.sitepark.ies.sharedkernel.anchor.domain.Anchor getAnchor() {
    return this.anchor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.anchor);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Anchor that) && Objects.equals(this.anchor, that.anchor);
  }

  @Override
  public String toString() {
    return "Anchor{" + "anchor=" + anchor + '}';
  }
}
