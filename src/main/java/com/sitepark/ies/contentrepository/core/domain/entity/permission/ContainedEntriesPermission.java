package com.sitepark.ies.contentrepository.core.domain.entity.permission;

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
  private final Set<String> sectionTypes;
  private final Set<Integer> channels;

  private ContainedEntriesPermission(Builder builder) {
    this.create = builder.create;
    this.read = builder.read;
    this.write = builder.write;
    this.delete = builder.delete;
    this.sectionTypes = Collections.unmodifiableSet(builder.sectionTypes);
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

  public Set<String> getSectionTypes() {
    return sectionTypes;
  }

  public Set<Integer> getChannels() {
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
        && Objects.equals(sectionTypes, that.sectionTypes)
        && Objects.equals(channels, that.channels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(create, read, write, delete, sectionTypes, channels);
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
        + ", sectionTypes="
        + sectionTypes
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
    private final Set<String> sectionTypes = new HashSet<>();
    private final Set<Integer> channels = new HashSet<>();

    private Builder() {}

    private Builder(ContainedEntriesPermission entity) {
      this.create = entity.create;
      this.read = entity.read;
      this.write = entity.write;
      this.delete = entity.delete;
      this.sectionTypes.addAll(entity.sectionTypes);
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
    public Builder sectionTypes(Set<String> sectionTypes) {
      Objects.requireNonNull(sectionTypes, "sectionTypes is null");
      this.sectionTypes.clear();
      for (String sectionType : sectionTypes) {
        this.sectionType(sectionType);
      }
      return this;
    }

    public Builder sectionTypes(String... sectionTypes) {
      Objects.requireNonNull(sectionTypes, "sectionTypes is null");
      this.sectionTypes.clear();
      for (String sectionType : sectionTypes) {
        this.sectionType(sectionType);
      }
      return this;
    }

    public Builder sectionType(String sectionType) {
      Objects.requireNonNull(sectionType, "sectionType is null");
      this.sectionTypes.add(sectionType);
      return this;
    }

    @JsonSetter
    public Builder channels(Set<Integer> channels) {
      Objects.requireNonNull(channels, "channels is null");
      this.channels.clear();
      for (Integer channel : channels) {
        this.channel(channel);
      }
      return this;
    }

    public Builder channels(Integer... channels) {
      Objects.requireNonNull(channels, "channels is null");
      this.channels.clear();
      for (Integer channel : channels) {
        this.channel(channel);
      }
      return this;
    }

    public Builder channel(Integer channel) {
      Objects.requireNonNull(channel, "channel is null");
      this.channels.add(channel);
      return this;
    }

    public Builder merge(ContainedEntriesPermission permission) {
      this.create = this.create || permission.create;
      this.read = this.read || permission.read;
      this.write = this.write || permission.write;
      this.delete = this.delete || permission.delete;
      this.sectionTypes.addAll(permission.sectionTypes);
      this.channels.addAll(permission.channels);
      return this;
    }

    public ContainedEntriesPermission build() {
      return new ContainedEntriesPermission(this);
    }
  }
}
