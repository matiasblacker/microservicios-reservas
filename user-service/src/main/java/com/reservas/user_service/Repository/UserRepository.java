package com.reservas.user_service.Repository;

import com.reservas.user_service.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(@Email(message = "el email no tiene un formato válido")String email);
}
