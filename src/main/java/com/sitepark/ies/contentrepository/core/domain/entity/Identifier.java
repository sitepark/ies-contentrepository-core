package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Identifier {

	private static final int MAX_ID_LENGTH = 19;

	private static final String ZERO_ID = "0";

	private final Long id;

	private final Anchor anchor;

	private Identifier(Long id) {
		this.id = id;
		this.anchor = null;
	}

	private Identifier(Anchor anchor) {
		this.id = null;
		this.anchor = anchor;
	}

	public static Identifier ofId(long id) {
		return new Identifier(id);
	}

	public static Identifier ofAnchor(Anchor anchor) {
		Objects.requireNonNull(anchor, "anchor is null");
		return new Identifier(anchor);
	}

	@JsonCreator
	public static Identifier ofString(String identifier) {
		if (isId(identifier)) {
			return new Identifier(Long.valueOf(identifier));
		}
		return new Identifier(Anchor.ofString(identifier));
	}

	public Optional<Long> getId() {
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
			if (c < '0' || c > '9') {
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

		if (!(o instanceof Identifier)) {
			return false;
		}

		Identifier that = (Identifier)o;
		return
				Objects.equals(this.id, that.id) &&
				Objects.equals(this.anchor, that.anchor);
	}

	@Override
	public String toString() {
		if (this.id != null) {
			return this.id.toString();
		}
		return this.anchor.toString();
	}
}
