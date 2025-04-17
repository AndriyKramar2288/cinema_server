package com.banew.cinema_server.backend.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ViewSession viewSession;
    private LocalDateTime bookingTime = LocalDateTime.now();
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CinemaUser cinemaUser;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CinemaViewer cinemaViewer;
    @NotNull
    private Long sit;
}