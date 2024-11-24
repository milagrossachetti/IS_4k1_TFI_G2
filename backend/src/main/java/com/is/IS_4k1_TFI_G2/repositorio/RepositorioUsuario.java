package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.UsuarioDato;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RepositorioUsuario {

    // Buscar usuario por CUIL
    public Optional<Usuario> buscarPorCuil(Long cuil) {
        return UsuarioDato.buscarPorCuil(cuil);
    }

    // Buscar usuario por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return UsuarioDato.buscarPorEmail(email);
    }

    // Verificar si un usuario existe por email
    public Boolean existsByEmail(String email) {
        return UsuarioDato.buscarPorEmail(email).isPresent();
    }

    // Autenticar usuario por email y contraseña
    public Optional<Usuario> autenticarUsuario(String email, String contrasenia) {
        return UsuarioDato.autenticarUsuario(email, contrasenia);
    }

    // Guardar un nuevo usuario (opcional, por si se requiere en el futuro)
    public void guardarUsuario(Usuario usuario) {
        UsuarioDato.USUARIOS.add(usuario);
    }

    // Eliminar usuario por CUIL (opcional, por si se requiere en el futuro)
    public void eliminarUsuario(Long cuil) {
        UsuarioDato.buscarPorCuil(cuil).ifPresent(UsuarioDato.USUARIOS::remove);
    }
}
