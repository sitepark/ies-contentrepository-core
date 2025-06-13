package com.sitepark.ies.contentrepository.core.domain.value.permission;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Objects;

@JsonDeserialize(builder = ContainedGroupsPermission.Builder.class)
public final class ContainedGroupsPermission {

  private final boolean create;
  private final boolean delete;

  private ContainedGroupsPermission(Builder builder) {
    this.create = builder.create;
    this.delete = builder.delete;
  }

  public boolean isCreate() {
    return create;
  }

  public boolean isDelete() {
    return delete;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ContainedGroupsPermission that)) {
      return false;
    }
    return create == that.create && delete == that.delete;
  }

  @Override
  public int hashCode() {
    return Objects.hash(create, delete);
  }

  @Override
  public String toString() {
    return "ContainedGroupsPermission{" + "create=" + create + ", delete=" + delete + '}';
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private boolean create;
    private boolean delete;

    private Builder() {}

    private Builder(ContainedGroupsPermission permission) {
      this.create = permission.create;
      this.delete = permission.delete;
    }

    @JsonSetter
    public Builder create(boolean create) {
      this.create = create;
      return this;
    }

    @JsonSetter
    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }

    public Builder merge(ContainedGroupsPermission permission) {
      this.create = this.create || permission.create;
      this.delete = this.delete || permission.delete;
      return this;
    }

    public ContainedGroupsPermission build() {
      return new ContainedGroupsPermission(this);
    }
  }
}
