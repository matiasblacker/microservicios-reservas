package com.reservas.user_service.config.jwt;

import com.reservas.user_service.Service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = extractToken(request);

            if (token != null) {
                // 1. Validar estructura del token (firma, formato)
                if (jwtUtil.validateTokenStructure(token)) {
                    String username = jwtUtil.extractUsername(token);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        try{
                            // 2. Cargar detalles del usuario UNA sola vez
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            // 3. Validar token contra los detalles del usuario
                            if (jwtUtil.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authToken =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities()
                                        );

                                authToken.setDetails(
                                        new WebAuthenticationDetailsSource().buildDetails(request)
                                );

                                SecurityContextHolder.getContext().setAuthentication(authToken);
                            }
                        } catch (DisabledException e) {
                            // Usuario deshabilitado - devolver error 401 con mensaje
                            logger.warn("Intento de acceso con cuenta deshabilitada: " + username);

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(
                                    "{\"error\": \"Su cuenta ha sido deshabilitada\", " +
                                            "\"message\": \"Contacte al administrador para más información\", " +
                                            "\"success\": false}"
                            );
                            return;
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
