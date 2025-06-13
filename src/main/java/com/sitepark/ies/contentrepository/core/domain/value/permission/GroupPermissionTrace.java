package com.sitepark.ies.contentrepository.core.domain.value.permission;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = GroupPermissionTrace.Builder.class)
public final class GroupPermissionTrace {

  private final GroupPermission resolvedPermission;
  private final List<GroupPermissionSource> sourcePermissions;

  private GroupPermissionTrace(Builder builder) {
    this.resolvedPermission = builder.resolvedPermission;
    this.sourcePermissions = List.copyOf(builder.sourcePermissions);
  }

  public GroupPermission getResolvedPermission() {
    return resolvedPermission;
  }

  public List<GroupPermissionSource> getSourcePermissions() {
    return sourcePermissions;
  }

  @Override
  public int hashCode() {
    return Objects.hash(resolvedPermission, sourcePermissions);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GroupPermissionTrace that)) {
      return false;
    }
    return Objects.equals(resolvedPermission, that.resolvedPermission)
        && Objects.equals(sourcePermissions, that.sourcePermissions);
  }

  @Override
  public String toString() {
    return "GroupPermissionTrace{"
        + "resolvedPermission="
        + resolvedPermission
        + ", sourcePermissions="
        + sourcePermissions
        + '}';
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
    private GroupPermission resolvedPermission;
    private final List<GroupPermissionSource> sourcePermissions = new ArrayList<>();

    public Builder() {}

    public Builder(GroupPermissionTrace trace) {
      this.resolvedPermission = trace.resolvedPermission;
      this.sourcePermissions.addAll(trace.sourcePermissions);
    }

    public Builder resolvedPermission(GroupPermission resolvedPermission) {
      Objects.requireNonNull(resolvedPermission, "resolvedPermission cannot be null");
      this.resolvedPermission = resolvedPermission;
      return this;
    }

    public Builder sourcePermissions(List<GroupPermissionSource> sourcePermissions) {
      Objects.requireNonNull(sourcePermissions, "sourcePermissions cannot be null");
      this.sourcePermissions.addAll(sourcePermissions);
      return this;
    }

    public GroupPermissionTrace build() {
      Objects.requireNonNull(this.resolvedPermission, "Resolved permission must not be null");
      return new GroupPermissionTrace(this);
    }
  }
}
