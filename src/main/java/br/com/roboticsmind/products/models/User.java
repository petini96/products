package br.com.roboticsmind.products.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String keycloakId;

    private String name;

    private String nickname;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false, name = "is_profile_complete")
    private boolean isProfileComplete = false;

}