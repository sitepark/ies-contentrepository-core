package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;

public class ParentAnchorList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<com.sitepark.ies.contentrepository.core.domain.entity.Anchor> parentAnchorList;

  protected ParentAnchorList(
      @JsonProperty("parentAnchorList")
          com.sitepark.ies.contentrepository.core.domain.entity.Anchor... parentAnchorList) {
    Objects.requireNonNull(parentAnchorList, "parentAnchorList is null");
    this.parentAnchorList = List.of(parentAnchorList);
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public List<com.sitepark.ies.contentrepository.core.domain.entity.Anchor> getParentAnchorList() {
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
