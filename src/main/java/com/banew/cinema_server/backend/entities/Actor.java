package com.banew.cinema_server.backend.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Actor {
    @Id
    @GeneratedValue
    Long id;
    private String fullname;
    @ManyToMany(mappedBy = "actors")
    private Set<Film> films;
}
