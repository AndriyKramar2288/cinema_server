package com.banew.cinema_server.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Hall {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}