package com.sitepark.ies.contentrepository.core.domain.entity.filter;

import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.anchor;
import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.and;
import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.idList;
import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.not;
import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.or;
import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.parent;
import static com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter.root;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitepark.ies.contentrepository.core.domain.databind.DatabindModule;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class FilterTest {

	@Test
	void testIsGroup() {
		IsGroup filter = Filter.isGroup(true);
		assertTrue(filter.isGroup(), "unexpected isGroup");
	}

	@Test
	void testId() {
		Id filter = Filter.id("123");
		assertEquals("123", filter.getId(), "unexpected id");
	}

	@Test
	void testIdList() {
		IdList filter = Filter.idList("123");
		assertEquals(Arrays.asList("123"), filter.getIdList(), "unexpected idList");
	}

	@Test
	void testAnchor() {
		com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor =
				com.sitepark.ies.contentrepository.core.domain.entity.Anchor.ofString("abc");
		Anchor filter = Filter.anchor(anchor);
		assertEquals(
				anchor,
				filter.getAnchor(),
				"unexpected anchorList");
	}

	@Test
	void testAnchorList() {
		com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor =
				com.sitepark.ies.contentrepository.core.domain.entity.Anchor.ofString("abc");
		AnchorList filter = Filter.anchorList(anchor);
		assertEquals(
				Arrays.asList(anchor),
				filter.getAnchorList(),
				"unexpected anchorList");
	}

	@Test
	void testParent() {
		Parent filter = Filter.parent("123");
		assertEquals("123", filter.getParent(), "unexpected parent");
	}

	@Test
	void testParentList() {
		ParentList filter = Filter.parentList("123");
		assertEquals(Arrays.asList("123"), filter.getParentList(), "unexpected parentList");
	}

	@Test
	void testParentAnchor() {
		com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor =
				com.sitepark.ies.contentrepository.core.domain.entity.Anchor.ofString("abc");
		ParentAnchor filter = Filter.parentAnchor(anchor);
		assertEquals(
				anchor,
				filter.getParentAnchor(),
				"unexpected parentAnchor");
	}

	@Test
	void testParentAnchorList() {
		com.sitepark.ies.contentrepository.core.domain.entity.Anchor anchor =
				com.sitepark.ies.contentrepository.core.domain.entity.Anchor.ofString("abc");
		ParentAnchorList filter = Filter.parentAnchorList(anchor);
		assertEquals(
				Arrays.asList(anchor),
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
		assertEquals(Arrays.asList("123"), filter.getRootList(), "unexpected rootList");
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
					anchor(com.sitepark.ies.contentrepository.core.domain.entity.Anchor.ofString("abc")),
					and(
						root("5"),
						parent("7"),
						not(parent("9"))
					)
				);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(filter);

		assertEquals(
				"{\"or\":[" +
						"{\"idList\":[\"6\"]}," +
						"{\"anchor\":\"abc\"}," +
						"{\"and\":[" +
								"{\"root\":\"5\"}," +
								"{\"parent\":\"7\"}," +
								"{\"not\":{\"parent\":\"9\"}}" +
						"]}" +
				"]}",
				json,
				"unexpected json-data");

	}

	@Test
	@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
	void testDeserialize() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new DatabindModule());

		String json = "{\"or\":[" +
				"{\"idList\":[\"6\"]}," +
				"{\"anchor\":\"abc\"}," +
				"{\"and\":[" +
					"{\"root\":\"5\"}," +
					"{\"parent\":\"7\"}," +
					"{\"not\":{\"parent\":\"9\"}}" +
				"]" +
		"}]}";

		Filter filter = objectMapper.readValue(json, Filter.class);

		assertInstanceOf(Or.class, filter);

		Or or = (Or)filter;

		assertInstanceOf(IdList.class, or.getOr().get(0));
		assertInstanceOf(Anchor.class, or.getOr().get(1));
		assertEquals("abc", ((Anchor)(or.getOr().get(1))).getAnchor().getName(), "wront root");
		assertInstanceOf(And.class, or.getOr().get(2));

		And and = (And)or.getOr().get(2);

		assertInstanceOf(Root.class, and.getAnd().get(0));
		assertEquals("5", ((Root)and.getAnd().get(0)).getRoot(), "wront root");
		assertInstanceOf(Parent.class, and.getAnd().get(1));
		assertEquals("7", ((Parent)and.getAnd().get(1)).getParent(), "wront parent");
		assertInstanceOf(Not.class, and.getAnd().get(2));

		Not not = (Not)and.getAnd().get(2);

		assertInstanceOf(Parent.class, not.getNot());
		assertEquals("9", ((Parent)not.getNot()).getParent(), "wront parent (in not)");
	}
}
