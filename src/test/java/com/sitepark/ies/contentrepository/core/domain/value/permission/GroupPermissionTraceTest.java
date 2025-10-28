package com.sitepark.ies.contentrepository.core.domain.value.permission;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GroupPermissionTraceTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(GroupPermissionTrace.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(GroupPermissionTrace.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Test
  void testResolvedPermission() {
    GroupPermissionTrace groupPermissionTrace =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().groupId("123").build())
            .build();

    GroupPermission expected = GroupPermission.builder().groupId("123").build();

    assertEquals(
        expected,
        groupPermissionTrace.getResolvedPermission(),
        "unexpected group permission trace");
  }

  @Test
  void testSourcePermissions() {
    GroupPermissionTrace groupPermissionTrace =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().groupId("123").build())
            .sourcePermissions(
                List.of(
                    GroupPermissionSource.builder()
                        .privilegeId("123")
                        .groupPermission(GroupPermission.builder().groupId("123").build())
                        .build()))
            .build();

    List<GroupPermissionSource> expected =
        List.of(
            GroupPermissionSource.builder()
                .privilegeId("123")
                .groupPermission(GroupPermission.builder().groupId("123").build())
                .build());

    assertEquals(
        expected,
        groupPermissionTrace.getSourcePermissions(),
        "unexpected source permissions in group permission trace");
  }

  @Test
  void testToBuilder() {
    GroupPermissionTrace groupPermissionTrace =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().groupId("123").build())
            .sourcePermissions(
                List.of(
                    GroupPermissionSource.builder()
                        .privilegeId("123")
                        .groupPermission(GroupPermission.builder().groupId("123").build())
                        .build()))
            .build()
            .toBuilder()
            .resolvedPermission(GroupPermission.builder().groupId("789").build())
            .build();

    GroupPermissionTrace expected =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().groupId("789").build())
            .sourcePermissions(
                List.of(
                    GroupPermissionSource.builder()
                        .privilegeId("123")
                        .groupPermission(GroupPermission.builder().groupId("123").build())
                        .build()))
            .build();

    assertEquals(expected, groupPermissionTrace, "unexpected group permission trace");
  }

  @Test
  void testDeserialization() throws JsonProcessingException {

    String json =
        """
        {"resolvedPermission":{"groupId":"789","recursive":false,"read":false,"write":false,"hidden":false,"groups":{"create":false,"delete":false},"entries":{"create":false,"read":false,"write":false,"delete":false,"sectionGrants":[],"channels":[]},"type":"CONTENT_GROUP"},"sourcePermissions":[{"roleIds":[],"privilegeId":"123","groupPermission":{"groupId":"123","recursive":false,"read":false,"write":false,"hidden":false,"groups":{"create":false,"delete":false},"entries":{"create":false,"read":false,"write":false,"delete":false,"sectionGrants":[],"channels":[]},"type":"CONTENT_GROUP"}}]}\
        """;

    ObjectMapper mapper = new ObjectMapper();
    GroupPermissionTrace groupPermissionTrace = mapper.readValue(json, GroupPermissionTrace.class);

    GroupPermissionTrace expected =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().groupId("123").build())
            .sourcePermissions(
                List.of(
                    GroupPermissionSource.builder()
                        .privilegeId("123")
                        .groupPermission(GroupPermission.builder().groupId("123").build())
                        .build()))
            .build()
            .toBuilder()
            .resolvedPermission(GroupPermission.builder().groupId("789").build())
            .build();

    assertEquals(expected, groupPermissionTrace, "unexpected group permission trace");
  }

  @Test
  void testSerialization() throws JsonProcessingException {
    GroupPermissionTrace groupPermissionTrace =
        GroupPermissionTrace.builder()
            .resolvedPermission(GroupPermission.builder().groupId("123").build())
            .sourcePermissions(
                List.of(
                    GroupPermissionSource.builder()
                        .privilegeId("123")
                        .groupPermission(GroupPermission.builder().groupId("123").build())
                        .build()))
            .build()
            .toBuilder()
            .resolvedPermission(GroupPermission.builder().groupId("789").build())
            .build();

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(groupPermissionTrace);

    String expected =
        """
        {"resolvedPermission":{"groupId":"789","recursive":false,"read":false,"write":false,"hidden":false,"groups":{"create":false,"delete":false},"entries":{"create":false,"read":false,"write":false,"delete":false,"sectionGrants":[],"channels":[]},"type":"CONTENT_GROUP"},"sourcePermissions":[{"roleIds":[],"privilegeId":"123","groupPermission":{"groupId":"123","recursive":false,"read":false,"write":false,"hidden":false,"groups":{"create":false,"delete":false},"entries":{"create":false,"read":false,"write":false,"delete":false,"sectionGrants":[],"channels":[]},"type":"CONTENT_GROUP"}}]}\
        """;
    assertEquals(expected, json, "unexpected json");
  }
}
