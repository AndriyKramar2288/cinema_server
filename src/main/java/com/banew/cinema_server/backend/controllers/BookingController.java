package com.banew.cinema_server.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.dto.BookingCreationDto;
import com.banew.cinema_server.backend.dto.BookingInfoDto;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.services.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/")
    public List<BookingInfoDto> saveBookings(@RequestBody @Valid List<BookingCreationDto> bookings) throws BadRequestSendedException {
        return bookingService.saveBookings(bookings);
    }
}
