package com.reservas.user_service.Service;

import com.reservas.user_service.Repository.UserRepository;
import com.reservas.user_service.model.User;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //buscara al usuario en la bd por su correo
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        if (!user.isEnabled()) {
            throw new DisabledException("La cuenta de usuario ha sido deshabilitada o eliminada");
        }
        return user;
    }
}
