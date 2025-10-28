package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.anchor.Anchor;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.List;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "anchorlist")
public final class AnchorList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<Anchor> anchorList;

  AnchorList(@JsonProperty("anchorList") Anchor... anchorList) {
    Objects.requireNonNull(anchorList, "anchorList is null");
    this.anchorList = List.of(anchorList);
  }

  public List<Anchor> getAnchorList() {
    return this.anchorList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.anchorList);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof AnchorList that) && Objects.equals(this.anchorList, that.anchorList);
  }

  @Override
  public String toString() {
    return "AnchorList{" + "anchorList=" + anchorList + '}';
  }
}
