package com.banew.cinema_server.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.dto.CinemaUserInfoDto;
import com.banew.cinema_server.backend.dto.ViewSessionInfoDto;
import com.banew.cinema_server.backend.entities.CinemaUser;
import com.banew.cinema_server.backend.services.CinemaUserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    CinemaUserService cinemaUserService;

    @GetMapping("/")
    public CinemaUserInfoDto getCurrentProfile(@AuthenticationPrincipal CinemaUser cinemaUser) {
        return CinemaUserInfoDto.fromUser(cinemaUser);
    }

    @GetMapping("/session/")
    public List<ViewSessionInfoDto> getUserBookingSessions(@AuthenticationPrincipal CinemaUser cinemaUser) {
        return cinemaUserService.getBookingSessionsByUser(cinemaUser);
    }
}
