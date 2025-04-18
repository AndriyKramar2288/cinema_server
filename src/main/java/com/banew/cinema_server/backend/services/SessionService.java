package com.banew.cinema_server.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banew.cinema_server.backend.dto.FilmSimpleInfoDto;
import com.banew.cinema_server.backend.dto.SessionCreationDto;
import com.banew.cinema_server.backend.dto.ViewSessionInfoDto;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.entities.ViewSession;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.repositories.ViewSessionRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionService {
    private FilmService filmService;
    private HallService hallService;

    private ViewSessionRepo viewSessionRepo;

    private static final Long MIN_BOOKING = 20L;
    private static final Long PREPARE_TIME = 10L;
    private static final String TIME_CURRENTLY_UNTAKEBLE = "Вказаний час сеансу у вказаній залі вже зайнятий!";

    public List<ViewSessionInfoDto> getSessionsByFilmId(Long film_id) throws BadRequestSendedException {
        Film film = filmService.getFilmById(film_id).orElseThrow(() -> new BadRequestSendedException("Film with " + film_id + " is not exist!"));
        return viewSessionRepo.findByFilm(film).stream().map(session -> ViewSessionInfoDto.fromViewSession(session)).toList();
    }

    public void deleteSessionById(Long id) throws BadRequestSendedException {
        try {
            viewSessionRepo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestSendedException("Видалення не вдалось: надто багато залежностей!");
        }
    }

    public List<ViewSession> getAll() {
        List<ViewSession> list = new ArrayList<>();
        viewSessionRepo.findAll().forEach(list::add);
        return list;
    }

    @Transactional
    public List<FilmSimpleInfoDto> getFilmInfoWithSessions() {
        return getAvailableSessions().stream().map(session -> {
            return FilmSimpleInfoDto.fromFilm(session.getFilm());
        }).toList();
    }

    @Transactional
    public List<ViewSession> getAvailableSessions() {
        LocalDateTime minTimeFilm = LocalDateTime.now().plusMinutes(MIN_BOOKING);

        return getAll().stream()
            .filter(session -> session.getDate().isAfter(minTimeFilm))
            .filter(session -> session.getBookings().size() < session.getHall_data().getSize())
            .toList();
    }

    @Transactional
    public ViewSessionInfoDto createSession(SessionCreationDto data) throws BadRequestSendedException {
        var session = new ViewSession();
        Hall hall = hallService.getHallById(data.getHall_id()).orElseThrow(() -> new BadRequestSendedException("Hall with " + data.getHall_id() + " is not exist!"));
        Film film = filmService.getFilmById(data.getFilm_id()).orElseThrow(() -> new BadRequestSendedException("Film with " + data.getFilm_id() + " is not exist!"));

        session.setFilm(film);
        session.setHall_data(hall);
        session.setFormat(data.getFormat());
        session.setPrice_per_sit(data.getPrice_per_sit());
        session.setDate(LocalDateTime.parse(data.getDate()));
        session.setBookings(List.of());

        for(ViewSession eachSession : viewSessionRepo.findAll()) {
            if (!eachSession.getHall_data().equals(session.getHall_data())) {
                continue;
            }

            if (eachSession.getDate().plusMinutes(Long.parseLong(eachSession.getFilm().getDuration() + PREPARE_TIME)).isBefore(session.getDate()) ||
                session.getDate().plusMinutes(Long.parseLong(film.getDuration() + PREPARE_TIME)).isBefore(eachSession.getDate())) {
                    continue;
            }

            throw new BadRequestSendedException(TIME_CURRENTLY_UNTAKEBLE);
        }

        viewSessionRepo.save(session);
        return ViewSessionInfoDto.fromViewSession(session);
    }

    public Optional<ViewSession> getSessionById(Long session_id) {
        return viewSessionRepo.findById(session_id);
    }
}
