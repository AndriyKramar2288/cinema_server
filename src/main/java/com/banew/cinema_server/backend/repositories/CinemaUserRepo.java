package com.banew.cinema_server.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.banew.cinema_server.backend.entities.CinemaUser;
import java.util.List;


public interface CinemaUserRepo extends CrudRepository<CinemaUser, Long> {
    List<CinemaUser> findByUsername(String username);
}
