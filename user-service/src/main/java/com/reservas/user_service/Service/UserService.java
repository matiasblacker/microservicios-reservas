package com.reservas.user_service.Service;

import com.reservas.user_service.dto.UserRequestDTO;
import com.reservas.user_service.dto.UserResponseDTO;
import com.reservas.user_service.dto.UserUpdateRequestDTO;
import com.reservas.user_service.model.User;

import java.util.List;

public interface UserService {

    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO findById(Long id, User usuarioSolicitante);
    List<UserResponseDTO> findAll(User usuarioSolicitante);
    UserResponseDTO update(Long id, UserUpdateRequestDTO userUpdateRequestDTO, User usuarioActualizador);
    void delete(Long id, User usuarioActual);

}
