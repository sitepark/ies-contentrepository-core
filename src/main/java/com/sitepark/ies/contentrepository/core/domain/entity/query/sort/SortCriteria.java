package com.sitepark.ies.contentrepository.core.domain.entity.query.sort;

import java.util.Objects;

public abstract class SortCriteria {

	private final Direction direction;

	public SortCriteria(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.direction);
	}

	@Override
	public final boolean equals(Object o) {

		if (!(o instanceof SortCriteria that)) {
			return false;
		}

		return Objects.equals(this.direction, that.direction);
	}
}
