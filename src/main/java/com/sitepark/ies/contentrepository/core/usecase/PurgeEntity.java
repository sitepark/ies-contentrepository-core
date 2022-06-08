package com.sitepark.ies.contentrepository.core.usecase;

import java.util.Optional;

import javax.inject.Inject;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLocked;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.MediaReferenceManager;
import com.sitepark.ies.contentrepository.core.port.Publisher;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;

public final class PurgeEntity {

	private final ContentRepository repository;
	private final EntityLockManager lockManager;
	private final VersioningManager versioningManager;
	private final HistoryManager historyManager;
	private final AccessControl accessControl;
	private final RecycleBin recycleBin;
	private final SearchIndex searchIndex;
	private final MediaReferenceManager mediaReferenceManager;
	private final Publisher publisher;

	@Inject
	protected PurgeEntity(ContentRepository repository, EntityLockManager lockManager,
			VersioningManager versioningManager, HistoryManager historyManager, AccessControl accessControl,
			RecycleBin recycleBin, SearchIndex searchIndex, MediaReferenceManager mediaReferenceManager,
			Publisher publisher) {

		this.repository = repository;
		this.lockManager = lockManager;
		this.historyManager = historyManager;
		this.versioningManager = versioningManager;
		this.accessControl = accessControl;
		this.recycleBin = recycleBin;
		this.searchIndex = searchIndex;
		this.mediaReferenceManager = mediaReferenceManager;
		this.publisher = publisher;
	}

	public void purgeEntity(long id) {

		if (!this.accessControl.isEntityRemovable(id)) {
			throw new AccessDenied("Not allowed to remove entity " + id);
		}

		Optional<EntityLock> lock = this.lockManager.getLock(id);
		lock.ifPresent(l -> {
			throw new EntityLocked(l);
		});

		this.searchIndex.remove(id);

		this.publisher.depublish(id);

		this.mediaReferenceManager.removeByReference(id);

		this.repository.removeEntity(id);

		this.historyManager.purge(id);

		this.versioningManager.removeAllVersions(id);

		this.recycleBin.removeByObject(id);

	}
}
