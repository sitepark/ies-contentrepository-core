package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.ReferenceRepository;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.SectionSchemaValidator;
import com.sitepark.ies.contentrepository.core.port.SectionTypeProvider;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import com.sitepark.ies.sharedkernel.security.Authentication;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.time.Clock;
import java.time.Instant;

/** Removes a section from an entry. */
@SuppressWarnings("PMD.ExcessiveParameterList")
public final class RemoveSection extends AbstractSectionUseCase {

  @Inject
  RemoveSection(
      ContentRepository repository,
      EntityLockManager lockManager,
      VersioningManager versioningManager,
      HistoryManager historyManager,
      AccessControl accessControl,
      SearchIndex searchIndex,
      ReferenceRepository referenceRepository,
      SectionTypeProvider sectionTypes,
      SectionSchemaValidator schemaValidator,
      Provider<Authentication> authentication) {
    this(
        repository,
        lockManager,
        versioningManager,
        historyManager,
        accessControl,
        searchIndex,
        referenceRepository,
        sectionTypes,
        schemaValidator,
        authentication,
        Clock.systemUTC());
  }

  RemoveSection(
      ContentRepository repository,
      EntityLockManager lockManager,
      VersioningManager versioningManager,
      HistoryManager historyManager,
      AccessControl accessControl,
      SearchIndex searchIndex,
      ReferenceRepository referenceRepository,
      SectionTypeProvider sectionTypes,
      SectionSchemaValidator schemaValidator,
      Provider<Authentication> authentication,
      Clock clock) {
    super(
        repository,
        lockManager,
        versioningManager,
        historyManager,
        accessControl,
        searchIndex,
        referenceRepository,
        sectionTypes,
        schemaValidator,
        authentication,
        clock);
  }

  /**
   * Removes a section.
   *
   * @param entryId the id of the entry
   * @param expectedVersion the version of the entry the change is based on
   * @param uuid the UUID of the section
   * @return the new version of the entry
   */
  public Instant remove(String entryId, Instant expectedVersion, String uuid) {
    return this.change.apply(
        entryId,
        expectedVersion,
        entry -> this.checks.checkGrant(entryId, existing(entry, uuid)),
        entry -> entry.withoutSection(uuid));
  }
}
