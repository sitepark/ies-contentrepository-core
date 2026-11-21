package com.sitepark.ies.contentrepository.core.domain.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Immutable JSON values without a JSON library: an object is a {@code Map<String, Object>}, an array
 * a {@code List<Object>}, the other values are {@code String}, {@code Number}, {@code Boolean} and
 * {@code null}. Every JSON mapper (Jackson 2 and 3, JSON-B) reads and writes them as they are.
 */
public final class JsonValues {

  private JsonValues() {}

  /**
   * Returns an immutable deep copy of a JSON object.
   *
   * @throws IllegalArgumentException if it contains a value that is no JSON value
   */
  @SuppressWarnings("PMD.UseConcurrentHashMap") // keeps the order and the null values of JSON
  public static Map<String, Object> copyObject(Map<String, ?> object) {
    Objects.requireNonNull(object, "object is null");
    Map<String, Object> copy = new LinkedHashMap<>();
    object.forEach(
        (name, value) -> copy.put(Objects.requireNonNull(name, "name is null"), copy(value)));
    return Collections.unmodifiableMap(copy);
  }

  private static List<Object> copyArray(List<?> array) {
    List<Object> copy = new ArrayList<>(array.size());
    for (Object value : array) {
      copy.add(copy(value));
    }
    return Collections.unmodifiableList(copy);
  }

  @SuppressWarnings("unchecked")
  private static @Nullable Object copy(@Nullable Object value) {
    if (value == null
        || value instanceof String
        || value instanceof Number
        || value instanceof Boolean) {
      return value;
    }
    if (value instanceof Map<?, ?> map) {
      return copyObject((Map<String, ?>) map);
    }
    if (value instanceof List<?> list) {
      return copyArray(list);
    }
    throw new IllegalArgumentException("no JSON value: " + value.getClass().getName());
  }
}
