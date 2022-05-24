package com.sitepark.ies.contentrepository.core.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLocked;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.MediaRepository;
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
	private final MediaRepository mediaRepository;
	private final Publisher publisher;

	@Inject
	protected PurgeEntity(ContentRepository repository, EntityLockManager lockManager,
			VersioningManager versioningManager, HistoryManager historyManager, AccessControl accessControl,
			RecycleBin recycleBin, SearchIndex searchIndex, MediaRepository mediaRepository, Publisher publisher) {

		this.repository = repository;
		this.lockManager = lockManager;
		this.historyManager = historyManager;
		this.versioningManager = versioningManager;
		this.accessControl = accessControl;
		this.recycleBin = recycleBin;
		this.searchIndex = searchIndex;
		this.mediaRepository = mediaRepository;
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

		List<Long> mediaRefs = this.repository.getAllMediaReferences(id);
		this.repository.removeEntity(id);

		this.historyManager.purge(id);

		List<Long> mediaRefsFromOtherVersions = this.versioningManager.getAllMediaReferences(id);
		this.versioningManager.removeAllVersions(id);

		this.recycleBin.removeByObject(id);

		List<Long> allMediaRefs = new ArrayList<>();
		allMediaRefs.addAll(mediaRefs);
		allMediaRefs.addAll(mediaRefsFromOtherVersions);

		this.mediaRepository.remove(allMediaRefs);
	}
}
