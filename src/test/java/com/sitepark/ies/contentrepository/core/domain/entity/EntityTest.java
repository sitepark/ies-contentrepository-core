package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import nl.jqno.equalsverifier.EqualsVerifier;

class EntityTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(Entity.class)
			.verify();
	}

	@Test
	void testSerialize() throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());

		Entity entity = Entity.builder()
				.id(100560100000014842L)
				.name("060 Rathaus")
				.parent(100560100000014840L)
				.version(1587102861231L)
				.isGroup(false)
				.build();

		String json = mapper.writeValueAsString(entity);

		String expected = "{\"id\":100560100000014842,\"anchor\":null," +
				"\"name\":\"060 Rathaus\",\"parent\":100560100000014840," +
				"\"version\":1587102861231,\"group\":false}";

		assertEquals(expected, json, "unexpected json");
	}
}
