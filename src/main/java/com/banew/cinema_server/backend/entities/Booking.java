package com.banew.cinema_server.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "session_id")
    private ViewSession viewSession;
    private LocalDateTime bookingTime = LocalDateTime.now();
    @OneToOne
    @JoinColumn(name = "viewer_id", nullable = false)
    private CinemaViewer cinemaViewer;
}