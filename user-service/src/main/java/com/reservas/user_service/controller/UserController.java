package com.reservas.user_service.controller;

import com.reservas.user_service.Service.UserServiceImpl;
import com.reservas.user_service.dto.UserRequestDTO;
import com.reservas.user_service.dto.UserResponseDTO;
import com.reservas.user_service.dto.UserUpdateRequestDTO;
import com.reservas.user_service.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, @AuthenticationPrincipal User user){
        try{
            UserResponseDTO response = userService.findById(id, user);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Usuario no encontrado",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "Acceso denegado",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 403
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error interno del servidor",
                            "message", "Ha ocurrido un error inesperado",
                            "timestamp", LocalDateTime.now(),
                            "status", 500
                    ));
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll(@AuthenticationPrincipal User user){
        List<UserResponseDTO> users = userService.findAll(user);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO,
                                                  @AuthenticationPrincipal User user){
        UserResponseDTO response = userService.update(id, userUpdateRequestDTO, user);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal User user){
        try{
            userService.delete(id, user);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario eliminado exitosamente",
                    "success", true
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", "Usuario no encontrado",
                            "error", e.getMessage(),
                            "success", false
                    ));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message", "Error en los datos proporcionados",
                            "error", e.getMessage(),
                            "success", false
                    ));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Error interno del servidor",
                            "error", e.getMessage(),
                            "success", false
                    ));
        }
    }


}
