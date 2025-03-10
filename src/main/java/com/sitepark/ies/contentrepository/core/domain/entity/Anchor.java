package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sitepark.ies.contentrepository.core.domain.exception.InvalidAnchorException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Anchor implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  public static final String VALID_CHARS_REGEX = "[a-zA-Z0-9_.\\-]+";

  private static final Pattern VALIDATOR_PATTERN = Pattern.compile(VALID_CHARS_REGEX);

  private static final Pattern ONLY_NUMBERS_PATTERN = Pattern.compile("[0-9]+");

  /** Used to reset anchor when saving entries. */
  public static final Anchor EMPTY = new Anchor("");

  @JsonValue private final String name;

  private Anchor(String name) {
    this.name = name;
  }

  public static Anchor ofString(String name) {

    if (name == null) {
      return null;
    }

    if (name.isBlank()) {
      return EMPTY;
    }

    Anchor.validate(name);
    return new Anchor(name);
  }

  public String getName() {
    return this.name;
  }

  /**
   * @throws InvalidAnchorException if the anchor is invalid
   */
  private static void validate(String name) {

    if (ONLY_NUMBERS_PATTERN.matcher(name).matches()) {
      throw new InvalidAnchorException(name, "Anchor must not only consist of numbers");
    }

    if (!VALIDATOR_PATTERN.matcher(name).matches()) {
      throw new InvalidAnchorException(
          name, "Anchor contains invalid characters. Allowed are: " + VALID_CHARS_REGEX);
    }
  }

  @Override
  public int hashCode() {
    return this.name != null ? this.name.hashCode() : 0;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Anchor that) && Objects.equals(this.name, that.name);
  }

  @Override
  public String toString() {
    return this == EMPTY ? "EMPTY" : this.name;
  }
}
