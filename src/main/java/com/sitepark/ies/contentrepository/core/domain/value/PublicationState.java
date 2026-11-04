package com.sitepark.ies.contentrepository.core.domain.value;

import java.time.Instant;
import java.util.Objects;

/**
 * The publication of an entry in a channel. It belongs to the content domain, but not to the
 * entry itself; it is read and written through its own port.
 *
 * @param entryId the id of the published entry
 * @param channel the id of the channel (formerly the publisher)
 * @param publishedVersion the version of the entry that is published
 * @param publishedAt when the entry was published
 * @param publishedBy the id of the user who published the entry
 */
public record PublicationState(
    String entryId,
    String channel,
    Instant publishedVersion,
    Instant publishedAt,
    String publishedBy) {

  public PublicationState {
    Objects.requireNonNull(entryId, "entryId is null");
    Objects.requireNonNull(channel, "channel is null");
    Objects.requireNonNull(publishedVersion, "publishedVersion is null");
    Objects.requireNonNull(publishedAt, "publishedAt is null");
    Objects.requireNonNull(publishedBy, "publishedBy is null");
  }
}
