package com.banew.cinema_server.backend.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

// @Data
// @Entity
// public class CinemaViewer {
//     @Id
//     @GeneratedValue
//     private Long id;
//     private String email;
//     private String phoneNumber;
//     private String fullName;
// }
