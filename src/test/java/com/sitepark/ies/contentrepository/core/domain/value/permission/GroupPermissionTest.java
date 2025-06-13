package com.sitepark.ies.contentrepository.core.domain.value.permission;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GroupPermissionTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(GroupPermission.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(GroupPermission.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void testMerge() {
    GroupPermission groupPermission =
        GroupPermission.builder()
            .groupId("123")
            .recursive(false)
            .read(false)
            .write(false)
            .hidden(false)
            .groups(ContainedGroupsPermission.builder().build())
            .entries(ContainedEntriesPermission.builder().build())
            .merge(
                GroupPermission.builder()
                    .groupId("345")
                    .recursive(true)
                    .read(true)
                    .write(true)
                    .hidden(true)
                    .groups(ContainedGroupsPermission.builder().build())
                    .entries(ContainedEntriesPermission.builder().build())
                    .build())
            .build();

    GroupPermission expected =
        GroupPermission.builder()
            .groupId("123")
            .recursive(false)
            .read(true)
            .write(true)
            .hidden(true)
            .groups(ContainedGroupsPermission.builder().build())
            .entries(ContainedEntriesPermission.builder().build())
            .build();

    assertEquals(expected, groupPermission, "unexpected merged group permission");
  }

  @Test
  void testDeserialization() throws JsonProcessingException {

    String json =
        """
        {"groupId":"groupId","recursive":true,"read":true,"write":true,"hidden":true,"groups":{"create":true,"delete":true},"entries":{"create":true,"read":true,"write":true,"delete":true,"sectionGrants":["test"],"channels":[1]},"type":"CONTENT_GROUP"}""";

    ObjectMapper mapper = new ObjectMapper();
    GroupPermission groupPermission = mapper.readValue(json, GroupPermission.class);

    GroupPermission expected =
        GroupPermission.builder()
            .groupId("groupId")
            .recursive(true)
            .read(true)
            .write(true)
            .hidden(true)
            .groups(ContainedGroupsPermission.builder().create(true).delete(true).build())
            .entries(
                ContainedEntriesPermission.builder()
                    .create(true)
                    .read(true)
                    .write(true)
                    .delete(true)
                    .sectionGrant("test")
                    .channel("1")
                    .build())
            .build();
    assertEquals(expected, groupPermission, "unexpected group permission");
  }

  @Test
  void testSerialization() throws JsonProcessingException {
    GroupPermission groupPermission =
        GroupPermission.builder()
            .groupId("groupId")
            .recursive(true)
            .read(true)
            .write(true)
            .hidden(true)
            .groups(ContainedGroupsPermission.builder().create(true).delete(true).build())
            .entries(
                ContainedEntriesPermission.builder()
                    .create(true)
                    .read(true)
                    .write(true)
                    .delete(true)
                    .sectionGrant("test")
                    .channel("1")
                    .build())
            .build();

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(groupPermission);

    String expected =
        """
        {"groupId":"groupId","recursive":true,"read":true,"write":true,"hidden":true,"groups":{"create":true,"delete":true},"entries":{"create":true,"read":true,"write":true,"delete":true,"sectionGrants":["test"],"channels":["1"]},"type":"CONTENT_GROUP"}""";
    assertEquals(expected, json, "unexpected json");
  }
}
