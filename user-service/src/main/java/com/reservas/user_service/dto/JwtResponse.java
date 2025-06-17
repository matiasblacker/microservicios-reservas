package com.reservas.user_service.dto;

import com.reservas.user_service.model.Rol;
import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Rol rol;

    public JwtResponse(String token, Long id, String name, String lastName, String email, Rol rol) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.rol = rol;
    }
}
