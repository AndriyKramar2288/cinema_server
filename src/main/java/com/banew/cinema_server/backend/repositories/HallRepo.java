package com.banew.cinema_server.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.banew.cinema_server.backend.entities.Hall;

public interface HallRepo extends CrudRepository<Hall, Long> {

}
