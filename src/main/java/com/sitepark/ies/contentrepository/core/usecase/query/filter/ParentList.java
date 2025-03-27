package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

  @SuppressFBWarnings("EI_EXPOSE_REP")
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
