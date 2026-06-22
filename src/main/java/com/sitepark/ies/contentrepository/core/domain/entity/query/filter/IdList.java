package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class IdList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<String> idList;

  protected IdList(@JsonProperty("idList") String... idList) {
    Objects.requireNonNull(idList, "idList is null");
    this.idList = List.of(idList);
  }

  public List<String> getIdList() {
    return this.idList;
  }
}
