package io.bhavik.genre.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.bhavik.genre.model.Genre;
import io.bhavik.genre.service.GenreService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GenreControllerTest {

	private static final String path = "/genre";

	@Mock
	private GenreService service;

	@Inject
	private GenreController controller;

	@Inject
	private TestRestTemplate restTemplate;

	private Genre testGenre;

	@Before
	public void setup() {
		Genre genre = new Genre();
		genre.setName("testGenre");
		testGenre = restTemplate.postForEntity(path, genre, Genre.class, Collections.emptyMap()).getBody();
	}

	@After
	public void tearDown() {
		restTemplate.delete(path + "/{id}", testGenre.getId().toString());
	}

	@Test
	public void testGenreNotFound() {
		Mockito.when(service.getGenre(Mockito.any())).thenReturn(null);
		controller.getGenre(UUID.randomUUID());
		Mockito.verifyNoMoreInteractions(service);
	}

	// Integration Tests start here
	@Test
	public void testCreateGenre() {
		Genre genre = new Genre();
		genre.setName("createTest");

		ResponseEntity<Genre> response = restTemplate.postForEntity(path, genre, Genre.class, Collections.emptyMap());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getHeaders().getLocation().toString());

		Genre actual = response.getBody();

		assertEquals(genre.getName(), actual.getName());
		assertNotNull(actual.getId());
		assertNotNull(actual.getCreatedDateTime());
		assertNotNull(actual.getUpdatedDateTime());
	}

	@Test
	public void testGetGenre() {
		ResponseEntity<Genre> response = restTemplate.getForEntity(path + "/{id}", Genre.class,
				testGenre.getId().toString());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Genre actual = response.getBody();

		assertEquals(testGenre.getName(), actual.getName());
		assertEquals(testGenre.getId(), actual.getId());
		assertEquals(testGenre.getCreatedDateTime(), actual.getCreatedDateTime());
		assertEquals(testGenre.getUpdatedDateTime(), actual.getUpdatedDateTime());
	}

	@Test
	public void testSearchReturnsGenres() {
		Genre genre = new Genre();
		genre.setName("searchTest1");
		restTemplate.postForEntity(path, genre, Genre.class, Collections.emptyMap());
		genre.setName("searchTest2");
		restTemplate.postForEntity(path, genre, Genre.class, Collections.emptyMap());
		genre.setName("searchTest3");
		restTemplate.postForEntity(path, genre, Genre.class, Collections.emptyMap());

		ResponseEntity<Genre[]> response = restTemplate.getForEntity(path + "?name={name}", Genre[].class, "search");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Genre> actual = Arrays.asList(response.getBody());

		assertEquals(3, actual.size());
	}

	@Test
	public void testSearchNoResults() {
		ResponseEntity<Genre[]> response = restTemplate.getForEntity(path + "?name={name}", Genre[].class,
				UUID.randomUUID().toString());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Genre> actual = Arrays.asList(response.getBody());
		assertEquals(0, actual.size());
	}

	@Test
	public void testUpdateGenre() {
		String id = testGenre.getId().toString();

		Genre update = new Genre();
		update.setName("update-genre");
		update.setCreatedDateTime(testGenre.getCreatedDateTime());
		update.setUpdatedDateTime(testGenre.getUpdatedDateTime());

		ResponseEntity<Genre> actualResponse = restTemplate.exchange(path + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(update), Genre.class, id);
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());

		Genre actual = actualResponse.getBody();
		assertEquals(update.getName(), actual.getName());
		assertEquals(id, actual.getId().toString());
		assertEquals(update.getCreatedDateTime(), actual.getCreatedDateTime());
		assertNotEquals(update.getUpdatedDateTime(), actual.getUpdatedDateTime());
	}

	@Test
	@Ignore
	public void testUpdateCreatesGenre() {
		String id = UUID.randomUUID().toString();
		Genre update = new Genre();
		update.setName("update-create-genre");
		update.setCreatedDateTime(testGenre.getCreatedDateTime());
		update.setUpdatedDateTime(testGenre.getUpdatedDateTime());

		ResponseEntity<Genre> actualResponse = restTemplate.exchange(path + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(update), Genre.class, id);
		assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());

		Genre actual = actualResponse.getBody();
		assertEquals(update.getName(), actual.getName());
		assertEquals(id, actual.getId().toString());
	}

	@Test
	public void testDeleteGenre() {
		ResponseEntity<String> actual = restTemplate.exchange(path + "/{id}", HttpMethod.DELETE, null, String.class,
				testGenre.getId().toString());
		assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
	}

	@Test
	@Ignore
	public void testDeleteGenreDoesNotExist() {
		restTemplate.delete(path + "/{id}", testGenre.getId().toString());
		ResponseEntity<String> actual = restTemplate.exchange(path + "/{id}", HttpMethod.DELETE, null, String.class,
				testGenre.getId().toString());
		assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
	}

//	@Test
//	public void testCreateGenreWithSameName() {
//		Genre genre = new Genre();
//		genre.setName("createTest");
//		restTemplate.postForEntity(path, genre, Genre.class, Collections.emptyMap());
//		ResponseEntity<ApiError> conflictResponse = restTemplate.postForEntity(path, genre, ApiError.class,
//				Collections.emptyMap());
//		assertEquals(HttpStatus.CONFLICT, conflictResponse.getStatusCode());
//
//		ApiError actual = conflictResponse.getBody();
//		assertEquals(1, actual.getErrors().size());
//		assertTrue(actual.getMessage().contains("already exists"));
//	}

	@Test
	@Ignore
	public void testGetReturnNotFound() {
		ResponseEntity<Genre> response = restTemplate.getForEntity(path + "/{id}", Genre.class,
				UUID.randomUUID().toString());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	@Ignore
	public void testUpdateChangedGenre() {
		String id = testGenre.getId().toString();

		Genre update = new Genre();
		update.setName("update-genre");
		update.setCreatedDateTime(testGenre.getCreatedDateTime());
		update.setUpdatedDateTime(testGenre.getUpdatedDateTime().plusMinutes(10));

		ResponseEntity<Genre> actualResponse = restTemplate.exchange(path + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(update), Genre.class, id);
		assertEquals(HttpStatus.CONFLICT, actualResponse.getStatusCode());
	}

}
