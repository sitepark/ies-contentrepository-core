package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.value.PublicationState;
import java.util.List;

/** Stores the publication states of entries, one per entry and channel. */
public interface PublicationStateRepository {

  /** Returns the publication states of an entry in all channels. */
  List<PublicationState> get(String entryId);

  /** Stores the state, replacing the state of the same entry and channel. */
  void store(PublicationState state);
}
