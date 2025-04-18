package com.banew.cinema_server.backend.dto;

import java.time.LocalDateTime;

import com.banew.cinema_server.backend.entities.Booking;
import com.banew.cinema_server.backend.entities.CinemaViewer;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingInfoDto {
    private Long id;
    private LocalDateTime bookingTime;
    private CinemaUserInfoDto cinemaUser;
    private CinemaViewer cinemaViewer;
    @NotNull
    private Long sit;

    public static BookingInfoDto fromBooking(Booking booking) {
        BookingInfoDto ret = builder()
        .bookingTime(booking.getBookingTime())
        .cinemaViewer(booking.getCinemaViewer())
        .id(booking.getId())
        .sit(booking.getSit())
        .build();

        if (booking.getCinemaUser() != null) {
            ret.setCinemaUser(CinemaUserInfoDto.fromUser(booking.getCinemaUser()));
        }

        return ret;
    }
}