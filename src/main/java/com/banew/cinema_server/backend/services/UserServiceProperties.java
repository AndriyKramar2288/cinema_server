package com.banew.cinema_server.backend.services;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "usr-service")
@Getter @Setter
public class UserServiceProperties {
    private List<String> adminEmails;
    private List<String> workerEmails;
}
