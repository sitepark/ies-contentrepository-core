package com.sitepark.ies.contentrepository.core.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sitepark.ies.contentrepository.core.domain.entity.Entity;
import com.sitepark.ies.contentrepository.core.domain.entity.EntityLock;
import com.sitepark.ies.contentrepository.core.domain.entity.Entry;
import com.sitepark.ies.contentrepository.core.domain.entity.HistoryEntryType;
import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.entity.SectionData;
import com.sitepark.ies.contentrepository.core.domain.exception.AccessDeniedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityLockedException;
import com.sitepark.ies.contentrepository.core.domain.exception.EntityNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionDataInvalidException;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionTypeNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.exception.VersionConflictException;
import com.sitepark.ies.contentrepository.core.domain.value.Reference;
import com.sitepark.ies.contentrepository.core.domain.value.ReferenceKind;
import com.sitepark.ies.contentrepository.core.domain.value.SectionPosition;
import com.sitepark.ies.contentrepository.core.domain.value.SectionType;
import com.sitepark.ies.contentrepository.core.port.AccessControl;
import com.sitepark.ies.contentrepository.core.port.ContentRepository;
import com.sitepark.ies.contentrepository.core.port.EntityLockManager;
import com.sitepark.ies.contentrepository.core.port.HistoryManager;
import com.sitepark.ies.contentrepository.core.port.ReferenceRepository;
import com.sitepark.ies.contentrepository.core.port.SearchIndex;
import com.sitepark.ies.contentrepository.core.port.SectionSchemaValidator;
import com.sitepark.ies.contentrepository.core.port.SectionTypeProvider;
import com.sitepark.ies.contentrepository.core.port.VersioningManager;
import com.sitepark.ies.sharedkernel.security.Authentication;
import com.sitepark.ies.sharedkernel.security.User;
import com.sitepark.ies.sharedkernel.security.UserAuthentication;
import jakarta.inject.Provider;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.CouplingBetweenObjects", "PMD.ExcessiveImports"})
class SectionUseCasesTest {

  private static final String ID = "100560100000012345";
  private static final String USER = "17";
  private static final String TEXT = "8a2e0f4c-7f1d-4c6b-9d3e-2b1a0c9d8e7f";
  private static final String LINKS = "c41b2a3c-4d5e-4f60-9a7b-8c9d0e1f2a3b";
  private static final String MAIN = "sp_main_iterate";
  private static final Instant VERSION = Instant.parse("2026-10-28T09:14:02Z");
  private static final Instant NEW_VERSION = Instant.parse("2026-11-04T12:00:00Z");
  private static final Instant NOW = Instant.parse("2026-11-04T11:59:59Z");

