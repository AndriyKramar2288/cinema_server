package com.banew.cinema_server.backend.controllers;

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

import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.services.HallService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/hall")
public class HallController {
    @Autowired
    private HallService hallService;

    @GetMapping("/")
    public List<Hall> getHalls() {
        return hallService.getAllHalls();
    }

    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Hall createHall(@RequestBody @Valid Hall hall) throws BadRequestSendedException {
        return hallService.saveHall(hall);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delHallById(@PathVariable Long id) throws BadRequestSendedException {
        hallService.deleteHallById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
