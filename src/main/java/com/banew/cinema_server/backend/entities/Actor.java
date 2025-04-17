package com.banew.cinema_server.backend.entities;

import java.util.List;

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
public class Actor {
    @Id
    @GeneratedValue
    Long id;
    @NotBlank
    private String fullname;
    @JsonIgnore
    @ManyToMany(mappedBy = "actors")
    private List<Film> films;
}
