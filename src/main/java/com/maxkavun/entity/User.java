package com.maxkavun.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private Set<Session> sessions = new HashSet<>();


}
