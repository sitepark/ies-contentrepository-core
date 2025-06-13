package com.sitepark.ies.contentrepository.core.domain.value.permission;

import static org.junit.jupiter.api.Assertions.*;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.List;
import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ContainedEntriesPermissionTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(ContainedEntriesPermission.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(ContainedEntriesPermission.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Test
  void testSetCreate() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().create(true).build();

    assertTrue(permission.isCreate(), "create permission should be true");
  }

  @Test
  void testSetRead() {
    ContainedEntriesPermission permission = ContainedEntriesPermission.builder().read(true).build();

    assertTrue(permission.isRead(), "read permission should be true");
  }

  @Test
  void testSetWrite() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().write(true).build();

    assertTrue(permission.isWrite(), "write permission should be true");
  }

  @Test
  void testSetDelete() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().delete(true).build();

    assertTrue(permission.isDelete(), "delete permission should be true");
  }

  @Test
  void testSetSectionGrant() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().sectionGrant("youtube").build();

    assertEquals(Set.of("youtube"), permission.getSectionGrants(), "unexpected section grants");
  }

  @Test
  void testSetSectionGrantsAsList() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().sectionGrants(List.of("youtube", "code")).build();

    assertEquals(
        Set.of("youtube", "code"), permission.getSectionGrants(), "unexpected section grants");
  }

  @Test
  void testSetSectionGrantsAsArray() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().sectionGrants("youtube", "code").build();

    assertEquals(
        Set.of("youtube", "code"), permission.getSectionGrants(), "unexpected section grants");
  }

  @Test
  void testSetChannel() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().channel("12").build();

    assertEquals(Set.of("12"), permission.getChannels(), "unexpected section grants");
  }

  @Test
  void testSetChannelsAsList() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().channels(List.of("12", "34")).build();

    assertEquals(Set.of("12", "34"), permission.getChannels(), "unexpected section grants");
  }

  @Test
  void testSetChannelsAsArray() {
    ContainedEntriesPermission permission =
        ContainedEntriesPermission.builder().channels("12", "34").build();

    assertEquals(Set.of("12", "34"), permission.getChannels(), "unexpected section grants");
  }
}
