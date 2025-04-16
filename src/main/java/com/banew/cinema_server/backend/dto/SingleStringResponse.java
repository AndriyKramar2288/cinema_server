package com.banew.cinema_server.backend.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class SingleStringResponse {
    private String message;
    private String status;

    public SingleStringResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus.toString();
    }
}
