package io.bhavik.genre.model;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class GenreTest {

	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testIdIsNullOnCreate() {
		Genre genre = new Genre();
		genre.setId(UUID.randomUUID());
		genre.setName("test");

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnCreate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("id", violations.getPropertyPath().toString());
		assertEquals("must be null", violations.getMessage());
	}

	@Test
	public void testIdIsNullOnUpdate() {
		ZonedDateTime now = ZonedDateTime.now();

		Genre genre = new Genre();
		genre.setId(UUID.randomUUID());
		genre.setName("test");
		genre.setCreatedDateTime(now);
		genre.setUpdatedDateTime(now);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnUpdate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("id", violations.getPropertyPath().toString());
		assertEquals("must be null", violations.getMessage());
	}

	@Test
	public void testNameIsNotNullOnCreate() {
		Genre genre = new Genre();
		genre.setName(null);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnCreate.class);

		assertEquals(1, constraintViolations.size());

		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("name", violations.getPropertyPath().toString());
		assertEquals("must not be null", violations.getMessage());
	}

	@Test
	public void testNameIsNotOnUpdate() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setName(null);
		genre.setCreatedDateTime(now);
		genre.setUpdatedDateTime(now);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnUpdate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("name", violations.getPropertyPath().toString());
		assertEquals("must not be null", violations.getMessage());
	}

	@Test
	public void testNameIsNotEmptyOnCreate() {
		Genre genre = new Genre();
		genre.setName("");

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnCreate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("name", violations.getPropertyPath().toString());
		assertEquals("size must be between 1 and 20", violations.getMessage());
	}

	@Test
	public void testNameLengthLessThanTwentyOnCreate() {
		Genre genre = new Genre();
		genre.setName(StringUtils.repeat("*", 21));

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnCreate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("name", violations.getPropertyPath().toString());
		assertEquals("size must be between 1 and 20", violations.getMessage());
	}

	@Test
	public void testCreatedDateTimeIsNullOnCreate() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setName("test");
		genre.setCreatedDateTime(now);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnCreate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("createdDateTime", violations.getPropertyPath().toString());
		assertEquals("must be null", violations.getMessage());
	}

	@Test
	public void testCreatedDateTimeIsNotOnUpdate() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setName("test");
		genre.setCreatedDateTime(null);
		genre.setUpdatedDateTime(now);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnUpdate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("createdDateTime", violations.getPropertyPath().toString());
		assertEquals("must not be null", violations.getMessage());
	}

	@Test
	public void testModifiedDateTimeIsNullOnCreate() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setName("test");
		genre.setUpdatedDateTime(now);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnCreate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("updatedDateTime", violations.getPropertyPath().toString());
		assertEquals("must be null", violations.getMessage());
	}

	@Test
	public void testModifiedDateTimeIsNotOnUpdate() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setName("test");
		genre.setCreatedDateTime(now);
		genre.setUpdatedDateTime(null);

		Set<ConstraintViolation<Genre>> constraintViolations = validator.validate(genre, OnUpdate.class);

		assertEquals(1, constraintViolations.size());
		ConstraintViolation<Genre> violations = constraintViolations.iterator().next();
		assertEquals("updatedDateTime", violations.getPropertyPath().toString());
		assertEquals("must not be null", violations.getMessage());
	}

	@Test
	public void testIdempotentId() {
		UUID id = UUID.randomUUID();
		Genre genre = new Genre();
		genre.setId(id);
		assertEquals(id, genre.getId());
	}

	@Test
	public void testIdempotentName() {
		String name = StringUtils.repeat("*", 10);
		Genre genre = new Genre();
		genre.setName(name);
		assertEquals(name, genre.getName());
	}

	@Test
	public void testIdempotentCreatedDateTime() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setCreatedDateTime(now);
		assertEquals(now, genre.getCreatedDateTime());
	}

	@Test
	public void testIdempotentUpdatedDateTime() {
		ZonedDateTime now = ZonedDateTime.now();
		Genre genre = new Genre();
		genre.setUpdatedDateTime(now);
		assertEquals(now, genre.getUpdatedDateTime());
	}
}
