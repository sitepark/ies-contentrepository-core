package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@SuppressFBWarnings({
	"PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
	"NP_NULL_PARAM_DEREF_NONVIRTUAL",
	"NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class EntityBulkExecutionTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(EntityBulkExecution.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(EntityBulkExecution.class)
				.withClassName(NameStyle.SIMPLE_NAME)
				.verify();
	}

	@Test
	void testSetTopic() {
		EntityBulkOperation op = mock(EntityBulkOperation.class);
		EntityBulkExecution execution = EntityBulkExecution.builder()
			.topic("topic")
			.operation(op)
			.build();

		String[] expected = new String[] {"topic"};

		assertArrayEquals(expected, execution.getTopic(), "unexpected topics");
	}

	@Test
	void testSetNullTopic() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkExecution.builder().topic((String[])null);
		}, "topic must not be null");
	}

	@Test
	void testSetNullTopicPart() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkExecution.builder().topic((String)null);
		}, "topic part must not be null");
	}

	@Test
	void testMissingTopic() {
		assertThrows(IllegalStateException.class, () -> {
			EntityBulkOperation op = mock(EntityBulkOperation.class);
			EntityBulkExecution.builder()
				.operation(op)
				.build();
		}, "topic must be set");
	}

	@Test
	void testSetOperation() {
		EntityBulkOperation op = mock(EntityBulkOperation.class);
		EntityBulkExecution execution = EntityBulkExecution.builder()
			.topic("topic")
			.operation(op)
			.build();

		List<EntityBulkOperation> expected = Arrays.asList(op);

		assertEquals(expected, execution.getOperations(), "unexpected operations");
	}

	@Test
	void testSetNullOperation() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkExecution.builder().operation((EntityBulkOperation)null);
		}, "operation must not be null");
	}

	@Test
	void testSetNullOperationArray() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkExecution.builder().operation((EntityBulkOperation[])null);
		}, "operations must not be null");
	}

	@Test
	void testMissingOperation() {
		assertThrows(IllegalStateException.class, () -> {
			EntityBulkExecution.builder()
				.topic("topic")
				.build();
		}, "operation must be set");
	}

	@Test
	void testSetFinalizer() {
		EntityBulkOperation op = mock(EntityBulkOperation.class);
		EntityBulkOperation finalizer = mock(EntityBulkOperation.class);
		EntityBulkExecution execution = EntityBulkExecution.builder()
			.topic("topic")
			.operation(op)
			.finalizer(finalizer)
			.build();

		assertEquals(
				finalizer,
				execution.getFinalizer().get(),
				"unexpected finalizer");
	}

	@Test
	void testEmptyFinalizer() {
		EntityBulkOperation op = mock(EntityBulkOperation.class);
		EntityBulkExecution execution = EntityBulkExecution.builder()
			.topic("topic")
			.operation(op)
			.build();

		assertTrue(
				execution.getFinalizer().isEmpty(),
				"finalizer optional should be empty");
	}
}
