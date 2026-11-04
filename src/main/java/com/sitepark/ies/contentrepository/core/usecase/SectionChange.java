package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.Entry;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.VersionConflictException;
import com.sitepark.ies.contentrepository.core.domain.service.ReferenceExtractor;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.ReferenceRepository;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import com.sitepark.ies.sharedkernel.security.AccessDeniedException;
import com.sitepark.ies.sharedkernel.security.Authentication;
import com.sitepark.ies.sharedkernel.security.UserAuthentication;
import jakarta.inject.Provider;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.Nullable;

/**
 * The common course of the section use cases: load the entry, check version, lock and access,
 * apply the change, create a new version, store it, replace the references and record history and
 * index.
 *
 * <p>Every change creates a new version of the whole entry; history stays on entry level.
 */
final class SectionChange {

  private final ContentRepository repository;
  private final EntityLockManager lockManager;
  private final VersioningManager versioningManager;
  private final HistoryManager historyManager;
  private final AccessControl accessControl;
  private final SearchIndex searchIndex;
  private final ReferenceRepository referenceRepository;
  private final Provider<Authentication> authentication;
  private final Clock clock;
  private final ReferenceExtractor referenceExtractor = new ReferenceExtractor();

  SectionChange(
      ContentRepository repository,
      EntityLockManager lockManager,
      VersioningManager versioningManager,
      HistoryManager historyManager,
      AccessControl accessControl,
      SearchIndex searchIndex,
      ReferenceRepository referenceRepository,
      Provider<Authentication> authentication,
      Clock clock) {
    this.repository = repository;
    this.lockManager = lockManager;
    this.versioningManager = versioningManager;
    this.historyManager = historyManager;
    this.accessControl = accessControl;
    this.searchIndex = searchIndex;
    this.referenceRepository = referenceRepository;
    this.authentication = authentication;
    this.clock = clock;
  }

  /**
   * Changes an entry.
   *
   * @param id the id of the entry
   * @param expectedVersion the version the change is based on
   * @param check checks on the loaded entry before the change, e.g. type, grant and schema of the
   *     affected section
   * @param change the change of the entry
   * @return the new version of the entry
   * @throws EntityNotFoundException if there is no entry with the id
   * @throws VersionConflictException if the entry has another version
   * @throws EntityLockedException if another user locked the entry
   * @throws AccessDeniedException if the current user may not change the entry
   */
  Instant apply(
      String id, Instant expectedVersion, Consumer<Entry> check, UnaryOperator<Entry> change) {
    Objects.requireNonNull(id, "id is null");
    Objects.requireNonNull(expectedVersion, "expectedVersion is null");

    Entry entry = this.repository.getEntry(id).orElseThrow(() -> new EntityNotFoundException(id));

    Instant actualVersion = entry.getVersion().orElse(null);
    if (!expectedVersion.equals(actualVersion)) {
      throw new VersionConflictException(id, expectedVersion, actualVersion);
    }

    String userId = this.currentUserId();
    this.lockManager
        .getLock(id)
        .filter(lock -> !lock.getUserId().equals(userId))
        .ifPresent(
            lock -> {
              throw new EntityLockedException(lock);
            });

    if (!this.accessControl.isEntityWritable(id)) {
      throw new AccessDeniedException("Not allowed to update entity " + id);
    }

    check.accept(entry);

    Entry changed = change.apply(entry);
    Entry.Builder builder = changed.toBuilder().changed(Instant.now(this.clock));
    if (userId != null) {
      builder.changedBy(userId);
    }
    Entity versioned = this.versioningManager.createNewVersion(builder.build());
    Instant newVersion =
        versioned
            .getVersion()
            .orElseThrow(() -> new IllegalStateException("new version of " + id + " is missing"));

    this.repository.store(versioned);
    this.referenceRepository.replace(
        id, this.referenceExtractor.extract(id, changed.getSections()));
    this.historyManager.createEntry(id, newVersion, HistoryEntryType.UPDATED);
    this.searchIndex.index(id);

    return newVersion;
  }

  private @Nullable String currentUserId() {
    Authentication auth = this.authentication.get();
    if (auth instanceof UserAuthentication user) {
      return user.user().id();
    }
    return null;
  }
}
