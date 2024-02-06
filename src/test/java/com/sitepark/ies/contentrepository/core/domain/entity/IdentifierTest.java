package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@SuppressFBWarnings({
	"PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
	"NP_NULL_PARAM_DEREF_NONVIRTUAL",
	"NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class IdentifierTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(Identifier.class)
			.verify();
	}

	@Test
	void testToStringWithId() {
		Identifier identifier = Identifier.ofString("123");
		assertEquals(
				"123",
				identifier.toString(),
				"unexpected identifier-string");
	}

	@Test
	void testToStringWithNull() {
		assertThrows(NullPointerException.class, () -> {
			Identifier.ofString(null);
		});
	}

	@Test
	void testToStringWithZero() {
		assertThrows(IllegalArgumentException.class, () -> {
			Identifier.ofString("0");
		});
	}

	@Test
	void testToStringWithBlank() {
		assertThrows(IllegalArgumentException.class, () -> {
			Identifier.ofString(" ");
		});
	}

	@Test
	void testToStringWithAnchor() {
		Identifier identifier = Identifier.ofString("abc");
		assertEquals(
				"abc",
				identifier.toString(),
				"unexpected identifier-string");
	}

	@Test
	void testOfStringToId() {
		Identifier identifier = Identifier.ofString("123");
		assertEquals(Optional.of("123"), identifier.getId(), "id exprected");
	}

	@Test
	void testOfStringToAnchor() {
		Identifier identifier = Identifier.ofString("abc");
		Anchor anchor = Anchor.ofString("abc");
		assertEquals(Optional.of(anchor), identifier.getAnchor(), "anchor exprected");
	}

	@Test
	void testOfStringWithLongString() {
		Identifier identifier = Identifier.ofString("abcdefghijklmnopqrstuvwxyz");
		Anchor anchor = Anchor.ofString("abcdefghijklmnopqrstuvwxyz");
		assertEquals(Optional.of(anchor), identifier.getAnchor(), "anchor exprected");
	}

	@Test
	void testOfStringWithDot() {
		Identifier identifier = Identifier.ofString("123.b");
		Anchor anchor = Anchor.ofString("123.b");
		assertEquals(Optional.of(anchor), identifier.getAnchor(), "anchor exprected");
	}

	@Test
	void testOfId() {
		Identifier identifier = Identifier.ofId("123");
		assertEquals(Optional.of("123"), identifier.getId(), "id exprected");
	}

	@Test
	void testOfAnchor() {
		Anchor anchor = Anchor.ofString("abc");
		Identifier identifier = Identifier.ofAnchor(anchor);
		assertEquals(
				Optional.of(Anchor.ofString("abc")),
				identifier.getAnchor(),
				"anchor exprected");
	}

	@Test
	void testOfAnchorWithNull() {
		assertThrows(NullPointerException.class, () -> {
			Identifier.ofAnchor(null);
		});
	}
}
