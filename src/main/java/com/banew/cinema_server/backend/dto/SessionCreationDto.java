package com.banew.cinema_server.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionCreationDto {
    @NotBlank
    private String date;
    @NotBlank
    private String format;
    @NotNull
    @Min(value = 5)
    private Integer price_per_sit;
    @NotNull
    private Long hall_id;
    @NotNull
    private Long film_id;
}
