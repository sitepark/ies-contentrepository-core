package com.sitepark.ies.contentrepository.core.domain.value.permission;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.List;
import java.util.Set;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GroupPermissionSourceTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(GroupPermissionSource.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(GroupPermissionSource.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Test
  void testRoleIdId() {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .roleId("123")
            .privilegeId("123")
            .groupPermission(GroupPermission.builder().build())
            .build();
    assertEquals(Set.of("123"), groupPermissionSource.getRoleIds(), "unexpected role ids");
  }

  @Test
  void testRoleIdsWithCollection() {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .roleIds(List.of("123", "345"))
            .privilegeId("123")
            .groupPermission(GroupPermission.builder().build())
            .build();
    assertEquals(Set.of("123", "345"), groupPermissionSource.getRoleIds(), "unexpected role ids");
  }

  @Test
  void testRoleIdsWithArray() {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .roleIds("123", "345")
            .privilegeId("123")
            .groupPermission(GroupPermission.builder().build())
            .build();
    assertEquals(Set.of("123", "345"), groupPermissionSource.getRoleIds(), "unexpected role ids");
  }

  @Test
  void testPrivilegeId() {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .privilegeId("123")
            .groupPermission(GroupPermission.builder().build())
            .build();
    assertEquals("123", groupPermissionSource.getPrivilegeId(), "unexpected role ids");
  }

  @Test
  void testGroupPermission() {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .privilegeId("123")
            .groupPermission(GroupPermission.builder().read(true).build())
            .build();
    assertEquals(
        GroupPermission.builder().read(true).build(),
        groupPermissionSource.getGroupPermission(),
        "unexpected read permission");
  }

  @Test
  void testToBuilder() {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .roleIds("123", "345")
            .privilegeId("123")
            .groupPermission(GroupPermission.builder().read(true).build())
            .build()
            .toBuilder()
            .privilegeId("789")
            .build();

    GroupPermissionSource expected =
        GroupPermissionSource.builder()
            .roleIds("123", "345")
            .privilegeId("789")
            .groupPermission(GroupPermission.builder().read(true).build())
            .build();
    assertEquals(expected, groupPermissionSource, "unexpected result of toBuilder");
  }

  @Test
  void testDeserialization() throws JsonProcessingException {

    String json =
        """
        {"roleIds":["123"],"privilegeId":"345","groupPermission":{"groupId":"456","recursive":false,"read":true,"write":false,"hidden":false,"groups":{"create":false,"delete":false},"entries":{"create":false,"read":false,"write":false,"delete":false,"sectionGrants":[],"channels":[]},"type":"CONTENT_GROUP"}}""";

    ObjectMapper mapper = new ObjectMapper();
    GroupPermissionSource groupPermissionSource =
        mapper.readValue(json, GroupPermissionSource.class);

    GroupPermissionSource expected =
        GroupPermissionSource.builder()
            .roleId("123")
            .privilegeId("345")
            .groupPermission(GroupPermission.builder().groupId("456").read(true).build())
            .build();

    assertEquals(expected, groupPermissionSource, "unexpected group permission source");
  }

  @Test
  void testSerialization() throws JsonProcessingException {
    GroupPermissionSource groupPermissionSource =
        GroupPermissionSource.builder()
            .roleId("123")
            .privilegeId("345")
            .groupPermission(GroupPermission.builder().groupId("456").read(true).build())
            .build();

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(groupPermissionSource);

    String expected =
        """
        {"roleIds":["123"],"privilegeId":"345","groupPermission":{"groupId":"456","recursive":false,"read":true,"write":false,"hidden":false,"groups":{"create":false,"delete":false},"entries":{"create":false,"read":false,"write":false,"delete":false,"sectionGrants":[],"channels":[]},"type":"CONTENT_GROUP"}}""";
    assertEquals(expected, json, "unexpected json");
  }
}
