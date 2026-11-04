package com.sitepark.ies.contentrepository.core.domain.value;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;
import java.util.Optional;

/**
 * The type of a section, as far as the content repository needs it. Templates and the aggregator
 * configuration of a type are not part of it.
 *
 * <p>Section types come from product and customer modules, completed by the client configuration.
 * A type may describe its data with a JSON schema; types without schema stay possible for backward
 * compatibility. Selected types name a grant (a {@code SectionGrant}) that an editor needs to
 * change sections of that type.
 */
@SuppressWarnings("PMD.LawOfDemeter")
public final class SectionType {

  private final String id;
  private final JsonNode schema;
  private final String grant;
  private final String origin;

  private SectionType(Builder builder) {
    this.id = builder.id;
    this.schema = builder.schema;
    this.grant = builder.grant;
    this.origin = builder.origin;
  }

  public String getId() {
    return this.id;
  }

  /** The JSON schema of the data; a copy, changes to it do not affect the type. */
  public Optional<JsonNode> getSchema() {
    return Optional.ofNullable(this.schema).map(JsonNode::deepCopy);
  }

  /** The id of the grant an editor needs to change sections of this type. */
  public Optional<String> getGrant() {
    return Optional.ofNullable(this.grant);
  }

  /** Where the type is defined: a module or the client configuration. */
  public String getOrigin() {
    return this.origin;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.schema, this.grant, this.origin);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof SectionType that
        && Objects.equals(this.id, that.id)
        && Objects.equals(this.schema, that.schema)
        && Objects.equals(this.grant, that.grant)
        && Objects.equals(this.origin, that.origin);
  }

  @Override
  public String toString() {
    return "SectionType{"
        + "id='"
        + this.id
        + '\''
        + ", schema="
        + this.schema
        + ", grant='"
        + this.grant
        + '\''
        + ", origin='"
        + this.origin
        + '\''
        + '}';
  }

  public static final class Builder {

    private String id;
    private JsonNode schema;
    private String grant;
    private String origin;

    private Builder() {}

    private Builder(SectionType type) {
      this.id = type.id;
      this.schema = type.schema;
      this.grant = type.grant;
      this.origin = type.origin;
    }

    public Builder id(String id) {
      this.id = requireNotBlank(id, "id");
      return this;
    }

    /**
     * Sets the JSON schema of the data.
     *
     * @param schema the schema; it is copied, later changes to it do not affect the type
     */
    public Builder schema(JsonNode schema) {
      Objects.requireNonNull(schema, "schema is null");
      this.schema = schema.deepCopy();
      return this;
    }

    public Builder grant(String grant) {
      this.grant = requireNotBlank(grant, "grant");
      return this;
    }

    public Builder origin(String origin) {
      this.origin = requireNotBlank(origin, "origin");
      return this;
    }

    /**
     * Builds the type.
     *
     * @throws IllegalStateException if the id or the origin is missing
     */
    public SectionType build() {
      if (this.id == null) {
        throw new IllegalStateException("id is not set");
      }
      if (this.origin == null) {
        throw new IllegalStateException("origin is not set");
      }
      return new SectionType(this);
    }

    private static String requireNotBlank(String value, String name) {
      Objects.requireNonNull(value, name + " is null");
      if (value.isBlank()) {
        throw new IllegalArgumentException(name + " is blank");
      }
      return value;
    }
  }
}
