package com.banew.cinema_server.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.dto.FilmSimpleInfoDto;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.repositories.FilmRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FilmService {
    private FilmRepo filmRepo;

    public FilmSimpleInfoDto saveFilm(FilmSimpleInfoDto filmInfo) {
        filmRepo.save(filmInfo.toFilm());
        return filmInfo;
    }

    public Optional<Film> getFilmById(Long id) {
        return filmRepo.findById(id);
    }

    public void deleteFilmById(Long id) {
        filmRepo.deleteById(id);
    }

    public List<Film> getAll() {
        List<Film> list = new ArrayList<>();
        filmRepo.findAll().forEach(list::add);
        return list;
    }
}
