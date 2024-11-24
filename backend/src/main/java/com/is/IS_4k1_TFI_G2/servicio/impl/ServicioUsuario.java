package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.ServicioUsuarioInterface;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioUsuario implements ServicioUsuarioInterface {

    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    public ServicioUsuario(RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder) {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica un usuario verificando su email y contraseña.
     *
     * @param email        el email del usuario.
     * @param contrasenia  la contraseña del usuario.
     * @return Usuario autenticado si las credenciales son válidas.
     * @throws IllegalArgumentException si el usuario no existe o las credenciales son incorrectas.
     */
    @Override
    public Usuario autenticarUsuario(String email, String contrasenia) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        if (contrasenia == null || contrasenia.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        Optional<Usuario> usuarioOptional = repositorioUsuario.buscarPorEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // Verificar contraseña
            if (passwordEncoder.matches(contrasenia, usuario.getContrasenia())) {
                return usuario; // Usuario autenticado con éxito
            } else {
                throw new IllegalArgumentException("Contraseña incorrecta.");
            }
        } else {
            throw new IllegalArgumentException("Usuario no encontrado con el email: " + email);
        }
    }
}
