package com.banew.cinema_server.backend.dto;

import java.util.Set;

import com.banew.cinema_server.backend.entities.CinemaUser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CinemaUserInfoDto {
    private Long id;
    private String username;
    private String email;

    private String photoSrc;
    private Set<String> roles;

    public static CinemaUserInfoDto fromUser(CinemaUser cinemaUser) {
        return builder()
        .email(cinemaUser.getEmail())
        .id(cinemaUser.getId())
        .photoSrc(cinemaUser.getPhotoSrc())
        .roles(cinemaUser.getRoles())
        .username(cinemaUser.getUsername())
        .build();
    }

}