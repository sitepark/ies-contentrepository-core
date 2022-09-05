package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sitepark.ies.contentrepository.core.domain.exception.InvalidAnchor;

public final class Anchor implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Wird verwendet, um Anchor beim speichern von Einträgen zurück zu setzen.
	 */
	public static final Anchor EMPTY = new Anchor("");

	@JsonValue
	private final String name;

	private Anchor(String name) {
		this.name = name;
	}

	public static Optional<Anchor> ofString(String name) {
		if (name == null) {
			return Optional.empty();
		} else {

			if (name.isBlank()) {
				return Optional.of(EMPTY);
			}

			Anchor.validate(name);
			return Optional.of(new Anchor(name));
		}
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @throws InvalidAnchor
	 */
	private static void validate(String name) {
		if (name == null) {
			return;
		}

		// TODO: implement validator
		if (name.indexOf(' ') != -1) {
			throw new InvalidAnchor(name, "Anchor contains Spaces");
		}
	}

	@Override
	public int hashCode() {
		return this.name != null ? this.name.hashCode() : 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Anchor)) {
			return false;
		}

		Anchor anchor = (Anchor)o;
		return Objects.equals(anchor.name, this.name);
	}

	@Override
	public String toString() {
		return this == EMPTY ? "EMPTY" : this.name;
	}
}
