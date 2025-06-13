package com.sitepark.ies.contentrepository.core.domain.value.permission;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.*;

@JsonDeserialize(builder = ContainedEntriesPermission.Builder.class)
public final class ContainedEntriesPermission {

  private final boolean create;
  private final boolean read;
  private final boolean write;
  private final boolean delete;
  private final Set<String> sectionGrants;
  private final Set<String> channels;

  private ContainedEntriesPermission(Builder builder) {
    this.create = builder.create;
    this.read = builder.read;
    this.write = builder.write;
    this.delete = builder.delete;
    this.sectionGrants = Collections.unmodifiableSet(builder.sectionGrants);
    this.channels = Collections.unmodifiableSet(builder.channels);
  }

  public boolean isCreate() {
    return create;
  }

  public boolean isRead() {
    return read;
  }

  public boolean isWrite() {
    return write;
  }

  public boolean isDelete() {
    return delete;
  }

  public Set<String> getSectionGrants() {
    return sectionGrants;
  }

  public Set<String> getChannels() {
    return channels;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ContainedEntriesPermission that)) {
      return false;
    }
    return create == that.create
        && read == that.read
        && write == that.write
        && delete == that.delete
        && Objects.equals(sectionGrants, that.sectionGrants)
        && Objects.equals(channels, that.channels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(create, read, write, delete, sectionGrants, channels);
  }

  @Override
  public String toString() {
    return "ContainedEntriesPermission{"
        + "create="
        + create
        + ", read="
        + read
        + ", write="
        + write
        + ", delete="
        + delete
        + ", sectionGrants="
        + sectionGrants
        + ", channels="
        + channels
        + '}';
  }

  @JsonPOJOBuilder(withPrefix = "")
  @SuppressWarnings("PMD.TooManyMethods")
  public static final class Builder {

    private boolean create;
    private boolean read;
    private boolean write;
    private boolean delete;
    private final Set<String> sectionGrants = new HashSet<>();
    private final Set<String> channels = new HashSet<>();

    private Builder() {}

    private Builder(ContainedEntriesPermission entity) {
      this.create = entity.create;
      this.read = entity.read;
      this.write = entity.write;
      this.delete = entity.delete;
      this.sectionGrants.addAll(entity.sectionGrants);
      this.channels.addAll(entity.channels);
    }

    @JsonSetter
    public Builder create(boolean create) {
      this.create = create;
      return this;
    }

    @JsonSetter
    public Builder read(boolean read) {
      this.read = read;
      return this;
    }

    @JsonSetter
    public Builder write(boolean write) {
      this.write = write;
      return this;
    }

    @JsonSetter
    public Builder delete(boolean delete) {
      this.delete = delete;
      return this;
    }

    @JsonSetter
    public Builder sectionGrants(Collection<String> sectionGrants) {
      Objects.requireNonNull(sectionGrants, "sectionGrants is null");
      this.sectionGrants.clear();
      for (String sectionGrant : sectionGrants) {
        this.sectionGrant(sectionGrant);
      }
      return this;
    }

    public Builder sectionGrants(String... sectionGrants) {
      Objects.requireNonNull(sectionGrants, "sectionGrants is null");
      this.sectionGrants.clear();
      for (String sectionGrant : sectionGrants) {
        this.sectionGrant(sectionGrant);
      }
      return this;
    }

    public Builder sectionGrant(String sectionGrant) {
      Objects.requireNonNull(sectionGrant, "sectionGrant is null");
      this.sectionGrants.add(sectionGrant);
      return this;
    }

    @JsonSetter
    public Builder channels(Collection<String> channels) {
      Objects.requireNonNull(channels, "channels is null");
      this.channels.clear();
      for (String channel : channels) {
        this.channel(channel);
      }
      return this;
    }

    public Builder channels(String... channels) {
      Objects.requireNonNull(channels, "channels is null");
      this.channels.clear();
      for (String channel : channels) {
        this.channel(channel);
      }
      return this;
    }

    public Builder channel(String channel) {
      Objects.requireNonNull(channel, "channel is null");
      this.channels.add(channel);
      return this;
    }

    public Builder merge(ContainedEntriesPermission permission) {
      this.create = this.create || permission.create;
      this.read = this.read || permission.read;
      this.write = this.write || permission.write;
      this.delete = this.delete || permission.delete;
      this.sectionGrants.addAll(permission.sectionGrants);
      this.channels.addAll(permission.channels);
      return this;
    }

    public ContainedEntriesPermission build() {
      return new ContainedEntriesPermission(this);
    }
  }
}
