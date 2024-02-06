package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class RecycleBinItemTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		RecycleBinItem a = RecycleBinItem.builder().id("1").build();
		RecycleBinItem b = RecycleBinItem.builder().id("2").build();
		EqualsVerifier.forClass(RecycleBinItem.class)
			.withPrefabValues(RecycleBinItem.class, a, b)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(RecycleBinItem.class)
				.withClassName(NameStyle.SIMPLE_NAME)
				.verify();
	}

	@Test
	void testSetId() {
		RecycleBinItem item = RecycleBinItem.builder()
			.id("123")
			.build();

		assertEquals("123", item.getId(), "unexpected id");
	}

	@Test
	void testSetParent() {
		RecycleBinItem item = RecycleBinItem.builder()
			.parent("123")
			.build();

		assertEquals("123", item.getParent(), "unexpected parent");
	}

	@Test
	void testSetEntity() {
		Entity entity = mock();
		RecycleBinItem item = RecycleBinItem.builder()
			.entity(entity)
			.build();

		assertEquals(entity, item.getEntity(), "unexpected entity");
	}

	@Test
	void testSetChild() {
		RecycleBinItem child = mock(RecycleBinItem.class);
		RecycleBinItem item = RecycleBinItem.builder()
			.child(child)
			.build();
		List<RecycleBinItem> expected = Arrays.asList(child);
		assertEquals(expected, item.getChildren(), "unexpected children");
	}

	@Test
	void testSetNullChild() {
		assertThrows(NullPointerException.class, () -> {
			RecycleBinItem.builder().child(null);
		}, "child must not be null");
	}

	@Test
	void testSetChildren() {
		List<RecycleBinItem> children = Arrays.asList(mock(RecycleBinItem.class));
		RecycleBinItem item = RecycleBinItem.builder()
			.children(children)
			.build();
		assertEquals(children, item.getChildren(), "unexpected children");
	}

	@Test
	void testSetNullChildren() {
		assertThrows(NullPointerException.class, () -> {
			RecycleBinItem.builder().children(null);
		}, "children must not be null");
	}

	@Test
	void testToBuilder() {
		Entity entity = mock();
		List<RecycleBinItem> children = Arrays.asList(mock(RecycleBinItem.class));
		RecycleBinItem item = RecycleBinItem.builder()
			.id("123")
			.parent("345")
			.entity(entity)
			.children(children)
			.build();

		RecycleBinItem copy = item.toBuilder()
				.parent("678")
				.build();

		RecycleBinItem expected = RecycleBinItem.builder()
				.id("123")
				.parent("678")
				.entity(entity)
				.children(children)
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}
}
