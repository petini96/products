package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.admin.RoleDTO;
import br.com.roboticsmind.products.dto.admin.UserDTO;
import br.com.roboticsmind.products.services.KeycloakAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        return ResponseEntity.ok(keycloakAdminService.listUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> listAvailableRoles() {
        return ResponseEntity.ok(keycloakAdminService.listAvailableClientRoles());
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleDTO>> getUserRoles(@PathVariable String userId) {
        return ResponseEntity.ok(keycloakAdminService.getUserClientRoles(userId));
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> updateUserRoles(@PathVariable String userId, @RequestBody List<RoleDTO> roles) {
        keycloakAdminService.updateUserClientRoles(userId, roles);
        return ResponseEntity.noContent().build();
    }
}