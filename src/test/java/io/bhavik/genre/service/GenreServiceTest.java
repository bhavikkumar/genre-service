package io.bhavik.genre.service;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.bhavik.genre.model.Genre;
import io.bhavik.genre.repoistory.GenreRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GenreService.class)
public class GenreServiceTest {

	@MockBean
	private GenreRepository repository;

	@Inject
	private GenreService service;

	@Test
	public void testCreateGenre() {
		Genre genre = new Genre();
		service.createGenre(genre);
		Mockito.verify(repository).save(genre);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testGetGenre() {
		UUID id = UUID.randomUUID();
		service.getGenre(id);
		Mockito.verify(repository).findOne(id);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testUpdateGenre() {
		Genre genre = new Genre();
		service.updateGenre(genre);
		Mockito.verify(repository).save(genre);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void testDeleteGenre() {
		UUID id = UUID.randomUUID();
		service.deleteGenre(id);
		Mockito.verify(repository).delete(id);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSearchGenre() {
		String name = "unitTest";
		service.searchGenre(name);
		Mockito.verify(repository).findAll(Mockito.any(Example.class));
		Mockito.verifyNoMoreInteractions(repository);
	}

}
