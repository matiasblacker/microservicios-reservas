package com.reservas.user_service.controller;

import com.reservas.user_service.Service.UserService;
import com.reservas.user_service.config.jwt.JwtUtil;
import com.reservas.user_service.dto.*;
import com.reservas.user_service.model.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(user);

            //devolver el token
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    user.getId(),
                    user.getName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRol()
            ));
        }catch(Exception e){
            //log de error
            logger.info("error en el login: " + e.getMessage());
            logger.info("Intento de login con el email: {}", loginRequest.getEmail());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> save(@Valid @RequestBody UserRequestDTO userRequestDTO){
        try {
            UserResponseDTO userCreated = userService.save(userRequestDTO);
            logger.info("Usuario registrado exitosamente: {}", userCreated.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

        } catch (IllegalArgumentException e) {
            // Errores de validación de negocio (email duplicado, etc.)
            logger.warn("Error de validación en registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación en registro");

        }
    }

}
