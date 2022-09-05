package com.sitepark.ies.contentrepository.core.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLocked;
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

public class BulkPurge {

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

	@Inject
	@SuppressWarnings("PMD.ExcessiveParameterList")
	protected BulkPurge(ContentRepository repository, EntityLockManager lockManager,
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

	public void bulkPurge(BulkPurgeInput input, BulkPurgeOutput output) {

		List<Entity> entityList = this.repository.getAll(input.getQuery());

		this.accessControl(entityList);

		try {

			this.lockEntityList(entityList, input.isForceLock());

			this.depublishEntityList(entityList);

			this.purgeEntityList(entityList);

			this.notifyExtensions(entityList);

		} finally {
			this.unlockEntityList(entityList);
		}
	}

	private void accessControl(List<Entity> entityList) {

		entityList.stream().forEach(entity -> {
			long id = entity.getId().get();
			if (!this.accessControl.isEntityRemovable(id)) {
				throw new AccessDenied("Not allowed to remove entity " + id);
			}
		});
	}

	private void lockEntityList(List<Entity> entityList, boolean forceLock) {
		entityList.stream().forEach(entity -> this.lockEntity(entity, forceLock));
	}

	private void lockEntity(Entity entity, boolean forceLock) {

		long id = entity.getId().get();

		if (forceLock) {
			this.lockManager.forceLock(id);
		} else {
			Optional<EntityLock> lock = this.lockManager.getLock(id);
			lock.ifPresent(l -> {
				throw new EntityLocked(l);
			});
		}
	}

	private void unlockEntityList(List<Entity> entityList) {
		entityList.stream().forEach(entity -> this.unlockEntity(entity));
	}

	private void unlockEntity(Entity entity) {
		long id = entity.getId().get();
		this.lockManager.unlock(id);
	}

	private void depublishEntityList(List<Entity> entityList) {

		List<Long> idList = entityList.stream()
				.map(entity -> entity.getId().get())
				.collect(Collectors.toList());

		this.publisher.depublish(idList, id -> {
			//System.out.println(id)
		});
	}

	private void purgeEntityList(List<Entity> entityList) {


		entityList.stream()
				.filter(entity -> !entity.isGroup())
				.forEach(entity -> this.purgeEntity(entity));

		entityList.stream()
				.filter(entity -> entity.isGroup())
				.forEach(entity -> this.purgeEntity(entity));
	}

	private void purgeEntity(Entity entity) {

		long id = entity.getId().get();

		this.searchIndex.remove(id);

		this.mediaReferenceManager.removeByReference(id);

		this.repository.removeEntity(id);

		this.historyManager.purge(id);

		this.versioningManager.removeAllVersions(id);

		this.recycleBin.removeByObject(id);
	}

	private void notifyExtensions(List<Entity> entityList) {

		List<Long> idList = entityList.stream()
				.map(entity -> entity.getId().get())
				.collect(Collectors.toList());

		this.extensionsNotifier.notifyBulkPurge(idList);
	}
}
