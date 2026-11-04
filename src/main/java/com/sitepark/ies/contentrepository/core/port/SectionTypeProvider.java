package com.sitepark.ies.contentrepository.core.port;

import com.sitepark.ies.contentrepository.core.domain.value.SectionType;
import java.util.Optional;

/**
 * Provides the section types. Where the types come from (modules, client configuration) is up to
 * the implementation.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface SectionTypeProvider {

  Optional<SectionType> get(String id);
}
