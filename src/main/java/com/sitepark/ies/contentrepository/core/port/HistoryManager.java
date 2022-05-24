package com.sitepark.ies.contentrepository.core.port;

import java.util.List;

import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntry;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;

public interface HistoryManager {
	void purge(long id);
	HistoryEntry createEntry(long id, long timestamp, HistoryEntryType type);
	List<HistoryEntry> getHistory(long id);
}