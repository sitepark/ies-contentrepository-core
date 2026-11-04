package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.value.EntryType;
import java.util.Optional;

/** Provides the entry types. Where the types come from is up to the implementation. */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface EntryTypeProvider {

  Optional<EntryType> get(String id);
}
