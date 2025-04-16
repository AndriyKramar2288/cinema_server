package com.banew.cinema_server.backend.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Film {
    @Id
    @GeneratedValue
    Long id;
    private String uk_name;
    private String en_name;
    private Long release_year;
    private Set<String> countries;
    private Set<String> genres;
    private String director;
    private String src_poster;
    private Set<String> src_photos;
    @ManyToMany(targetEntity = Actor.class, cascade = CascadeType.ALL)
    private Set<Actor> actors;
    private String duration;
    private String voice_acting;
    @ManyToMany(targetEntity = Rate.class, cascade = CascadeType.ALL)
    private Set<Rate> rating;
    private String age_limit;
    private String about;
}
