package com.banew.cinema_server.backend.dto;

import java.util.List;

import com.banew.cinema_server.backend.entities.Actor;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Rate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmSimpleInfoDto {
    Long id;
    @NotBlank
    private String uk_name;
    @NotBlank
    private String en_name;
    @NotNull
    private Long release_year;
    @NotNull
    private List<String> countries;
    @NotNull
    private List<String> genres;
    @NotBlank
    private String director;
    @NotBlank
    private String src_poster;
    @NotNull
    private List<String> src_photos;
    @NotNull
    private List<String> actors;
    @NotBlank
    private String duration;
    @NotNull
    private String voice_acting;
    @NotBlank
    private String imdb;
    @NotBlank
    private String age_limit;
    @NotBlank
    private String about;

    public static FilmSimpleInfoDto fromFilm(Film film) {
        return builder()
        .uk_name(film.getUk_name())
        .en_name(film.getEn_name())
        .release_year(film.getRelease_year())
        .countries(film.getCountries())
        .genres(film.getGenres())
        .director(film.getDirector())
        .src_poster(film.getSrc_poster())
        .src_photos(film.getSrc_photos())
        .actors(film.getActors().stream()
            .map(actor -> actor.getFullname())
            .toList())
        .duration(film.getDuration())
        .voice_acting(film.getVoice_acting())
        .imdb(film.getRating().stream()
            .filter(rate -> rate.getName().equals("imdb"))
            .findFirst().orElseGet(() -> {
                Rate rate = new Rate();
                rate.setName("imdb");
                rate.setRate("-");
                return rate;
            })
            .getRate())
        .age_limit(film.getAge_limit())
        .about(film.getAbout())
        .id(film.getId())
        .build();
    }

    public Film toFilm() {
        Film film = new Film();
        film.setAbout(about);
        film.setActors(actors.stream().map(actorName -> {
            Actor actor = new Actor();
            actor.setFullname(actorName);
            return actor;
        }).toList());
        film.setAge_limit(age_limit);
        film.setCountries(countries);
        film.setDirector(director);
        film.setDirector(director);
        film.setDuration(duration);
        film.setEn_name(en_name);
        film.setGenres(genres);

        Rate imdb_rate = new Rate();
        imdb_rate.setName("imdb");
        imdb_rate.setRate(imdb);

        film.setRating(List.of(imdb_rate));
        film.setRelease_year(release_year);
        film.setSrc_photos(src_photos);
        film.setSrc_poster(src_poster);
        film.setUk_name(uk_name);
        film.setVoice_acting(voice_acting);

        return film;
    }
}


