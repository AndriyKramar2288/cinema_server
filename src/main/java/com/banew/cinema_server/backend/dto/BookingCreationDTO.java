package com.banew.cinema_server.backend.dto;

import com.banew.cinema_server.backend.entities.CinemaViewer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingCreationDto {
    @NotNull
    private Long session_id;
    private Long user_id;
    @NotNull
    private CinemaViewer cinemaViewer;
    @NotNull
    private Long sit;
}
