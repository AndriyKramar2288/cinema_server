package com.banew.cinema_server.backend.dto;

import com.banew.cinema_server.backend.entities.Booking;
import com.banew.cinema_server.backend.entities.Film;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingInfo {
    private Booking booking;
    private Film film;
}
