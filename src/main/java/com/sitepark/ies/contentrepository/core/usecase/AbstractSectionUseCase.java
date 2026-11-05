package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.Entry;
import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionNotFoundException;
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
import jakarta.inject.Provider;
import java.time.Clock;

/**
 * Base of the use cases that change single sections of an entry. Each change is based on the
 * version of the entry the client has read; it fails if the entry has changed since, if another
 * user locked the entry, or if the current user may not change the entry or the section.
 */
@SuppressWarnings({"PMD.ExcessiveParameterList", "PMD.AbstractClassWithoutAbstractMethod"})
abstract class AbstractSectionUseCase {

  protected final SectionChange change;
  protected final SectionChecks checks;

  protected AbstractSectionUseCase(
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
    this.change =
        new SectionChange(
            repository,
            lockManager,
            versioningManager,
            historyManager,
            accessControl,
            searchIndex,
            referenceRepository,
            authentication,
            clock);
    this.checks = new SectionChecks(accessControl, sectionTypes, schemaValidator);
  }

  /** Checks the grant of the parent section of a position, if the position lies in a section. */
  protected void checkParentGrant(String entryId, Entry entry, SectionPosition position) {
    position
        .getParent()
        .ifPresent(parent -> this.checks.checkGrant(entryId, existing(entry, parent)));
  }

  /**
   * Returns the section of the entry.
   *
   * @throws SectionNotFoundException if the entry has no section with the UUID
   */
  protected static Section existing(Entry entry, String uuid) {
    return entry.getSection(uuid).orElseThrow(() -> new SectionNotFoundException(uuid));
  }
}
