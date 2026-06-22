package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.sharedkernel.domain.UrlMappingMode;

public record UrlMapping(
    String id,
    String siteId,
    String channelId,
    String path,
    String ownerId,
    UrlMappingMode mode,
    int priority) {

  public UrlMapping {
    if (siteId == null || siteId.isBlank()) {
      throw new IllegalArgumentException("Site ID must not be null or blank");
    }
    if (channelId == null || channelId.isBlank()) {
      throw new IllegalArgumentException("Channel ID must not be null or blank");
    }
    if (path == null || path.isBlank()) {
      throw new IllegalArgumentException("Path must not be null or blank");
    }
    if (ownerId == null || ownerId.isBlank()) {
      throw new IllegalArgumentException("Owner ID must not be null or blank");
    }
    if (mode == null) {
      throw new IllegalArgumentException("Mapping mode must not be null");
    }
  }

  public boolean isRedirect() {
    return mode.isRedirect();
  }

  public boolean isForward() {
    return mode.isForward();
  }

  /**
   * Returns {@code true} if this mapping is content-identical to {@code other}, ignoring the
   * technical {@code id} field. Two mappings are considered identical when {@code siteId}, {@code
   * channelId}, {@code path}, {@code ownerId}, {@code mode}, and {@code priority} all match.
   *
   * @param other the mapping to compare against; must not be {@code null}
   * @return {@code true} if all content fields are equal
   */
  public boolean hasSameContent(UrlMapping other) {
    return this.siteId.equals(other.siteId)
        && this.channelId.equals(other.channelId)
        && this.path.equals(other.path)
        && this.ownerId.equals(other.ownerId)
        && this.mode == other.mode
        && this.priority == other.priority;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {
    private String id;
    private String siteId;
    private String channelId;
    private String path;
    private String ownerId;
    private UrlMappingMode mode;
    private int priority;

    public Builder() {}

    private Builder(UrlMapping mapping) {
      this.id = mapping.id;
      this.siteId = mapping.siteId;
      this.channelId = mapping.channelId;
      this.path = mapping.path;
      this.ownerId = mapping.ownerId;
      this.mode = mapping.mode;
      this.priority = mapping.priority;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder siteId(String siteId) {
      this.siteId = siteId;
      return this;
    }

    public Builder channelId(String channelId) {
      this.channelId = channelId;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder ownerId(String ownerId) {
      this.ownerId = ownerId;
      return this;
    }

    public Builder mode(UrlMappingMode mode) {
      this.mode = mode;
      return this;
    }

    public Builder priority(int priority) {
      this.priority = priority;
      return this;
    }

    public UrlMapping build() {
      return new UrlMapping(id, siteId, channelId, path, ownerId, mode, priority);
    }
  }
}
