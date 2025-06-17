package com.reservas.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 50, message = "El nombre no puede tener mas de 50 caracteres")
    private String name;

    @NotBlank(message = "El apellido no puede estar en blanco")
    @Size(max = 50, message = "El apellido no puede tener mas de 50 caracteres")
    private String lastName;

    @NotBlank(message = "El email no puede estar en blanco")
    @Email
    private String email;

    @Size(min = 8, max = 16, message = "La contraseña debe tener entre 8 y 16 caracteres")
    private String password;

    private Boolean enabled;
}
