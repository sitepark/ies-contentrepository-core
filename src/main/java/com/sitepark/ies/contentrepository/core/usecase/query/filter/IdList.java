package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.List;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "idlist")
public final class IdList implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<String> idList;

  IdList(@JsonProperty("idList") String... idList) {
    Objects.requireNonNull(idList, "idList is null");
    this.idList = List.of(idList);
  }

  public List<String> getIdList() {
    return this.idList;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.idList);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof IdList that) && Objects.equals(this.idList, that.idList);
  }

  @Override
  public String toString() {
    return "IdList{" + "idList=" + idList + '}';
  }
}
