package com.sitepark.ies.contentrepository.core.domain.entity;

public class Media extends Entity {

	protected Media(Builder<?> builder) {
		super(builder);
	}

	public static Builder<?> builder() {
		return new MediaBuilder();
	}

	@Override
	public Builder<?> toBuilder() {
		return new MediaBuilder(this);
	}

	public static abstract class Builder<B extends Builder<B>> extends Entity.Builder<B> {
		protected Builder() {}

		protected Builder(Media media) {
			super(media);
		}

		@Override
		public Entity build() {
			return new Media(this);
		}
	}

	private static class MediaBuilder extends Builder<MediaBuilder> {
		protected MediaBuilder() {}

		protected MediaBuilder(Media media) {
			super(media);
		}

		@Override
		protected MediaBuilder self() {
			return this;
		}
	}
}
