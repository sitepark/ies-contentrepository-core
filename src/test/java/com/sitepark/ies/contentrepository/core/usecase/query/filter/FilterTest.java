package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import static com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class FilterTest {

  @Test
  void testIsGroup() {
    IsGroup filter = Filter.isGroup(true);
    assertTrue(filter.isGroup(), "unexpected isGroup");
  }

  @Test
  void testName() {
    Name filter = Filter.name("test");
    assertEquals("test", filter.getName(), "unexpected name");
  }

  @Test
  void testId() {
    Id filter = Filter.id("123");
    assertEquals("123", filter.getId(), "unexpected id");
  }

  @Test
  void testIdList() {
    IdList filter = Filter.idList("123");
    assertEquals(List.of("123"), filter.getIdList(), "unexpected idList");
  }

  @Test
  void testAnchor() {
    com.sitepark.ies.sharedkernel.anchor.Anchor anchor =
        com.sitepark.ies.sharedkernel.anchor.Anchor.ofString("abc");
    Anchor filter = Filter.anchor(anchor);
    assertEquals(anchor, filter.getAnchor(), "unexpected anchorList");
  }

  @Test
  void testAnchorList() {
    com.sitepark.ies.sharedkernel.anchor.Anchor anchor =
        com.sitepark.ies.sharedkernel.anchor.Anchor.ofString("abc");
    AnchorList filter = Filter.anchorList(anchor);
    assertEquals(
        Collections.singletonList(anchor), filter.getAnchorList(), "unexpected anchorList");
  }

  @Test
  void testParent() {
    Parent filter = Filter.parent("123");
    assertEquals("123", filter.getParent(), "unexpected parent");
  }

  @Test
  void testParentList() {
    ParentList filter = Filter.parentList("123");
    assertEquals(List.of("123"), filter.getParentList(), "unexpected parentList");
  }

  @Test
  void testParentAnchor() {
    com.sitepark.ies.sharedkernel.anchor.Anchor anchor =
        com.sitepark.ies.sharedkernel.anchor.Anchor.ofString("abc");
    ParentAnchor filter = Filter.parentAnchor(anchor);
    assertEquals(anchor, filter.getParentAnchor(), "unexpected parentAnchor");
  }

  @Test
  void testParentAnchorList() {
    com.sitepark.ies.sharedkernel.anchor.Anchor anchor =
        com.sitepark.ies.sharedkernel.anchor.Anchor.ofString("abc");
    ParentAnchorList filter = Filter.parentAnchorList(anchor);
    assertEquals(
        Collections.singletonList(anchor),
        filter.getParentAnchorList(),
        "unexpected parentAnchorList");
  }

  @Test
  void testRoot() {
    Root filter = Filter.root("123");
    assertEquals("123", filter.getRoot(), "unexpected root");
  }

  @Test
  void testRootList() {
    RootList filter = Filter.rootList("123");
    assertEquals(List.of("123"), filter.getRootList(), "unexpected rootList");
  }

  @Test
  void testOr() {
    Filter a = mock();
    Filter b = mock();
    Or filter = Filter.or(a, b);
    assertEquals(Arrays.asList(a, b), filter.getOr(), "unexpected or");
  }

  @Test
  void testAnd() {
    Filter a = mock();
    Filter b = mock();
    And filter = Filter.and(a, b);
    assertEquals(Arrays.asList(a, b), filter.getAnd(), "unexpected and");
  }

  @Test
  void testNot() {
    Filter a = mock();
    Not filter = Filter.not(a);
    assertEquals(a, filter.getNot(), "unexpected not");
  }

  @Test
  void testSerialize() throws Exception {

    Filter filter =
        or(
            idList("6"),
            anchor(com.sitepark.ies.sharedkernel.anchor.Anchor.ofString("abc")),
            and(root("5"), parent("7"), not(parent("9"))));

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(filter);

    assertEquals(
        "{\"or\":["
            + "{\"idList\":[\"6\"]},"
            + "{\"anchor\":\"abc\"},"
            + "{\"and\":["
            + "{\"root\":\"5\"},"
            + "{\"parent\":\"7\"},"
            + "{\"not\":{\"parent\":\"9\"}}"
            + "]}"
            + "]}",
        json,
        "unexpected json-data");
  }

  // Deserialization tests are in infrastructure module
  // since that's where the actual deserializer implementation lives
}
