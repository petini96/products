package br.com.roboticsmind.products.dto.admin;

public record UserDTO(
        String id,
        String username,
        String email,
        String firstName,
        String lastName
) {}