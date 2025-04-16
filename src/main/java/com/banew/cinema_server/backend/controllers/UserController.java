package com.banew.cinema_server.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.entities.CinemaUser;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/")
    public CinemaUser getCurrentProfile(@AuthenticationPrincipal CinemaUser cinemaUser) {
        return cinemaUser;
    }
}
