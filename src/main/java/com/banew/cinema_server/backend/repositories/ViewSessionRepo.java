package com.banew.cinema_server.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.ViewSession;
import java.util.List;


public interface ViewSessionRepo extends CrudRepository<ViewSession, Long> {
    List<ViewSession> findByFilm(Film film);
}
