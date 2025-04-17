package com.banew.cinema_server.backend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Film {
    @Id
    @GeneratedValue
    Long id;
    @NotBlank
    private String uk_name;
    @NotBlank
    private String en_name;
    private Long release_year;
    private List<String> countries;
    private List<String> genres;
    @NotBlank
    private String director;
    @NotBlank
    private String src_poster;
    private List<String> src_photos;
    @ManyToMany(targetEntity = Actor.class, cascade = CascadeType.ALL)
    private List<Actor> actors;
    private String duration;
    private String voice_acting;
    @ManyToMany(targetEntity = Rate.class, cascade = CascadeType.ALL)
    private List<Rate> rating;
    private String age_limit;
    private String about;
    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
    private List<ViewSession> sessions;
}
