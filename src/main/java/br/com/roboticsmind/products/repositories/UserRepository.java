package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {

    Optional<User> findByKeycloakId(String keycloakId);

}
