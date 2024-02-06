package com.sitepark.ies.contentrepository.core.usecase;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.GroupNotEmptyException;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.ExtensionsNotifier;
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

	private final ExtensionsNotifier extensionsNotifier;

	private static Logger LOGGER = LogManager.getLogger();

	@Inject
	@SuppressWarnings("PMD.ExcessiveParameterList")
	protected PurgeEntity(ContentRepository repository, EntityLockManager lockManager,
			VersioningManager versioningManager, HistoryManager historyManager, AccessControl accessControl,
			RecycleBin recycleBin, SearchIndex searchIndex, MediaReferenceManager mediaReferenceManager,
			Publisher publisher, ExtensionsNotifier extensionsNotifier) {

		this.repository = repository;
		this.lockManager = lockManager;
		this.historyManager = historyManager;
		this.versioningManager = versioningManager;
		this.accessControl = accessControl;
		this.recycleBin = recycleBin;
		this.searchIndex = searchIndex;
		this.mediaReferenceManager = mediaReferenceManager;
		this.publisher = publisher;
		this.extensionsNotifier = extensionsNotifier;
	}

	public void purgeEntity(String id) {

		if (!this.accessControl.isEntityRemovable(id)) {
			throw new AccessDeniedException("Not allowed to remove entity " + id);
		}

		if (this.repository.isGroup(id) && !this.repository.isEmptyGroup(id)) {
			throw new GroupNotEmptyException(id);
		}

		try {
			this.lockManager.lock(id);

			this.publisher.depublish(id);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("purge: {}", this.repository.get(id));
			}

			this.searchIndex.remove(id);

			this.mediaReferenceManager.removeByReference(id);

			this.repository.removeEntity(id);

			this.historyManager.purge(id);

			this.versioningManager.removeAllVersions(id);

			this.recycleBin.removeByObject(id);

			this.extensionsNotifier.notifyPurge(id);

		} finally {
			this.lockManager.unlock(id);
		}
	}
}
