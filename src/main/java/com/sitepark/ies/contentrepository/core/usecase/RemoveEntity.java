package com.sitepark.ies.contentrepository.core.usecase;

import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;
import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLocked;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;

public final class RemoveEntity {

	private final ContentRepository repository;
	private final EntityLockManager lockManager;
	private final HistoryManager historyManager;
	private final AccessControl accessControl;
	private final RecycleBin recycleBin;
	private final SearchIndex searchIndex;

	protected RemoveEntity(ContentRepository repository, EntityLockManager lockManager,
			HistoryManager historyManager, AccessControl accessControl, RecycleBin recycleBin,
			SearchIndex searchIndex) {
		this.repository = repository;
		this.lockManager = lockManager;
		this.historyManager = historyManager;
		this.accessControl = accessControl;
		this.recycleBin = recycleBin;
		this.searchIndex = searchIndex;
	}

	public Optional<Entity> remove(Identifier identifier) {

		if (!this.accessControl.isEntityRemovable(identifier)) {
			throw new AccessDenied("Not allowed to remove entity " + identifier);
		}

		Optional<Entity> entity = this.repository.get(identifier);
		if (entity.isEmpty()) {
			return entity;
		}

		Optional<EntityLock> lock = this.lockManager.getLock(identifier);
		lock.ifPresent(l -> {
			throw new EntityLocked(l);
		});

		this.repository.remove(identifier);

		this.historyManager.createEntry(identifier, System.currentTimeMillis(), HistoryEntryType.REMOVED);

		RecycleBinItem recycleBinItem = RecycleBinItem.builder().build();
		this.recycleBin.add(recycleBinItem);

		this.searchIndex.remove(identifier);

		return entity;
	}
}
