package com.banew.cinema_server.backend.entities;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class Rate {
    @NotBlank
    private String name;
    @NotBlank
    private String rate;
}
