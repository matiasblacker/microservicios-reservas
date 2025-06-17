package com.reservas.user_service.Utils;

import java.security.SecureRandom;

public class PasswordUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuxyz0123456789@#$%";
    private static final int PASSWORD_LENGHT = 8;

    public static String generatedSecuredPassword(){
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for(int i = 0; i < PASSWORD_LENGHT; i++){
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }
}
