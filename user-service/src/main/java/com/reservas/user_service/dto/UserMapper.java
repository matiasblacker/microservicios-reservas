package com.reservas.user_service.dto;

import com.reservas.user_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", expression = "java(user.getName() + \" \" + user.getLastName())")
    @Mapping(source = "rol", target = "rol")
    UserResponseDTO userToUserResponseDTO(User user);

    @Mapping(target = "id", ignore = true) // ID se genera automáticamente
    @Mapping(target = "password", ignore = true) // Se maneja separadamente
    @Mapping(target = "rol", ignore = true) // Se asigna por defecto en el servicio
    User userRequestDTOTouser(UserRequestDTO userRequestDTO);

    UserRequestDTO userToUserRequestDTO(User user);
}
