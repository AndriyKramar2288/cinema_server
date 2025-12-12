package com.banew.cinema_server.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.dto.FilmSimpleInfoDto;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.services.FilmService;
import com.banew.cinema_server.backend.services.ParsingService;
import com.banew.cinema_server.backend.services.SessionService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmService filmService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ParsingService parsingService;

    @GetMapping("/with_sessions")
    public List<FilmSimpleInfoDto> getAllAvailable() {
        return sessionService.getAvailableSessions().stream()
        .map(session -> FilmSimpleInfoDto.fromFilm(session.getFilm()))
        .distinct()
        .toList();
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FilmSimpleInfoDto> getAll() {
        return filmService.getAll().stream().map(film -> FilmSimpleInfoDto.fromFilm(film)).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delFilmById(@PathVariable Long id) throws BadRequestSendedException {
        filmService.deleteFilmById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public FilmSimpleInfoDto createFilm(@RequestBody @Valid FilmSimpleInfoDto filmData) {
        return filmService.saveFilm(filmData);
    }

    @GetMapping("/uakino")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FilmSimpleInfoDto> getUakinoSearch(@RequestParam String request) throws BadRequestSendedException {
        if (request == null) {
            throw new BadRequestSendedException("Запит в полі 'request' вказаний не був!");
        }
        else if (request.length() < 3) {
            throw new BadRequestSendedException("Запит занадто короткий!");
        }
        return parsingService.findFilmsByUAKino(request).stream().toList();
    }
}
