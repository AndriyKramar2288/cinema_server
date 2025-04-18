package com.banew.cinema_server.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banew.cinema_server.backend.dto.LoginRequestDto;
import com.banew.cinema_server.backend.dto.LoginResponseDto;
import com.banew.cinema_server.backend.dto.SingleStringResponse;
import com.banew.cinema_server.backend.services.AuthorizationService;

@RestController
public class AuthorizationController {
    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/auth/google_id_token")
    public LoginResponseDto resolveGoogleToken(@RequestBody LoginRequestDto request) {
        return authorizationService.resolveGoogleCredential(request.getToken());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public SingleStringResponse badJwt(JwtException jwtException) {
        return new SingleStringResponse(jwtException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
