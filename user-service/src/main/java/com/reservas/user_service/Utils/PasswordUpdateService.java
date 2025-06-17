package com.reservas.user_service.Utils;

import com.reservas.user_service.Repository.UserRepository;
import com.reservas.user_service.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordUpdateService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(PasswordUpdateService.class);

    public PasswordUpdateService(UserRepository userRepository) {
        this.userRepository = userRepository;
        actualizarContraseñas();
    }

    //encripta la contraseñas en la bd
    @Scheduled(fixedRate = 60 * 60 * 1000) //cada 1 hora
    private void actualizarContraseñas() {
        logger.info("Iniciando encriptación");
        List<User> users = userRepository.findAll();
        for(User user : users){
            //encriptar solo si no está encriptada
            if(user.getPassword().length() < 9){
                String contrasenaEncriptada = EncryptionUtils.hashPassword(user.getPassword());
                user.setPassword(contrasenaEncriptada);
                userRepository.save(user);

                logger.info("Contraseña actualizada para el usuario con email: {} ", user.getEmail());
            }
        }
    }
}
