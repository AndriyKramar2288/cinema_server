package com.banew.cinema_server.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ViewSession viewSession;
    @ManyToOne(fetch = FetchType.EAGER)
    private CinemaUser cinemaUser;
    private LocalDateTime bookingTime = LocalDateTime.now();
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CinemaViewer cinemaViewer;
    @NotNull
    private Long sit;
}