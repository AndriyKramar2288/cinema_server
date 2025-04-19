package com.banew.cinema_server.backend.dto;

import com.banew.cinema_server.backend.entities.Booking;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingOnlySitDto {
    @NotNull
    private Long sit;

    public static BookingOnlySitDto fromBooking(Booking booking) {
        BookingOnlySitDto ret = builder()
        .sit(booking.getSit())
        .build();
        return ret;
    }
}