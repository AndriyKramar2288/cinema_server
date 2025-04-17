package com.banew.cinema_server.backend.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Rate {
    @Id
    @GeneratedValue
    Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String rate;
    @JsonIgnore
    @ManyToMany(mappedBy = "rating")
    private Set<Film> films;
}
