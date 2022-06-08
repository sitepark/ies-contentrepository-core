package com.sitepark.ies.contentrepository.core.domain.entity;

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
			assert mediaId > 0;
			this.mediaId = mediaId;
			return this;
		}

		public Builder usedBy(long usedBy) {
			assert usedBy > 0;
			this.usedBy = usedBy;
			return this;
		}
		public Builder type(MediaReferenceType type) {
			assert type != null;
			this.type = type;
			return this;
		}
		public MediaReference build() {
			return new MediaReference(this);
		}
	}
}
