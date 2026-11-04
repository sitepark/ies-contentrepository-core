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
import org.jspecify.annotations.Nullable;

/** Moves a section of an entry, within its container or into another one. */
@SuppressWarnings("PMD.ExcessiveParameterList")
public final class MoveSection extends AbstractSectionUseCase {

  @Inject
  MoveSection(
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

  MoveSection(
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
   * Moves a section.
   *
   * @param entryId the id of the entry
   * @param expectedVersion the version of the entry the change is based on
   * @param uuid the UUID of the section
   * @param container the target container; {@code null} for the fixed sections
   * @param index the position within the target container
   * @return the new version of the entry
   */
  public Instant move(
      String entryId, Instant expectedVersion, String uuid, @Nullable String container, int index) {
    return this.change.apply(
        entryId,
        expectedVersion,
        entry -> this.checks.checkGrant(entryId, existing(entry, uuid)),
        entry -> entry.withSectionMovedTo(uuid, container, index));
  }
}
