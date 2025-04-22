package com.maxkavun.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private Set<Location> locations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private Set<Session> sessions = new HashSet<>();
}
