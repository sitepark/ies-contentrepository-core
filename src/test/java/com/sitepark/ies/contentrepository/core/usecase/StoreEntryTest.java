package com.sitepark.ies.contentrepository.core.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sitepark.ies.contentrepository.core.domain.entity.ChangeSet;
import com.sitepark.ies.contentrepository.core.domain.entity.Entry;
import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.entity.SectionData;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionTypeNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.service.ContentDiffer;
import com.sitepark.ies.contentrepository.core.domain.value.Reference;
import com.sitepark.ies.contentrepository.core.domain.value.ReferenceKind;
import com.sitepark.ies.contentrepository.core.domain.value.SectionType;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.IdGenerator;
import com.sitepark.ies.contentrepository.core.port.ReferenceRepository;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.SectionSchemaValidator;
import com.sitepark.ies.contentrepository.core.port.SectionTypeProvider;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.ExcessiveImports")
class StoreEntryTest {

  private static final String ID = "100560100000012345";
  private static final String LINKS = "c41b2a3c-4d5e-4f60-9a7b-8c9d0e1f2a3b";

  private SectionTypeProvider sectionTypes;
  private ReferenceRepository referenceRepository;
  private StoreEntity storeEntity;

  private static Entry entry() {
    ObjectNode data = JsonNodeFactory.instance.objectNode();
    data.put("sp_url", "ies:entity/4711");
    return Entry.builder()
        .id(ID)
        .parent("100560100000000200")
        .type("page")
        .sections(
            List.of(
                Section.builder()
                    .uuid(LINKS)
                    .type("linkList")
                    .container("sp_main_iterate")
                    .data(SectionData.of(data))
                    .build()))
        .build();
  }

  @BeforeEach
  void setUp() {
    ContentRepository repository = mock();
    AccessControl accessControl = mock();
    this.sectionTypes = mock();
    this.referenceRepository = mock();
    EntityLockManager lockManager = mock();
    VersioningManager versioningManager = mock();
    HistoryManager historyManager = mock();
    IdGenerator idGenerator = mock();
    SearchIndex searchIndex = mock();
    ContentDiffer contentDiffer = mock();
    SectionSchemaValidator schemaValidator = mock();

    when(repository.get(ID)).thenReturn(Optional.of(entry()));
    when(accessControl.isEntityWritable(ID)).thenReturn(true);
    when(lockManager.getLock(ID)).thenReturn(Optional.empty());
    ChangeSet changes = mock();
    when(changes.isEmpty()).thenReturn(false);
    when(contentDiffer.diff(any(), any())).thenReturn(changes);
    when(versioningManager.createNewVersion(any()))
        .thenAnswer(
            call ->
                ((Entry) call.getArgument(0))
                    .toBuilder().version(Instant.parse("2026-11-04T12:00:00Z")).build());
    when(this.sectionTypes.get(anyString()))
        .thenAnswer(
            call ->
                Optional.of(
                    SectionType.builder().id(call.getArgument(0)).origin("sitekit").build()));

    this.storeEntity =
        new StoreEntity(
            repository,
            lockManager,
            versioningManager,
            historyManager,
            accessControl,
            idGenerator,
            searchIndex,
            contentDiffer,
            this.sectionTypes,
            schemaValidator,
            this.referenceRepository);
  }

  @Test
  void storingAnEntryReplacesItsReferences() {
    this.storeEntity.store(entry());

    verify(this.referenceRepository)
        .replace(
            ID,
            List.of(new Reference(ID, LINKS, "/sp_url", "ies:entity/4711", ReferenceKind.LINK)));
  }

  @Test
  void storingAnEntryWithUnknownSectionTypeFails() {
    when(this.sectionTypes.get("linkList")).thenReturn(Optional.empty());
    Entry entry = entry();

    assertThatThrownBy(() -> this.storeEntity.store(entry))
        .as("a section of an unknown type should be rejected")
        .isInstanceOf(SectionTypeNotFoundException.class);
  }
}
