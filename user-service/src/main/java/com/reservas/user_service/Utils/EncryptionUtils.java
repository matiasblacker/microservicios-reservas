package com.reservas.user_service.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptionUtils {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    //al crear la contraseña
    public static String hashPassword(String password){
        if(password == null || password.isEmpty()){
            throw new IllegalArgumentException("La contraseña no puese estar vacia");
        }
        return ENCODER.encode(password);
    }

}
