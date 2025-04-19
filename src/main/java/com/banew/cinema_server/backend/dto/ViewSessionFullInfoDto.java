package com.banew.cinema_server.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.entities.ViewSession;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewSessionFullInfoDto {
    private Long id;
    private FilmSimpleInfoDto film;
    private LocalDateTime date;
    private String format;
    private Integer price_per_sit;
    private Hall hall_data;
    private List<BookingInfoDto> bookings;

    public static ViewSessionFullInfoDto fromViewSession(ViewSession viewSession) {
        return builder()
        .id(viewSession.getId())
        .date(viewSession.getDate())
        .bookings(viewSession.getBookings().stream().map(booking -> BookingInfoDto.fromBooking(booking)).toList())
        .film(FilmSimpleInfoDto.fromFilm(viewSession.getFilm()))
        .format(viewSession.getFormat())
        .hall_data(viewSession.getHall_data())
        .price_per_sit(viewSession.getPrice_per_sit())
        .build();
    }
}
