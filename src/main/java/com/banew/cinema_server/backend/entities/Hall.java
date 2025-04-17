package com.banew.cinema_server.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Hall {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    @Min(value = 3)
    private Long size;
}