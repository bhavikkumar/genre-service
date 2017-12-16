package io.bhavik.genre.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.bhavik.genre.model.Genre;
import io.bhavik.genre.model.OnCreate;
import io.bhavik.genre.model.OnUpdate;
import io.bhavik.genre.service.GenreService;

@RestController
@RequestMapping("/genre")
public class GenreController {

	@Inject
	private GenreService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Genre> createGenre(@Validated(OnCreate.class) @RequestBody(required = true) Genre genre) {
		Genre createdGenre = service.createGenre(genre);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(createdGenre.getId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		return new ResponseEntity<Genre>(createdGenre, headers, HttpStatus.CREATED);
	}

	@GetMapping(path = "{id}")
	public Genre getGenre(@PathVariable("id") UUID id) {
		Genre genre = service.getGenre(id);
		if (genre != null) {
			return genre;
		}
		return null;
	}

	@GetMapping
	public List<Genre> searchGenre(@RequestParam("name") String name) {
		return service.searchGenre(name);
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<Genre> updateGenre(@PathVariable("id") UUID id,
			@Validated(OnUpdate.class) @RequestBody(required = true) Genre genre) {
		genre.setId(id);
		HttpStatus status = service.getGenre(id) == null ? HttpStatus.CREATED : HttpStatus.OK;
		return new ResponseEntity<Genre>(service.updateGenre(genre), status);
	}

	@DeleteMapping(path = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteGenre(@PathVariable("id") UUID id) {
		service.deleteGenre(id);
	}
}
