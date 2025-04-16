package com.banew.cinema_server.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.banew.cinema_server.backend.dto.LoginResponseDto;

@Service
public class AuthorizationService {

    @Autowired
    private CinemaUserService cinemaUserService;

    @Value("${google.issure_uri}")
    private String googleIssuerUri;

    @Value("${google.client_id}")
    private String googleClientId;



    private void checkResultJwtData(Jwt jwt) {
        if (!jwt.getClaimAsString("aud").equals("[" + googleClientId + "]")) {
            throw new JwtException("'aud' claim isn't equal to real google id! " + jwt.getClaimAsString("aud"));
        }
        else if (!jwt.getClaim("iss").equals("https://accounts.google.com")) {
            throw new JwtException("'iss' claim isn't equal to google! " + jwt.getClaim("iss"));
        }
    }

    public LoginResponseDto resolveGoogleCredential(String googleCredential) {

        JwtDecoder googleDecoder = NimbusJwtDecoder.withIssuerLocation(googleIssuerUri).build();
        Jwt resultJwt = googleDecoder.decode(googleCredential);
        checkResultJwtData(resultJwt);

        return cinemaUserService.processJwt(resultJwt);
    } 
}
