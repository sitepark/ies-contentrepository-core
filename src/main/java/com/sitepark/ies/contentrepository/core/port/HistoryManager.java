package com.sitepark.ies.contentrepository.core.port;

import java.time.OffsetDateTime;
import java.util.List;

import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntry;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;

public interface HistoryManager {
	void purge(String id);
	HistoryEntry createEntry(String id, OffsetDateTime date, HistoryEntryType type);
	List<HistoryEntry> getHistory(String id);
}