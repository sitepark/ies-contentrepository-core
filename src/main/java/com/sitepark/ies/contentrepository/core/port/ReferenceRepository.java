package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.value.Reference;
import java.util.List;

/** Stores the references found in the sections of entries and groups. */
public interface ReferenceRepository {

  /** Replaces all references of an entry or a group. */
  void replace(String entityId, List<Reference> references);

  /** Returns the references pointing to a target, e.g. {@code ies:media/42}. */
  List<Reference> getReferencesTo(String target);
}
