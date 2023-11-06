package com.sitepark.ies.contentrepository.core.domain.entity;

import java.util.Objects;

public class MediaReference {

	private final long mediaId;
	final long usedBy;
	private final MediaReferenceType type;

	protected MediaReference(Builder builder) {
		this.mediaId = builder.mediaId;
		this.usedBy = builder.usedBy;
		this.type = builder.type;
	}

	public long getMediaId() {
		return this.mediaId;
	}

	public long getUsedBy() {
		return this.usedBy;
	}

	public MediaReferenceType getType() {
		return this.type;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(
				this.mediaId,
				this.usedBy,
				this.type);
	}

	@Override
	public final boolean equals(Object o) {

		if (!(o instanceof MediaReference)) {
			return false;
		}

		MediaReference ref = (MediaReference)o;

		return Objects.equals(this.mediaId, ref.mediaId) &&
				Objects.equals(this.usedBy, ref.usedBy) &&
				Objects.equals(this.type, ref.type);
	}

	public static Builder builder() {
		return new Builder();
	}

	public Builder toBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private long mediaId;
		private long usedBy;
		private MediaReferenceType type;

		protected Builder() {}

		protected Builder(MediaReference media) {
			this.mediaId = media.mediaId;
			this.usedBy = media.usedBy;
			this.type = media.type;
		}

		public Builder mediaId(long mediaId) {
			if (mediaId <= 0) {
				throw new IllegalArgumentException("mediaId should be greater than 0");
			}
			this.mediaId = mediaId;
			return this;
		}

		public Builder usedBy(long usedBy) {
			if (usedBy <= 0) {
				throw new IllegalArgumentException("usedBy should be greater than 0");
			}
			this.usedBy = usedBy;
			return this;
		}
		public Builder type(MediaReferenceType type) {
			Objects.requireNonNull(type, "type is null");
			this.type = type;
			return this;
		}
		public MediaReference build() {
			return new MediaReference(this);
		}
	}
}
