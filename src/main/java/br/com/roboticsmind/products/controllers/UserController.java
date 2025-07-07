package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.user.UserStatusDto;
import br.com.roboticsmind.products.exceptions.UserNotFoundException;
import br.com.roboticsmind.products.models.User;
import br.com.roboticsmind.products.repositories.ProductRepository;
import br.com.roboticsmind.products.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me/status")
    public ResponseEntity<UserStatusDto> getUserStatus(JwtAuthenticationToken authentication) {
        String keycloakId = authentication.getToken().getSubject(); // Pega o 'sub' (ID do Keycloak)

        User appUser = userRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
            // Lógica para criar um novo usuário (JIT Provisioning)
            String email = authentication.getToken().getClaimAsString("email");
            String name = authentication.getToken().getClaimAsString("name"); // "name" ou "given_name" + "family_name"

            User newUser = new User();
            newUser.setKeycloakId(keycloakId);
            newUser.setEmail(email);
            newUser.setName(name); // Pode ser nulo se não vier no token
            newUser.setProfileComplete(false); // Perfil obviamente não está completo

            return userRepository.save(newUser);
        });

        UserStatusDto status = new UserStatusDto(appUser.isProfileComplete());
        return ResponseEntity.ok(status);
    }
}