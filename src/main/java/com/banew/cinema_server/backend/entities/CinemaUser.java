package com.banew.cinema_server.backend.entities;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CinemaUser {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;

    private String photoSrc;
    private Set<String> roles;
 
    @OneToMany(mappedBy = "cinemaUser", fetch = FetchType.EAGER)
    private List<Booking> bookings;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map((e) -> new SimpleGrantedAuthority("ROLE_" + e)).toList();
    }
}
