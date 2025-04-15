package com.sitepark.ies.contentrepository.core.domain.entity.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.sharedkernel.security.domain.Permission;
import java.util.Objects;

@JsonDeserialize(builder = GroupPermission.Builder.class)
public final class GroupPermission implements Permission {

  public static final String TYPE = "CONTENT_GROUP";

  private final String groupId;
  private final boolean recursive;
  private final boolean read;
  private final boolean write;
  private final boolean hidden;
  private final ContainedGroupsPermission groups;
  private final ContainedEntriesPermission entries;

  private GroupPermission(Builder builder) {
    this.groupId = builder.groupId;
    this.recursive = builder.recursive;
    this.read = builder.read;
    this.write = builder.write;
    this.hidden = builder.hidden;
    this.groups = builder.groups;
    this.entries = builder.entries;
  }

  public String getType() {
    return TYPE;
  }

  public String getGroupId() {
    return groupId;
  }

  public boolean isRecursive() {
    return recursive;
  }

  public boolean isRead() {
    return read;
  }

  public boolean isWrite() {
    return write;
  }

  public boolean isHidden() {
    return hidden;
  }

  public ContainedGroupsPermission getGroups() {
    return groups;
  }

  public ContainedEntriesPermission getEntries() {
    return entries;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GroupPermission that)) {
      return false;
    }
    return Objects.equals(groupId, that.groupId)
        && recursive == that.recursive
        && read == that.read
        && write == that.write
        && hidden == that.hidden
        && Objects.equals(groups, that.groups)
        && Objects.equals(entries, that.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, recursive, read, write, hidden, groups, entries);
  }

  @Override
  public String toString() {
    return "GroupPermission{"
        + "groupId="
        + groupId
        + ", recursive="
        + recursive
        + ", read="
        + read
        + ", write="
        + write
        + ", hidden="
        + hidden
        + ", groups="
        + groups
        + ", entries="
        + entries
        + '}';
  }

  @JsonPOJOBuilder(withPrefix = "")
  @JsonIgnoreProperties({"type"})
  public static final class Builder {

    private String groupId;
    private boolean recursive;
    private boolean read;
    private boolean write;
    private boolean hidden;
    private ContainedGroupsPermission groups;
    private ContainedEntriesPermission entries;

    private Builder() {}

    private Builder(GroupPermission entity) {
      this.groupId = entity.groupId;
      this.recursive = entity.recursive;
      this.read = entity.read;
      this.write = entity.write;
      this.hidden = entity.hidden;
      this.groups = entity.groups;
      this.entries = entity.entries;
    }

    public Builder groupId(String groupId) {
      this.groupId = Objects.requireNonNull(groupId, "groupId is null");
      return this;
    }

    public Builder recursive(boolean recursive) {
      this.recursive = recursive;
      return this;
    }

    public Builder read(boolean read) {
      this.read = read;
      return this;
    }

    public Builder write(boolean write) {
      this.write = write;
      return this;
    }

    public Builder hidden(boolean hidden) {
      this.hidden = hidden;
      return this;
    }

    public Builder groups(ContainedGroupsPermission groups) {
      this.groups = Objects.requireNonNull(groups, "groups is null");
      return this;
    }

    public Builder entries(ContainedEntriesPermission entries) {
      this.entries = Objects.requireNonNull(entries, "entries is null");
      return this;
    }

    public Builder merge(GroupPermission permission) {
      this.read = this.read || permission.isRead();
      this.write = this.write || permission.isWrite();
      this.hidden = this.hidden || permission.isHidden();
      this.groups = this.groups.toBuilder().merge(permission.getGroups()).build();
      this.entries = this.entries.toBuilder().merge(permission.getEntries()).build();
      return this;
    }

    public GroupPermission build() {
      if (this.groups == null) {
        this.groups = ContainedGroupsPermission.builder().build();
      }
      if (this.entries == null) {
        this.entries = ContainedEntriesPermission.builder().build();
      }
      return new GroupPermission(this);
    }
  }
}
