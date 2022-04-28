package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;
import java.util.Optional;

public final class Identifier implements Serializable {

	private Long id;
	private Anchor anchor;

	private static final long serialVersionUID = 1L;

	private Identifier() {}

	public static Identifier ofId(Long id) {
		Identifier identifier = new Identifier();
		identifier.id = id;
		return identifier;
	}

	public static Identifier ofAnchor(Anchor anchor) {
		Identifier identifier = new Identifier();
		identifier.anchor = anchor;
		return identifier;
	}


	/*
	public void setId(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("id is null");
		}
		if (id <= 0) {
			throw new IllegalArgumentException("id is lower equals 0: " + id);
		}
		this.id = id;
	}

	public void setAnchor(Anchor anchor) {
		if (anchor == null) {
			throw new IllegalArgumentException("anchor is null");
		}
		this.anchor = anchor;
	}
	*/

	public Optional<Long> getId() {
		return Optional.ofNullable(this.id);
	}
	public Optional<Anchor> getAnchor() {
		return Optional.ofNullable(this.anchor);
	}

	@Override
	public String toString() {

		StringBuilder b = new StringBuilder();
		if (this.id != null) {
			b.append(this.id);
		}
		if (this.anchor != null) {
			if (b.length() > 0) {
				b.append('/');
			}
			b.append(this.anchor);
		}

		return b.toString();
	}
}
