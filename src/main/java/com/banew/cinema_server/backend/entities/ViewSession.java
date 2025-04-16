package com.banew.cinema_server.backend.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private Film film;
    private LocalDateTime date;
    private String format;
    private Integer price_per_sit;
    @ManyToOne(optional = false)
    private Hall hall_data;
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Booking> bookings;

}
