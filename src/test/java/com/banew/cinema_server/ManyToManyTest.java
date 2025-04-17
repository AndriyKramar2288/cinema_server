package com.banew.cinema_server;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.banew.cinema_server.backend.repositories.FilmRepo;

@DataJpaTest
public class ManyToManyTest {
    @Autowired
    private FilmRepo filmRepo;

    @Test
    public void isWorks() {
        // Film film = new Film();
        // film.setActors(Set.of(Actor.builder().fullname("HOMO").build()));
        // filmRepo.save(film);



        // System.out.println(filmRepo.findAll());
    }
}
