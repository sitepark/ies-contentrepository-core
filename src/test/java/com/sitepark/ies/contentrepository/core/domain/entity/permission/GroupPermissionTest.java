package com.sitepark.ies.contentrepository.core.domain.entity.permission;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class GroupPermissionTest {

  @Test
  void testDeserialization() throws JsonProcessingException {

    String json =
        """
        {"groupId":"groupId","recursive":true,"read":true,"write":true,"hidden":true,"groups":{"create":true,"delete":true},"entries":{"create":true,"read":true,"write":true,"delete":true,"sectionTypes":["test"],"channels":[1]},"type":"CONTENT_GROUP"}""";

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
                    .sectionType("test")
                    .channel(1)
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
                    .sectionType("test")
                    .channel(1)
                    .build())
            .build();

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(groupPermission);

    String expected =
        """
        {"groupId":"groupId","recursive":true,"read":true,"write":true,"hidden":true,"groups":{"create":true,"delete":true},"entries":{"create":true,"read":true,"write":true,"delete":true,"sectionTypes":["test"],"channels":[1]},"type":"CONTENT_GROUP"}""";
    assertEquals(expected, json, "unexpected json");
  }
}
