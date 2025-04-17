package com.banew.cinema_server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.banew.cinema_server.backend.dto.SessionCreationDto;
import com.banew.cinema_server.backend.entities.CinemaUser;
import com.banew.cinema_server.backend.entities.Film;
import com.banew.cinema_server.backend.entities.Hall;
import com.banew.cinema_server.backend.repositories.CinemaUserRepo;
import com.banew.cinema_server.backend.services.FilmService;
import com.banew.cinema_server.backend.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    FilmService filmService;
    @Autowired
    JwtService jwtService;
    @Autowired
    CinemaUserRepo cinemaUserRepo;

    @Autowired
	private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    @BeforeEach
    public void prepare() {
        objectMapper.registerModule(new JavaTimeModule());

        CinemaUser fakeAdmin = CinemaUser.builder()
        .roles(Set.of("USER", "ADMIN"))
        .username("admin")
        .email("admin")
        .build();

        cinemaUserRepo.save(fakeAdmin);
        token = jwtService.encodeJwt(fakeAdmin);
    }

    @Test
    @Rollback
    @Transactional
    public void testUserStory() throws Exception {
        checkIfTokenValid();
        hallChecking();
        filmChecking();
    }

    @AfterEach
    public void delAdmin() {
        List<CinemaUser> admins = cinemaUserRepo.findByEmail("admin");
        if (admins.size() > 0) {
            cinemaUserRepo.deleteAll(admins);
        }
    }

    private void hallChecking() throws Exception {
        Hall hall = new Hall();
        hall.setName("aboba");

        mockMvc.perform(MockMvcRequestBuilders.post("/films/hall/").header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(hall)).contentType("application/json"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/films/hall/"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().length() > 5);
    }

    private void filmChecking() throws Exception {
        Film film = new Film();
        mockMvc.perform(MockMvcRequestBuilders.post("/films/").header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(List.of(film))).contentType("application/json"))
        .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/films/session/").header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(new SessionCreationDto())).contentType("application/json"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        SessionCreationDto viewSession = new SessionCreationDto();
        viewSession.setFilm_id(filmService.getAll().get(0).getId());
        viewSession.setHall_id(filmService.getAllHalls().get(0).getId());
        viewSession.setDate("2025-04-16T19:00:43");
        viewSession.setFormat("2D");
        viewSession.setPrice_per_sit(1455);

        mockMvc.perform(MockMvcRequestBuilders.post("/films/session/").header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(viewSession)).contentType("application/json"))
        .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private void checkIfTokenValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/").header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
