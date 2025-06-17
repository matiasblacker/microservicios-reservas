package com.reservas.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank(message = "El email no puede estar en blanco")
    private String email;
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String password;
}