  private ContentRepository repository;
  private EntityLockManager lockManager;
  private VersioningManager versioningManager;
  private HistoryManager historyManager;
  private AccessControl accessControl;
  private SearchIndex searchIndex;
  private ReferenceRepository referenceRepository;
  private SectionTypeProvider sectionTypes;
  private SectionSchemaValidator schemaValidator;
  private Provider<Authentication> authentication;
  private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);

  private static SectionData data(String field, String value) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put(field, value);
    return SectionData.of(node);
  }

  private static Entry entry() {
    return Entry.builder()
        .id(ID)
        .type("page")
        .version(VERSION)
        .sections(
            List.of(
                Section.builder()
                    .uuid(TEXT)
                    .type("text")
                    .container(MAIN)
                    .data(data("sp_text", "Hello"))
                    .build()))
        .build();
  }

  private static Section linkSection() {
    return Section.builder()
        .uuid(LINKS)
        .type("linkList")
        .container(MAIN)
        .data(data("sp_url", "ies:entity/4711"))
        .build();
  }

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    this.repository = mock();
    this.lockManager = mock();
    this.versioningManager = mock();
    this.historyManager = mock();
    this.accessControl = mock();
    this.searchIndex = mock();
    this.referenceRepository = mock();
    this.sectionTypes = mock();
    this.schemaValidator = mock();
    this.authentication = mock(Provider.class);

    when(this.repository.getEntry(ID)).thenReturn(Optional.of(entry()));
    when(this.lockManager.getLock(ID)).thenReturn(Optional.empty());
    when(this.accessControl.isEntityWritable(ID)).thenReturn(true);
    when(this.sectionTypes.get(anyString()))
        .thenAnswer(
            call ->
                Optional.of(
                    SectionType.builder().id(call.getArgument(0)).origin("sitekit").build()));
    when(this.versioningManager.createNewVersion(any()))
        .thenAnswer(call -> ((Entry) call.getArgument(0)).toBuilder().version(NEW_VERSION).build());

    User user = mock();
    when(user.id()).thenReturn(USER);
    UserAuthentication auth = mock();
    when(auth.user()).thenReturn(user);
    when(this.authentication.get()).thenReturn(auth);
  }

  private AddSection addSection() {
    return new AddSection(
        this.repository,
        this.lockManager,
        this.versioningManager,
        this.historyManager,
        this.accessControl,
        this.searchIndex,
        this.referenceRepository,
        this.sectionTypes,
        this.schemaValidator,
        this.authentication,
        this.clock);
  }

  private UpdateSection updateSection() {
    return new UpdateSection(
        this.repository,
        this.lockManager,
        this.versioningManager,
        this.historyManager,
        this.accessControl,
        this.searchIndex,
        this.referenceRepository,
        this.sectionTypes,
        this.schemaValidator,
        this.authentication,
        this.clock);
  }

  private RemoveSection removeSection() {
    return new RemoveSection(
        this.repository,
        this.lockManager,
        this.versioningManager,
        this.historyManager,
        this.accessControl,
        this.searchIndex,
        this.referenceRepository,
        this.sectionTypes,
        this.schemaValidator,
        this.authentication,
        this.clock);
  }

  private MoveSection moveSection() {
    return new MoveSection(
        this.repository,
        this.lockManager,
        this.versioningManager,
        this.historyManager,
        this.accessControl,
        this.searchIndex,
        this.referenceRepository,
        this.sectionTypes,
        this.schemaValidator,
        this.authentication,
        this.clock);
  }

  private Entry storedEntry() {
    ArgumentCaptor<Entity> stored = ArgumentCaptor.forClass(Entity.class);
    verify(this.repository).store(stored.capture());
    return (Entry) stored.getValue();
  }

  @Test
  void addReturnsTheNewVersion() {
    Instant version = this.addSection().add(ID, VERSION, linkSection(), 1);

    assertThat(version)
        .as("the new version of the entry should be returned")
        .isEqualTo(NEW_VERSION);
  }

  @Test
  void addStoresTheEntryWithTheNewSection() {
    this.addSection().add(ID, VERSION, linkSection(), 1);

    assertThat(this.storedEntry().getSections())
        .as("the new section should follow the text section")
        .extracting(Section::getUuid)
        .containsExactly(TEXT, LINKS);
  }

  @Test
  void addRecordsWhoChangedTheEntry() {
    this.addSection().add(ID, VERSION, linkSection(), 1);

    assertThat(this.storedEntry().getChangedBy())
        .as("the current user should be recorded")
        .contains(USER);
  }

  @Test
  void addRecordsWhenTheEntryWasChanged() {
    this.addSection().add(ID, VERSION, linkSection(), 1);

    assertThat(this.storedEntry().getChanged())
        .as("the time of the change should be recorded")
        .contains(NOW);
  }

  @Test
  void addReplacesTheReferencesOfTheEntry() {
    this.addSection().add(ID, VERSION, linkSection(), 1);

    verify(this.referenceRepository)
        .replace(
            ID,
            List.of(new Reference(ID, LINKS, "/sp_url", "ies:entity/4711", ReferenceKind.LINK)));
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts") // history and index belong together
  void addRecordsHistoryAndIndexesTheEntry() {
    this.addSection().add(ID, VERSION, linkSection(), 1);

    verify(this.historyManager).createEntry(ID, NEW_VERSION, HistoryEntryType.UPDATED);
    verify(this.searchIndex).index(ID);
  }

  @Test
  void changeOfAnotherVersionFails() {
    Instant outdated = VERSION.minusSeconds(60);
    assertThatThrownBy(() -> this.addSection().add(ID, outdated, linkSection(), 1))
        .as("a change based on another version should be rejected")
        .isInstanceOf(VersionConflictException.class);
  }

  @Test
  void changeOfUnknownEntryFails() {
    when(this.repository.getEntry(ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, linkSection(), 1))
        .as("a change of an unknown entry should be rejected")
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void changeOfEntryLockedByAnotherUserFails() {
    EntityLock lock = EntityLock.builder().entity(ID).user("99").ttl(60).build();
    when(this.lockManager.getLock(ID)).thenReturn(Optional.of(lock));

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, linkSection(), 1))
        .as("an entry locked by another user should not be changed")
        .isInstanceOf(EntityLockedException.class);
  }

  @Test
  void changeOfEntryLockedByCurrentUserSucceeds() {
    EntityLock lock = EntityLock.builder().entity(ID).user(USER).ttl(60).build();
    when(this.lockManager.getLock(ID)).thenReturn(Optional.of(lock));

    Instant version = this.addSection().add(ID, VERSION, linkSection(), 1);

    assertThat(version).as("the user holding the lock may change the entry").isEqualTo(NEW_VERSION);
  }

  @Test
  void changeWithoutWriteAccessFails() {
    when(this.accessControl.isEntityWritable(ID)).thenReturn(false);

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, linkSection(), 1))
        .as("a change without write access should be rejected")
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void changeWithoutGrantOfTheSectionTypeFails() {
    when(this.sectionTypes.get("linkList"))
        .thenReturn(
            Optional.of(
                SectionType.builder().id("linkList").origin("sitekit").grant("links").build()));
    when(this.accessControl.isSectionGrantGranted(ID, "links")).thenReturn(false);

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, linkSection(), 1))
        .as("a section whose grant is missing should not be added")
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void changeWithUnknownSectionTypeFails() {
    when(this.sectionTypes.get("linkList")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, linkSection(), 1))
        .as("a section of an unknown type should be rejected")
        .isInstanceOf(SectionTypeNotFoundException.class);
  }

  @Test
  void changeWithDataViolatingTheSchemaFails() {
    JsonNode schema = JsonNodeFactory.instance.objectNode();
    when(this.sectionTypes.get("text"))
        .thenReturn(
            Optional.of(SectionType.builder().id("text").origin("sitekit").schema(schema).build()));
    when(this.schemaValidator.validate(any(), any())).thenReturn(List.of("sp_text: required"));

    assertThatThrownBy(() -> this.updateSection().update(ID, VERSION, TEXT, SectionData.empty()))
        .as("data violating the schema should be rejected")
        .isInstanceOf(SectionDataInvalidException.class);
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts") // the failure and its effect
  void failedChangeStoresNothing() {
    when(this.accessControl.isEntityWritable(ID)).thenReturn(false);

    assertThatThrownBy(() -> this.removeSection().remove(ID, VERSION, TEXT))
        .isInstanceOf(AccessDeniedException.class);
    verify(this.repository, never()).store(any());
  }

  @Test
  void updateReplacesTheDataOfTheSection() {
    this.updateSection().update(ID, VERSION, TEXT, data("sp_text", "Changed"));

    assertThat(this.storedEntry().getSection(TEXT).map(Section::getData))
        .as("the section should have the new data")
        .contains(data("sp_text", "Changed"));
  }

  @Test
  void updateOfUnknownSectionFails() {
    SectionData empty = SectionData.empty();
    assertThatThrownBy(() -> this.updateSection().update(ID, VERSION, LINKS, empty))
        .as("updating an unknown section should be rejected")
        .isInstanceOf(SectionNotFoundException.class);
  }

  @Test
  void removeDropsTheSection() {
    this.removeSection().remove(ID, VERSION, TEXT);

    assertThat(this.storedEntry().getSections()).as("the section should be removed").isEmpty();
  }

  @Test
  void moveChangesTheContainer() {
    this.moveSection().move(ID, VERSION, TEXT, "sp_link_column_iterate", 0);

    assertThat(this.storedEntry().getSection(TEXT).flatMap(Section::getContainer))
        .as("the section should belong to the target container")
        .contains("sp_link_column_iterate");
  }

  private static final String SECTION = "5b6c7d8e-9f01-4a2b-8c3d-4e5f60718293";

  /** The stored entry gets a section of type newSection holding the text section. */
  private void storeNestedEntry() {
    Section text = entry().getSections().get(0).toBuilder().container("items").build();
    Section newSection =
        Section.builder()
            .uuid(SECTION)
            .type("newSection")
            .container(MAIN)
            .children("items", List.of(text))
            .build();
    when(this.repository.getEntry(ID))
        .thenReturn(Optional.of(entry().toBuilder().sections(List.of(newSection)).build()));
  }

  private void requireGrant(String sectionType) {
    when(this.sectionTypes.get(sectionType))
        .thenReturn(
            Optional.of(
                SectionType.builder().id(sectionType).origin("sitekit").grant("g").build()));
    when(this.accessControl.isSectionGrantGranted(ID, "g")).thenReturn(false);
  }

  @Test
  void addPutsTheSectionIntoTheContainerOfAParent() {
    this.storeNestedEntry();

    this.addSection().add(ID, VERSION, linkSection(), SectionPosition.within(SECTION, "items", 1));

    assertThat(this.storedEntry().getSection(SECTION).orElseThrow().getChildren("items"))
        .as("the link section should be the second child of the parent")
        .extracting(Section::getUuid)
        .containsExactly(TEXT, LINKS);
  }

  @Test
  void addBelowAParentWithoutItsGrantFails() {
    this.storeNestedEntry();
    this.requireGrant("newSection");
    SectionPosition position = SectionPosition.within(SECTION, "items", 0);

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, linkSection(), position))
        .as("adding below a section whose grant is missing should be rejected")
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void addChecksTheSectionsBelowTheAddedSection() {
    this.requireGrant("text");
    Section parent =
        Section.builder()
            .uuid(SECTION)
            .type("newSection")
            .container(MAIN)
            .children(
                "items",
                List.of(Section.builder().uuid(LINKS).type("text").container("items").build()))
            .build();

    assertThatThrownBy(() -> this.addSection().add(ID, VERSION, parent, 1))
        .as("a child whose grant is missing should not be added")
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void moveIntoAParentWithoutItsGrantFails() {
    this.storeNestedEntry();
    this.requireGrant("newSection");
    SectionPosition position = SectionPosition.within(SECTION, "items", 0);

    assertThatThrownBy(() -> this.moveSection().move(ID, VERSION, TEXT, position))
        .as("moving below a section whose grant is missing should be rejected")
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void removeOfAParentWithoutTheGrantOfAChildFails() {
    this.storeNestedEntry();
    this.requireGrant("text");

    assertThatThrownBy(() -> this.removeSection().remove(ID, VERSION, SECTION))
        .as("removing a section whose child may not be changed should be rejected")
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  void moveOfUnknownSectionFails() {
    assertThatThrownBy(() -> this.moveSection().move(ID, VERSION, LINKS, MAIN, 0))
        .as("moving an unknown section should be rejected")
        .isInstanceOf(SectionNotFoundException.class);
  }
}
