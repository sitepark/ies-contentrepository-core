package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.anchor.Anchor;
import java.util.List;
import java.util.Objects;

public final class ParentAnchorList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<Anchor> parentAnchorList;

  ParentAnchorList(@JsonProperty("parentAnchorList") Anchor... parentAnchorList) {
    Objects.requireNonNull(parentAnchorList, "parentAnchorList is null");
    this.parentAnchorList = List.of(parentAnchorList);
  }

  public List<Anchor> getParentAnchorList() {
    return this.parentAnchorList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.parentAnchorList);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof ParentAnchorList that)
        && Objects.equals(this.parentAnchorList, that.parentAnchorList);
  }

  @Override
  public String toString() {
    return "ParentAnchorList{" + "parentAnchorList=" + parentAnchorList + '}';
  }
}
