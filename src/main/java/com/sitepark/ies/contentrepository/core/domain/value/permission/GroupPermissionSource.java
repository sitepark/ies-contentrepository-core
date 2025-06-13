package com.sitepark.ies.contentrepository.core.domain.value.permission;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonDeserialize(builder = GroupPermissionSource.Builder.class)
public final class GroupPermissionSource {

  private final Set<String> roleIds;
  private final String privilegeId;
  private final GroupPermission groupPermission;

  private GroupPermissionSource(Builder builder) {
    this.roleIds = Set.copyOf(builder.roleIds);
    this.privilegeId = builder.privilegeId;
    this.groupPermission = builder.groupPermission;
  }

  public Set<String> getRoleIds() {
    return this.roleIds;
  }

  public String getPrivilegeId() {
    return privilegeId;
  }

  public GroupPermission getGroupPermission() {
    return groupPermission;
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleIds, privilegeId, groupPermission);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GroupPermissionSource that)) {
      return false;
    }
    return Objects.equals(roleIds, that.roleIds)
        && Objects.equals(privilegeId, that.privilegeId)
        && Objects.equals(groupPermission, that.groupPermission);
  }

  @Override
  public String toString() {
    return "GroupPermissionSource{"
        + "roleIds="
        + roleIds
        + ", privilegeId="
        + privilegeId
        + ", groupPermission="
        + groupPermission
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
    private final Set<String> roleIds = new HashSet<>();
    private String privilegeId;
    private GroupPermission groupPermission;

    protected Builder() {}

    protected Builder(GroupPermissionSource groupPermissionSource) {
      this.roleIds.addAll(groupPermissionSource.roleIds);
      this.privilegeId = groupPermissionSource.privilegeId;
      this.groupPermission = groupPermissionSource.groupPermission.toBuilder().build();
    }

    @JsonSetter
    public Builder roleIds(Collection<String> roleIds) {
      Objects.requireNonNull(roleIds, "roleIds is null");
      this.roleIds.clear();
      for (String roleId : roleIds) {
        this.roleId(roleId);
      }
      return this;
    }

    public Builder roleIds(String... roleIds) {
      Objects.requireNonNull(roleIds, "roleIds is null");
      this.roleIds.clear();
      for (String roleId : roleIds) {
        this.roleId(roleId);
      }
      return this;
    }

    public Builder roleId(String roleId) {
      Objects.requireNonNull(roleId, "roleId is null");
      this.roleIds.add(roleId);
      return this;
    }

    public Builder privilegeId(String privilegeId) {
      Objects.requireNonNull(privilegeId, "privilegeId cannot be null");
      this.privilegeId = privilegeId;
      return this;
    }

    public Builder groupPermission(GroupPermission groupPermission) {
      Objects.requireNonNull(groupPermission, "groupPermission cannot be null");
      this.groupPermission = groupPermission;
      return this;
    }

    public GroupPermissionSource build() {
      Objects.requireNonNull(privilegeId, "privilegeId cannot be null");
      Objects.requireNonNull(groupPermission, "groupPermission cannot be null");
      return new GroupPermissionSource(this);
    }
  }
}
