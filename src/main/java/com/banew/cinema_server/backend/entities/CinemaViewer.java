package com.banew.cinema_server.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class CinemaViewer {
    @Id
    @GeneratedValue
    private Long id;
    private String password;
    private String username;
    private String email;
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
