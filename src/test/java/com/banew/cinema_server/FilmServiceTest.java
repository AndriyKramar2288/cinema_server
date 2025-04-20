package com.banew.cinema_server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.banew.cinema_server.backend.dto.FilmSimpleInfoDto;
import com.banew.cinema_server.backend.services.FilmService;

@SpringBootTest
public class FilmServiceTest {
    @Autowired
    FilmService filmService;

    @Transactional
    @Rollback
    @Test
    void mappingPlusSaving() {
        FilmSimpleInfoDto info = FilmSimpleInfoDto.builder()
        .about("хуй")
        .actors(List.of("aboba"))
        .age_limit("")
        .countries(List.of("aboba"))
        .director("aboba")
        .duration(14555L)
        .en_name("aboba")
        .genres(List.of("aboba"))
        .imdb("aboba")
        .uk_name("aboba")
        .voice_acting("aboba")
        .src_poster("ABOBA1")
        .src_photos(List.of("ABBOA"))
        .build();

        filmService.saveFilm(info);
        FilmSimpleInfoDto realInfo = FilmSimpleInfoDto.fromFilm(
            filmService.getAll().stream().filter(f -> f.getAbout() == "хуй").findFirst().orElseThrow()
        );
        info.setId(realInfo.getId());

        assertEquals(info, realInfo);
    }
}
