package com.banew.cinema_server.backend.controllers;

import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.dto.SessionCreationDto;
import com.banew.cinema_server.backend.dto.ViewSessionFullInfoDto;
import com.banew.cinema_server.backend.dto.ViewSessionInfoDto;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.services.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping("/{id}")
    public List<ViewSessionInfoDto> getSessionsByFilmId(@PathVariable String id) throws BadRequestSendedException {
        return sessionService.getSessionsByFilmId(Long.parseLong(id));
    }

    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ViewSessionInfoDto createSession(@RequestBody @Valid SessionCreationDto sessionCreationDto)  throws BadRequestSendedException {
        return sessionService.createSession(sessionCreationDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delSessionById(@PathVariable Long id) throws BadRequestSendedException {
        sessionService.deleteSessionById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/available")
    public List<ViewSessionInfoDto> getAvailableSessions() {
        return sessionService.getAvailableSessions().stream()
        .map(session -> ViewSessionInfoDto.fromViewSession(session))
        .toList();
    }

    @PreAuthorize("hasRole('WORKER') or hasRole('ADMIN')")
    @GetMapping("/available_worker")
    public List<ViewSessionFullInfoDto> getAvailableSessionsFullInfo() {
        return sessionService.getFutureSessions()
        .stream()
        .sorted(Comparator.comparing(session -> session.getDate()))
        .map(session -> ViewSessionFullInfoDto.fromViewSession(session))
        .toList();
    }
}