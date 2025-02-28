package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.Optional;

public final class Identifier {

  private static final int MAX_ID_LENGTH = 19;

  private static final String ZERO_ID = "0";

  private final String id;

  private final Anchor anchor;

  private Identifier(String id) {
    this.id = id;
    this.anchor = null;
  }

  private Identifier(Anchor anchor) {
    this.id = null;
    this.anchor = anchor;
  }

  public static Identifier ofId(String id) {
    Objects.requireNonNull(id, "id is null");
    if (!isId(id)) {
      throw new IllegalArgumentException("invalid id: " + id);
    }
    return new Identifier(id);
  }

  public static Identifier ofAnchor(String anchor) {
    Objects.requireNonNull(anchor, "anchor is null");
    return Identifier.ofAnchor(Anchor.ofString(anchor));
  }

  public static Identifier ofAnchor(Anchor anchor) {
    Objects.requireNonNull(anchor, "anchor is null");
    if (anchor == Anchor.EMPTY) {
      throw new IllegalArgumentException("anchor is empty");
    }
    return new Identifier(anchor);
  }

  @JsonCreator
  public static Identifier ofString(String identifier) {
    Objects.requireNonNull(identifier, "identifier is null");
    if (identifier.isBlank()) {
      throw new IllegalArgumentException("identifier is blank");
    }
    if (isId(identifier)) {
      return new Identifier(identifier);
    }
    return new Identifier(Anchor.ofString(identifier));
  }

  public Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }

  public Optional<Anchor> getAnchor() {
    return Optional.ofNullable(this.anchor);
  }

  public static boolean isId(String str) {

    if (ZERO_ID.equals(str)) {
      throw new IllegalArgumentException("id should be greater than 0");
    }

    int length = str.length();
    if (length > MAX_ID_LENGTH) {
      return false;
    }

    for (int i = 0; i < length; i++) {
      char c = str.charAt(i);
      if ((c < '0') || (c > '9')) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.anchor);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof Identifier that)) {
      return false;
    }

    return Objects.equals(this.id, that.id) && Objects.equals(this.anchor, that.anchor);
  }

  @JsonValue
  @Override
  public String toString() {
    if (this.id != null) {
      return this.id;
    }
    assert this.anchor != null;
    return this.anchor.toString();
  }
}
