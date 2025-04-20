package com.banew.cinema_server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banew.cinema_server.backend.services.ParsingService;

@ExtendWith(MockitoExtension.class)
public class ParsingTest {
    @InjectMocks
    private ParsingService parsingService;

    @Test
    public void checkTest() throws Exception {
        System.out.println(parsingService.findFilmsByUAKino("Месники").size());
    }
}
