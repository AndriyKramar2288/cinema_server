package com.banew.cinema_server.backend.dto;

import com.banew.cinema_server.backend.entities.CinemaUser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private CinemaUser cinemaUser;
    private String accessToken;
}
