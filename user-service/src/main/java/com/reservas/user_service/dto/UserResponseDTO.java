package com.reservas.user_service.dto;

import com.reservas.user_service.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private Rol rol;
}
