package io.bhavik.genre.repoistory;

import java.util.UUID;

import javax.inject.Named;

import org.springframework.data.jpa.repository.JpaRepository;

import io.bhavik.genre.model.Genre;

@Named
public interface GenreRepository extends JpaRepository<Genre, UUID> {

}
