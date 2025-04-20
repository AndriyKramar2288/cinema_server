package com.banew.cinema_server.backend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Film {
    @Id
    @GeneratedValue
    Long id;
    private String uk_name;
    private String en_name;
    private Long release_year;
    private List<String> countries;
    private List<String> genres;
    private String director;
    private String src_poster;
    private List<String> src_photos;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Actor> actors;
    private Long duration;
    private String voice_acting;
    @ElementCollection
    private List<Rate> rating;
    private String age_limit;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String about;
    @OneToMany(mappedBy = "film")
    private List<ViewSession> sessions;
}