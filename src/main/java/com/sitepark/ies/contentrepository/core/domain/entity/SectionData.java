package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sitepark.ies.contentrepository.core.domain.value.JsonValues;
import java.util.Map;
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
 * <p>The JSON object is a map of JSON values without a JSON library ({@link JsonValues}): nested
 * objects are maps, arrays are lists. The data is immutable: the object is copied when the data is
 * created.
 */
public final class SectionData {

  private static final SectionData NO_FIELDS = new SectionData(Map.of());

  private final Map<String, Object> json;

  private SectionData(Map<String, Object> json) {
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
   * @throws IllegalArgumentException if it contains a value that is no JSON value
   */
  @JsonCreator
  public static SectionData of(Map<String, ?> json) {
    Objects.requireNonNull(json, "json is null");
    return new SectionData(JsonValues.copyObject(json));
  }

  /** The JSON object, immutable. */
  @JsonValue
  public Map<String, Object> toMap() {
    return this.json;
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
