package com.banew.cinema_server.backend.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banew.cinema_server.backend.dto.BookingInfoDto;
import com.banew.cinema_server.backend.dto.LoginResponseDto;
import com.banew.cinema_server.backend.dto.ViewSessionInfoDto;
import com.banew.cinema_server.backend.entities.CinemaUser;
import com.banew.cinema_server.backend.repositories.BookingRepo;
import com.banew.cinema_server.backend.repositories.CinemaUserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CinemaUserService {
    private final CinemaUserRepo userRepository;
    private final JwtService jwtService;
    private final BookingRepo bookingRepo;
    private final UserServiceProperties userServiceProperties;

    public Optional<CinemaUser> getUserByEmail(String email) {
        List<CinemaUser> users = userRepository.findByEmail(email);
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
        String email = resultJwt.getClaim("email");
        CinemaUser cinemaUser = getUserByEmail(email).orElseGet(() -> {
            Set<String> roles = new HashSet<>();
            roles.add("USER");

            if (userServiceProperties.getAdminEmails().contains(resultJwt.getClaim("email"))) {
                roles.add("ADMIN");
            }

            CinemaUser user = CinemaUser.builder()
            .email(resultJwt.getClaim("email"))
            .username(resultJwt.getClaim("name"))
            .photoSrc(resultJwt.getClaim("picture"))
            .roles(roles)
            .build();

            this.save(user);
            return user;
        });

        cinemaUser.setUsername(resultJwt.getClaim("name"));
        cinemaUser.setPhotoSrc(resultJwt.getClaim("picture"));

        return new LoginResponseDto(cinemaUser, jwtService.encodeJwt(cinemaUser));
    }

    @Transactional
    public List<ViewSessionInfoDto> getBookingSessionsByUser(CinemaUser cinemaUser) {
        return bookingRepo.findByCinemaUser(cinemaUser).stream()
        .map(booking -> ViewSessionInfoDto.fromViewSession(booking.getViewSession()))
        .distinct()
        .toList();
    }
}
