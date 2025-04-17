package com.banew.cinema_server.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.dto.BookingCreationDTO;
import com.banew.cinema_server.backend.dto.SessionCreationDto;
import com.banew.cinema_server.backend.entities.Booking;
import com.banew.cinema_server.backend.entities.CinemaUser;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.entities.ViewSession;
import com.banew.cinema_server.backend.repositories.BookingRepo;
import com.banew.cinema_server.backend.repositories.CinemaUserRepo;
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
    private BookingRepo bookingRepo;
    private CinemaUserRepo cinemaUserRepo;

    private static final Long PREPARE_TIME = 10L;
    private static final Long MIN_BOOKING = 20L;
    private static final String TIME_CURRENTLY_UNTAKEBLE = "Вказаний час сеансу у вказаній залі вже зайнятий!";

    public List<Film> saveFilm(List<Film> films) {
        filmRepo.saveAll(films);
        return films;
    }

    public void deleteFilmById(Long id) {
        filmRepo.deleteById(id);
    }

    public void deleteHallById(Long id) {
        hallRepo.deleteById(id);
    }

    public void deleteSessionById(Long id) {
        viewSessionRepo.deleteById(id);
    }

    public List<Film> getFilmsWithSessions() {
        List<Film> result = filmRepo.findBySessionsIsNotEmpty();

        LocalDateTime minTimeFilm = LocalDateTime.now().plusMinutes(MIN_BOOKING);

        return result.stream().filter(each -> {
            Long notBeforeNowSessionsFilmSize = each.getSessions().stream()
                .filter(session -> session.getDate().isAfter(minTimeFilm)).count();
            
            return notBeforeNowSessionsFilmSize > 0;
        }).toList();
    }

    public List<Film> getAll() {
        List<Film> list = new ArrayList<>();
        filmRepo.findAll().forEach(list::add);
        return list;
    }

    public ViewSession createSession(SessionCreationDto data) throws BadRequestException {
        var session = new ViewSession();
        Hall hall = hallRepo.findById(data.getHall_id()).orElseThrow(() -> new BadRequestException("Hall with " + data.getHall_id() + " is not exist!"));
        Film film = filmRepo.findById(data.getFilm_id()).orElseThrow(() -> new BadRequestException("Film with " + data.getFilm_id() + " is not exist!"));

        session.setFilm(film);
        session.setHall_data(hall);
        session.setFormat(data.getFormat());
        session.setPrice_per_sit(data.getPrice_per_sit());
        session.setDate(LocalDateTime.parse(data.getDate()));

        for(ViewSession eachSession : viewSessionRepo.findAll()) {
            if (!eachSession.getHall_data().equals(session.getHall_data())) {
                continue;
            }

            if (eachSession.getDate().plusMinutes(Long.parseLong(eachSession.getFilm().getDuration() + PREPARE_TIME)).isBefore(session.getDate()) ||
                session.getDate().plusMinutes(Long.parseLong(film.getDuration() + PREPARE_TIME)).isBefore(eachSession.getDate())) {
                    continue;
            }

            throw new BadRequestException(TIME_CURRENTLY_UNTAKEBLE);
        }

        viewSessionRepo.save(session);

        return session;
    }

    public Hall saveHall(Hall hall) {
        hallRepo.save(hall);
        return hall;
    }

    public List<Hall> getAllHalls() {
        List<Hall> list = new ArrayList<>();
        hallRepo.findAll().forEach(list::add);
        return list;
    }

    public List<ViewSession> getSessionsByFilmId(Long film_id) throws BadRequestException {
        Film film = filmRepo.findById(film_id).orElseThrow(() -> new BadRequestException("Film with " + film_id + " is not exist!"));
        return viewSessionRepo.findByFilm(film);
    }

    public List<Booking> saveBookings(List<BookingCreationDTO> bookingDtos) throws BadRequestException {
        List<Booking> bookings = new ArrayList<>();
        
        for (BookingCreationDTO dto : bookingDtos) {
            Booking ret = new Booking();
            ret.setCinemaViewer(dto.getCinemaViewer());
            ret.setSit(dto.getSit());

            if (dto.getUser_id() != null) {
                CinemaUser user = cinemaUserRepo.findById(dto.getUser_id()).orElseThrow(
                    () -> new BadRequestException("User with " + dto.getUser_id() + " is not exist!"));
                ret.setCinemaUser(user);
            }

            ViewSession viewSession = viewSessionRepo.findById(dto.getSession_id()).orElseThrow(
                () -> new BadRequestException("Session with " + dto.getSession_id() + " is not exist!"));

            ret.setViewSession(viewSession);
            bookings.add(ret);
        }

        bookingRepo.saveAll(bookings);
        return bookings;
    }
}
