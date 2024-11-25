package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;

import java.util.Optional;

public interface RepositorioUsuario {
    Optional<Usuario> buscarPorCuil(Long cuil);
    Optional<Usuario> buscarPorEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Usuario> autenticarUsuario(String email, String contrasenia);
    void guardarUsuario(Usuario usuario);
}
