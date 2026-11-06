package com.sitepark.ies.contentrepository.core.domain.value;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * The publication of an entry in a channel. It belongs to the content domain, but not to the
 * entry itself; it is read and written through its own port.
 *
 * <p>An entry can be assigned to a channel without being published there, e.g. after it was
 * withdrawn; {@link #published()} tells which.
 *
 * @param entryId the id of the entry
 * @param channel the id of the channel (formerly the publisher)
 * @param published whether the entry is published in the channel
 * @param publishedVersion the version of the entry that is (or was last) published
 * @param publishedAt when the state was last changed, or {@code null} if unknown
 * @param publishedBy the id of the user who last changed the state, or {@code null} if unknown
 */
public record PublicationState(
    String entryId,
    String channel,
    boolean published,
    Instant publishedVersion,
    Instant publishedAt,
    String publishedBy) {

  public PublicationState {
    Objects.requireNonNull(entryId, "entryId is null");
    Objects.requireNonNull(channel, "channel is null");
    Objects.requireNonNull(publishedVersion, "publishedVersion is null");
  }

  /** When the state was last changed, if known. */
  public Optional<Instant> getPublishedAt() {
    return Optional.ofNullable(this.publishedAt);
  }

  /** The user who last changed the state, if known. */
  public Optional<String> getPublishedBy() {
    return Optional.ofNullable(this.publishedBy);
  }
}
