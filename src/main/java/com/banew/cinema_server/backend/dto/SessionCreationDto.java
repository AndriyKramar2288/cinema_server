package com.banew.cinema_server.backend.dto;

import lombok.Data;

@Data
public class SessionCreationDto {
    private String date;
    private String format;
    private Integer price_per_sit;
    private Long hall_id;
    private Long film_id;
}
