package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.user.AppUserStatusDto;
import br.com.roboticsmind.products.dto.user.AppUserUpdateDto;
import br.com.roboticsmind.products.models.AppUser;
import br.com.roboticsmind.products.repositories.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping("/me/status")
    public ResponseEntity<AppUserStatusDto> getUserStatus(JwtAuthenticationToken authentication) {
        AppUser appUser = findOrCreateUser(authentication);
        AppUserStatusDto status = new AppUserStatusDto(appUser.isProfileComplete());
        return ResponseEntity.ok(status);
    }

    @PutMapping("/me")
    public ResponseEntity<AppUser> updateProfile(
            JwtAuthenticationToken authentication,
            @Valid @RequestBody AppUserUpdateDto userUpdateDto) {

        AppUser userToUpdate = findOrCreateUser(authentication);

        userToUpdate.setName(userUpdateDto.getName());
        userToUpdate.setNickname(userUpdateDto.getNickname());
        userToUpdate.setPhone(userUpdateDto.getPhone());
        userToUpdate.setProfileComplete(true);

        AppUser updatedUser = userRepository.save(userToUpdate);

        return ResponseEntity.ok(updatedUser);
    }

    private AppUser findOrCreateUser(JwtAuthenticationToken authentication) {
        String keycloakId = authentication.getToken().getSubject();

        return userRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
            String email = authentication.getToken().getClaimAsString("email");
            String name = authentication.getToken().getClaimAsString("name");

            AppUser newUser = new AppUser();
            newUser.setKeycloakId(keycloakId);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProfileComplete(false);

            return userRepository.save(newUser);
        });
    }
}
