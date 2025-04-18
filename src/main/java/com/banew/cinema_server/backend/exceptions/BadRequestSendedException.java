package com.banew.cinema_server.backend.exceptions;

public class BadRequestSendedException extends Exception {
    public BadRequestSendedException(String message) {
        super(message);
    }
}
