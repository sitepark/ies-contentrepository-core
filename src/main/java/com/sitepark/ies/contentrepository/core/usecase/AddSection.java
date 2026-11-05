package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.value.SectionPosition;
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

/** Adds a section to an entry. */
@SuppressWarnings("PMD.ExcessiveParameterList")
public final class AddSection extends AbstractSectionUseCase {

  @Inject
  AddSection(
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

  AddSection(
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
   * Adds a section.
   *
   * @param entryId the id of the entry
   * @param expectedVersion the version of the entry the change is based on
   * @param section the new section with its UUID, type, container and data
   * @param index the position within the container of the section
   * @return the new version of the entry
   */
  public Instant add(String entryId, Instant expectedVersion, Section section, int index) {
    return this.add(
        entryId,
        expectedVersion,
        section,
        SectionPosition.topLevel(section.getContainer().orElse(null), index));
  }

  /**
   * Adds a section, together with the sections below it, at a position in the tree of sections.
   *
   * <p>Type, grant and schema of each added section are checked, and the grant of the parent
   * section of the position.
   *
   * @return the new version of the entry
   */
  public Instant add(
      String entryId, Instant expectedVersion, Section section, SectionPosition position) {
    return this.change.apply(
        entryId,
        expectedVersion,
        entry -> {
          this.checkParentGrant(entryId, entry, position);
          section.stream().forEach(s -> this.checks.check(entryId, s));
        },
        entry -> entry.withSection(section, position));
  }
}
