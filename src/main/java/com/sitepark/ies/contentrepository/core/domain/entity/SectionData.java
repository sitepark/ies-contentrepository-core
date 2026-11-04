package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * The data of a section: a JSON object.
 *
 * <p>Texts are strings. Links and media are URI values: {@code ies:entity/<id>} for an entry or a
 * group, {@code ies:media/<id>} for a medium, any other URI (e.g. {@code https://…}) for an
 * external target. A section type may describe the data with a JSON schema; without one the data
 * is free.
 *
 * <p>The data is immutable: the JSON object is copied when the data is created and whenever it is
 * handed out.
 */
public final class SectionData {

  private static final SectionData NO_FIELDS =
      new SectionData(JsonNodeFactory.instance.objectNode());

  private final ObjectNode json;

  private SectionData(ObjectNode json) {
    this.json = json;
  }

  /** Data without any fields. */
  public static SectionData empty() {
    return NO_FIELDS;
  }

  /**
   * Creates the data from a JSON object.
   *
   * @param json the JSON object; it is copied, later changes to it do not affect the data
   */
  @JsonCreator
  public static SectionData of(ObjectNode json) {
    Objects.requireNonNull(json, "json is null");
    return new SectionData(json.deepCopy());
  }

  /** Returns a copy of the JSON object; changes to it do not affect the data. */
  @JsonValue
  public ObjectNode toJson() {
    return this.json.deepCopy();
  }

  public boolean isEmpty() {
    return this.json.isEmpty();
  }

  @Override
  public int hashCode() {
    return this.json.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object o) {
    return o instanceof SectionData that && this.json.equals(that.json);
  }

  @Override
  public String toString() {
    return "SectionData{" + "json=" + this.json + '}';
  }
}
