package com.banew.cinema_server.backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banew.cinema_server.backend.dto.BookingCreationDto;
import com.banew.cinema_server.backend.dto.BookingInfoDto;
import com.banew.cinema_server.backend.entities.Booking;
import com.banew.cinema_server.backend.entities.CinemaUser;
import com.banew.cinema_server.backend.entities.ViewSession;
import com.banew.cinema_server.backend.exceptions.BadRequestSendedException;
import com.banew.cinema_server.backend.repositories.BookingRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookingService {
    private CinemaUserService cinemaUserService;
    private SessionService sessionService;

    private BookingRepo bookingRepo;

    @Transactional
    public List<BookingInfoDto> saveBookings(List<BookingCreationDto> bookingDtos) throws BadRequestSendedException {
        List<Booking> bookings = new ArrayList<>();
        
        checkUnique(bookingDtos);

        for (BookingCreationDto dto : bookingDtos) {
            Booking ret = new Booking();
            ret.setCinemaViewer(dto.getCinemaViewer());
            ret.setSit(dto.getSit());

            if (dto.getUser_id() != null) {
                CinemaUser user = cinemaUserService.getUserById(dto.getUser_id()).orElseThrow(
                    () -> new BadRequestSendedException("User with " + dto.getUser_id() + " is not exist!"));
                ret.setCinemaUser(user);
            }

            ViewSession viewSession = sessionService.getSessionById(dto.getSession_id()).orElseThrow(
                () -> new BadRequestSendedException("Session with " + dto.getSession_id() + " is not exist!"));

            
            checkSessionAvailable(viewSession);
            checkSits(viewSession, dto);

            ret.setViewSession(viewSession);
            bookings.add(ret);
        }

        bookingRepo.saveAll(bookings);
        return bookings.stream().map(b -> BookingInfoDto.fromBooking(b)).toList();
    }

    private void checkSessionAvailable(ViewSession viewSession) throws BadRequestSendedException {
        if (!sessionService.getAvailableSessions().contains(viewSession)) {
            throw new BadRequestSendedException("Даний сеанс для бронювань вже недоступний!");
        }
    }

    private void checkSits(ViewSession viewSession, BookingCreationDto dto) throws BadRequestSendedException {
        if (viewSession.getBookings().stream().map(b -> b.getSit()).toList().contains(dto.getSit())) {
            throw new BadRequestSendedException("Місце під номером " + dto.getSit() + " вже забронювали!");
        }
    }

    private void checkUnique(List<BookingCreationDto> bookingDtos) throws BadRequestSendedException {
        Long distinctViewersCount = bookingDtos.stream()
        .map(b -> b.getCinemaViewer())
        .distinct()
        .count();

        if (distinctViewersCount < bookingDtos.size()) {
            throw new BadRequestSendedException("Деякі дані глядачів одинакові!");
        }

        Long distinctViewerNamesCount = bookingDtos.stream()
        .map(b -> b.getCinemaViewer().getFullName())
        .distinct()
        .count();

        if (distinctViewerNamesCount < bookingDtos.size()) {
            throw new BadRequestSendedException("Деякі повні імена глядачів одинакові!");
        }
    }
}
