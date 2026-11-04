package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.SectionData;
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

/** Replaces the data of a section of an entry. */
@SuppressWarnings("PMD.ExcessiveParameterList")
public final class UpdateSection extends AbstractSectionUseCase {

  @Inject
  UpdateSection(
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

  UpdateSection(
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
   * Replaces the data of a section; type, container and position stay.
   *
   * @param entryId the id of the entry
   * @param expectedVersion the version of the entry the change is based on
   * @param uuid the UUID of the section
   * @param data the new data
   * @return the new version of the entry
   */
  public Instant update(String entryId, Instant expectedVersion, String uuid, SectionData data) {
    return this.change.apply(
        entryId,
        expectedVersion,
        entry -> this.checks.check(entryId, existing(entry, uuid).toBuilder().data(data).build()),
        entry -> entry.withSectionData(uuid, data));
  }
}
