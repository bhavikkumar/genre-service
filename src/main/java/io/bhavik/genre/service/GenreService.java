package io.bhavik.genre.service;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import io.bhavik.genre.model.Genre;
import io.bhavik.genre.repoistory.GenreRepository;

@Named
public class GenreService {

	@Inject
	private GenreRepository repository;

	public Genre createGenre(Genre genre) {
		return repository.save(genre);
	}

	public Genre getGenre(UUID id) {
		return repository.findOne(id);
	}

	public Genre updateGenre(Genre genre) {
		return repository.save(genre);
	}

	public void deleteGenre(UUID id) {
		repository.delete(id);
	}

	public List<Genre> searchGenre(String name) {
		Genre genre = new Genre();
		genre.setName(name);

		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", match -> match.startsWith());
		Example<Genre> example = Example.of(genre, matcher);
		return repository.findAll(example);
	}
}
