package com.banew.cinema_server.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.repositories.HallRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HallService {
    private HallRepo hallRepo;

    public void deleteHallById(Long id) throws BadRequestSendedException {
        try {
            hallRepo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestSendedException("Видалення не вдалось: надто багато залежностей!");
        }
    }

    public Hall saveHall(Hall hall) {
        hallRepo.save(hall);
        return hall;
    }

    public Optional<Hall> getHallById(Long id) {
        return hallRepo.findById(id);
    }

    public List<Hall> getAllHalls() {
        List<Hall> list = new ArrayList<>();
        hallRepo.findAll().forEach(list::add);
        return list;
    }
}
