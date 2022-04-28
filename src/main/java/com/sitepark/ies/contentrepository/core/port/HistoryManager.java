package com.sitepark.ies.contentrepository.core.port;

import java.util.List;

import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntry;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.entity.Identifier;

public interface HistoryManager {
	void purge(Identifier identifier);
	HistoryEntry createEntry(Identifier identifier, long timestamp, HistoryEntryType type);
	List<HistoryEntry> getHistory(Identifier identifier);
}