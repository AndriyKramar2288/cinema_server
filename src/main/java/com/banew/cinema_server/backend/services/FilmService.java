package com.banew.cinema_server.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.dto.SessionCreationDto;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.entities.ViewSession;
import com.banew.cinema_server.backend.repositories.FilmRepo;
import com.banew.cinema_server.backend.repositories.HallRepo;
import com.banew.cinema_server.backend.repositories.ViewSessionRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FilmService {
    private FilmRepo filmRepo;
    private HallRepo hallRepo;
    private ViewSessionRepo viewSessionRepo;

    public void saveFilm(Set<Film> films) {
        filmRepo.saveAll(films);
    }

    public List<Film> getFilmsWithSessions() {
        return filmRepo.findBySessionsIsNotEmpty();
    }

    public ViewSession createSession(SessionCreationDto data) throws BadRequestException {
        var session = new ViewSession();
        Hall hall = hallRepo.findById(data.getHall_id()).orElseThrow(() -> new BadRequestException("Hall with " + data.getHall_id() + " is not exist!"));
        Film film = filmRepo.findById(data.getHall_id()).orElseThrow(() -> new BadRequestException("Film with " + data.getFilm_id() + " is not exist!"));

        session.setFilm(film);
        session.setHall_data(hall);
        session.setFormat(data.getFormat());
        session.setPrice_per_sit(data.getPrice_per_sit());
        session.setDate(LocalDateTime.parse(data.getDate()));

        viewSessionRepo.save(session);

        return session;
    }

    public List<ViewSession> getSessionsByFilmId(Long film_id) throws BadRequestException {
        Film film = filmRepo.findById(film_id).orElseThrow(() -> new BadRequestException("Film with " + film_id + " is not exist!"));

        return viewSessionRepo.findByFilm(film);
    }
}
