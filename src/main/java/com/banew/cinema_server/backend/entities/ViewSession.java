package com.banew.cinema_server.backend.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class ViewSession {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "film_id")
    private Film film;
    private LocalDateTime date;
    private String format;
    private Integer price_per_sit;
    @ManyToOne(optional = false)
    @JoinColumn(name = "hall_data_id")
    private Hall hall_data;
    @OneToMany(mappedBy = "viewSession", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
