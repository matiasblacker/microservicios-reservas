package com.reservas.user_service.Service;

import com.reservas.user_service.Repository.UserRepository;
import com.reservas.user_service.Utils.EmailService;
import com.reservas.user_service.Utils.EncryptionUtils;
import com.reservas.user_service.Utils.PasswordUtils;
import com.reservas.user_service.dto.UserMapper;
import com.reservas.user_service.dto.UserRequestDTO;
import com.reservas.user_service.dto.UserResponseDTO;
import com.reservas.user_service.dto.UserUpdateRequestDTO;
import com.reservas.user_service.model.Rol;
import com.reservas.user_service.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {

        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        String generatedPassword = PasswordUtils.generatedSecuredPassword();
        //crear usuario
        User user = userMapper.userRequestDTOTouser(userRequestDTO);
        user.setPassword(EncryptionUtils.hashPassword(generatedPassword));
        //asignar rol por defecto
        user.setRol(Rol.USER);

        //guardar y enviar email
        User savedUser = userRepository.save(user);
        enviarCredencialesporEmail(savedUser, generatedPassword);

        return userMapper.userToUserResponseDTO(savedUser);
    }

    private void enviarCredencialesporEmail(User user, String password) {
        String emailContent = String.format(
                "Bienvenido %s %s,\n\n" + " gracias por registrarte. " +
                        "Tus credenciales de acceso son: \n\n" +
                        "Email: %s\n" +
                        "contraseña temporal: %s\n\n" +
                        "Recuerda cambiar tu contraseña de inicio de sesión.",
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                password
        );
        emailService.sendEmail(
                user.getEmail(),
                "credenciales acceso sistema de reservas",
                emailContent
        );
    }

    @Override
    public UserResponseDTO findById(Long id, User usuarioSolicitante) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
        // validar rol
        if (usuarioSolicitante.getRol() == Rol.ADMIN) {
            // ADMIN puede consultar cualquier usuario (incluido él mismo)
        } else if (usuarioSolicitante.getRol() == Rol.USER) {
            // USER solo puede consultar su propia información
            if (!usuarioSolicitante.getId().equals(id)) {
                throw new AccessDeniedException("Solo puedes consultar tu propia información");
            }
        } else {
            // Cualquier otro rol no tiene permisos de consulta
            throw new AccessDeniedException("No tienes permisos para consultar información de usuarios");
        }
        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> findAll(User usuarioSolicitante) {
        // validar rol
        if(usuarioSolicitante.getRol() != Rol.ADMIN){
            throw new AccessDeniedException("No tienes permisos para consultar");
        }
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserUpdateRequestDTO userUpdateRequestDTO, User usuarioActualizador) {
        User userToUpdated = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));

        //validar permisos para actualizar
        if (!usuarioActualizador.getRol().equals(Rol.ADMIN) &&
                !usuarioActualizador.getId().equals(userToUpdated.getId())) {
            throw new SecurityException("No puedes editar a otro usuario");
        }
        //validar email único
        if(userUpdateRequestDTO.getEmail() != null &&
            !userUpdateRequestDTO.getEmail().equals(userToUpdated.getEmail()) &&
                userRepository.existsByEmail(userUpdateRequestDTO.getEmail())){
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (userUpdateRequestDTO.getName() != null && !userUpdateRequestDTO.getName().trim().isEmpty()) {
            userToUpdated.setName(userUpdateRequestDTO.getName().trim());}
        if (userUpdateRequestDTO.getLastName() != null && !userUpdateRequestDTO.getLastName().trim().isEmpty()) {
            userToUpdated.setLastName(userUpdateRequestDTO.getLastName().trim());}
        if (userUpdateRequestDTO.getEmail() != null && !userUpdateRequestDTO.getEmail().trim().isEmpty()) {
            userToUpdated.setEmail(userUpdateRequestDTO.getEmail().trim());}
        if(userUpdateRequestDTO.getPassword() != null && !userUpdateRequestDTO.getPassword().trim().isEmpty()){
            userToUpdated.setPassword(EncryptionUtils.hashPassword(userUpdateRequestDTO.getPassword().trim()));
        }
        if(userUpdateRequestDTO.getEnabled() != null){
            userToUpdated.setEnabled(userUpdateRequestDTO.getEnabled());
        }
        User userUpdated = userRepository.save(userToUpdated);
        return userMapper.userToUserResponseDTO(userUpdated);
    }

    @Override
    @Transactional
    public void delete(Long id, User usuarioActual) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));

        //solo puede eliminar a usuarios
        if(usuarioActual.getRol() == Rol.ADMIN){
            // ADMIN puede:
            // 1. Eliminarse a sí mismo
            // 2. Eliminar usuarios de tipo USER
            if (!usuarioActual.getId().equals(userToDelete.getId()) && userToDelete.getRol() != Rol.USER) {
                throw new AccessDeniedException("Como ADMIN solo puedes eliminar usuarios de tipo USER o eliminarte a ti mismo");
            }
        }else if (usuarioActual.getRol() == Rol.USER) {
            // USER solo puede eliminarse a sí mismo
            if (!usuarioActual.getId().equals(userToDelete.getId())) {
                throw new AccessDeniedException("Solo puedes eliminarte a ti mismo");
            }
        } else {
            // Cualquier otro rol no tiene permisos de eliminación
            throw new AccessDeniedException("No tienes permisos para eliminar usuarios");
        }
        //eliminación lógica
        userToDelete.setEnabled(false); //ya no podrá logearse
        userRepository.save(userToDelete);

    }
}
