package com.sitepark.ies.contentrepository.core.usecase;

import java.util.Optional;

import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;
import com.sitepark.ies.contentrepository.core.domain.entity.RecycleBinItem;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDenied;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFound;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.RecycleBin;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;

public class RecoverEntity {

	private final ContentRepository repository;
	private final HistoryManager historyManager;
	private final AccessControl accessControl;
	private final RecycleBin recycleBin;
	private final SearchIndex searchIndex;

	protected RecoverEntity(ContentRepository repository, HistoryManager historyManager, AccessControl accessControl,
			RecycleBin recycleBin, SearchIndex searchIndex) {
		this.repository = repository;
		this.historyManager = historyManager;
		this.accessControl = accessControl;
		this.recycleBin = recycleBin;
		this.searchIndex = searchIndex;
	}

	public void recover(Identifier identifier) {

		Optional<RecycleBinItem> recycleBinItem = this.recycleBin.get(identifier);
		recycleBinItem.orElseThrow(() -> new EntityNotFound(identifier));

		if (!this.accessControl.isGroupCreateable(recycleBinItem.get().getParent())) {
			throw new AccessDenied("Not allowed to recover entity " + recycleBinItem.get().getIdentifier()
					+ " in group " + recycleBinItem.get().getParent());
		}

		Entity entity = recycleBinItem.get().getEntity();

		this.repository.store(entity);

		this.historyManager.createEntry(identifier, System.currentTimeMillis(), HistoryEntryType.RESTORED);

		this.searchIndex.index(entity);
	}
}
