package com.banew.cinema_server.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.dto.SessionCreationDto;
import com.banew.cinema_server.backend.dto.SingleStringResponse;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.entities.ViewSession;
import com.banew.cinema_server.backend.services.FilmService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delFilmById(@PathVariable Long id) {
        filmService.deleteFilmById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/with_sessions")
    public List<Film> getAllFilmsThatHaveSessions() {
        return filmService.getFilmsWithSessions();
    }
    
    @GetMapping("/session/{id}")
    public List<ViewSession> getSessionsByFilmId(@PathVariable String id) throws BadRequestException {
        return filmService.getSessionsByFilmId(Long.parseLong(id));
    }

    @PostMapping("/session/")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ViewSession createSession(@RequestBody @Valid SessionCreationDto sessionCreationDto)  throws BadRequestException {
        return filmService.createSession(sessionCreationDto);
    }

    @GetMapping("/hall/")
    public List<Hall> getHalls() {
        return filmService.getAllHalls();
    }

    @PostMapping("/hall/")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Hall createHall(@RequestBody @Valid Hall hall)  throws BadRequestException {
        return filmService.saveHall(hall);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/hall/{id}")
    public ResponseEntity delHallById(@PathVariable Long id) {
        filmService.deleteHallById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Film> createFilms(@RequestBody @Valid List<Film> films)  throws BadRequestException {
        return filmService.saveFilm(films);
    }
    
    
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SingleStringResponse badRequest(BadRequestException ex) {
        return new SingleStringResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SingleStringResponse badRequest(ValidationException ex) {
        return new SingleStringResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
