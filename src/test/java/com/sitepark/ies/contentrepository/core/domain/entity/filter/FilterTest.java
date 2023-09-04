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

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitepark.ies.contentrepository.core.domain.databind.DatabindModule;

class FilterTest {

	@Test
	void testSerialize() throws Exception {

		Filter filter =
				or(
					idList(6L),
					anchor(com.sitepark.ies.contentrepository.core.domain.entity.Anchor.ofString("abc")),
					and(
						root(5L),
						parent(7L),
						not(parent(9L))
					)
				);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(filter);

		assertEquals(
				"{\"or\":[" +
						"{\"idList\":[6]}," +
						"{\"anchor\":\"abc\"}," +
						"{\"and\":[" +
								"{\"root\":5}," +
								"{\"parent\":7}," +
								"{\"not\":{\"parent\":9}}" +
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
				"{\"idList\":[6]}," +
				"{\"anchor\":\"abc\"}," +
				"{\"and\":[" +
					"{\"root\":5}," +
					"{\"parent\":7}," +
					"{\"not\":{\"parent\":9}}" +
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
		assertEquals(5L, ((Root)and.getAnd().get(0)).getRoot(), "wront root");
		assertInstanceOf(Parent.class, and.getAnd().get(1));
		assertEquals(7L, ((Parent)and.getAnd().get(1)).getParent(), "wront parent");
		assertInstanceOf(Not.class, and.getAnd().get(2));

		Not not = (Not)and.getAnd().get(2);

		assertInstanceOf(Parent.class, not.getNot());
		assertEquals(9L, ((Parent)not.getNot()).getParent(), "wront parent (in not)");
	}
}
