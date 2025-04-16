package com.banew.cinema_server.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.dto.LoginResponseDto;
import com.banew.cinema_server.backend.entities.CinemaUser;
import com.banew.cinema_server.backend.repositories.CinemaUserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CinemaUserService {
    private final PasswordEncoder passwordEncoder;
    private final CinemaUserRepo userRepository;
    private final JwtService jwtService;

    public Optional<CinemaUser> getUserByUsername(String username) {
        List<CinemaUser> users = userRepository.findByUsername(username);
        if (users.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(users.get(0));
    }


    public Optional<CinemaUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Converter<Jwt, ? extends AbstractAuthenticationToken> myJwtAuthenticationConverter() {
        return (getJwt) -> {
            Long userId = Long.parseLong(Optional.ofNullable(getJwt.getSubject())
                .orElseThrow(() -> new BadCredentialsException("Getted jwt has no subject!")));

            CinemaUser foundUser = getUserById(userId)
                .orElseThrow(() -> new BadCredentialsException("User with id " + getJwt.getSubject() + " is not found!"));
            return new UsernamePasswordAuthenticationToken(foundUser, getJwt, foundUser.getAuthorities());
        };
    }

    public void save(CinemaUser user) {
        userRepository.save(user);
    }


    public void delete(CinemaUser user) {
        userRepository.delete(user);
    }


    public LoginResponseDto processJwt(Jwt resultJwt) {
        String username = resultJwt.getClaim("name");
        CinemaUser cinemaUser = getUserByUsername(username).orElseGet(() -> {
            CinemaUser user = CinemaUser.builder()
            .email(resultJwt.getClaim("email"))
            .username(resultJwt.getClaim("name"))
            .photoSrc(resultJwt.getClaim("picture"))
            .roles(Set.of("USER"))
            .build();

            this.save(user);

            return user;
        });

        return new LoginResponseDto(cinemaUser, jwtService.encodeJwt(cinemaUser));
    }
}
