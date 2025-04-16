package com.banew.cinema_server.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.banew.cinema_server.backend.entities.Film;

public interface FilmRepo extends CrudRepository<Film, Long> {

}
