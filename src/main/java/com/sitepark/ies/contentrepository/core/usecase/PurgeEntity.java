package com.sitepark.ies.contentrepository.core.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;
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

	public void purge(@NonNull Identifier identifier) {

		if (identifier == null) {
			throw new IllegalArgumentException("identifier is null");
		}

		if (!this.accessControl.isEntityRemovable(identifier)) {
			throw new AccessDenied("Not allowed to remove entity " + identifier);
		}

		Optional<EntityLock> lock = this.lockManager.getLock(identifier);
		lock.ifPresent(l -> {
			throw new EntityLocked(l);
		});

		this.searchIndex.remove(identifier);

		this.publisher.depublish(identifier);

		List<Long> mediaRefs = this.repository.getAllMediaReferences(identifier);
		this.repository.remove(identifier);

		this.historyManager.purge(identifier);

		List<Long> mediaRefsFromOtherVersions = this.versioningManager.getAllMediaReferences(identifier);
		this.versioningManager.removeAllVersions(identifier);

		this.recycleBin.remove(identifier);

		List<Long> allMediaRefs = new ArrayList<>();
		allMediaRefs.addAll(mediaRefs);
		allMediaRefs.addAll(mediaRefsFromOtherVersions);

		this.mediaRepository.remove(allMediaRefs);
	}
}
