package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;

public class RootList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<String> rootList;

  protected RootList(@JsonProperty("rootList") String... rootList) {
    Objects.requireNonNull(rootList, "rootList is null");
    this.rootList = List.of(rootList);
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public List<String> getRootList() {
    return this.rootList;
  }
}
