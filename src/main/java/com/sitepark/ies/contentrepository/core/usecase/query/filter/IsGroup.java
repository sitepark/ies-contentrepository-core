package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sitepark.ies.sharedkernel.json.UniquePropertyType;
import java.util.Objects;

@UniquePropertyType(uniqueProperty = "isgroup")
public final class IsGroup implements Filter {

  private final boolean group;

  IsGroup(@JsonProperty("isGroup") boolean isGroup) {
    this.group = isGroup;
  }

  public boolean isGroup() {
    return this.group;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.group);
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof IsGroup that) && Objects.equals(this.group, that.group);
  }

  @Override
  public String toString() {
    return "IsGroup{group=" + group + '}';
  }
}
