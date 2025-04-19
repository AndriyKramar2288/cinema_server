package com.banew.cinema_server.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private ViewSession viewSession;
    private LocalDateTime bookingTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cinemaUser_id")
    private CinemaUser cinemaUser;

    @ManyToOne
    @JoinColumn(name = "cinemaViewer_id")
    private CinemaViewer cinemaViewer;
    @NotNull
    private Long sit;
}