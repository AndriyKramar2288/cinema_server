package com.banew.cinema_server.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CinemaUser getCurrentProfile(@AuthenticationPrincipal CinemaUser cinemaUser) {
        return cinemaUser;
    }

    // @GetMapping("/booking/")
    // public List<Booking> getCurrentBookings(@AuthenticationPrincipal CinemaUser cinemaUser) {
    //     return cinemaUserService.getBookingsByUser(cinemaUser);
    // }
}
