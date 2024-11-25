package com.is.IS_4k1_TFI_G2.repositorio.memoria;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import org.springframework.stereotype.Repository;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RepositorioUsuarioMemoria implements RepositorioUsuario {

    private final List<Usuario> usuarios = new ArrayList<>();

    // Inicialización de datos
    public RepositorioUsuarioMemoria() {
        usuarios.add(new Usuario(
                20123456789L, 12345678L, 12345L, "Cardiología", "Dr. Juan Pérez",
                "juan.perez@hospital.com", "password123", 1145678901L, "Argentina",
                "CABA", "Av. Siempre Viva 123", "Buenos Aires", "MEDICO"
        ));

        usuarios.add(new Usuario(
                20345678901L, 87654321L, null, null, "Ana López",
                "ana.lopez@hospital.com", "password456", 1140987654L, "Argentina",
                "CABA", "Calle Falsa 456", "Buenos Aires", "RECEPCIONISTA"
        ));

        usuarios.add(new Usuario(
                20456789012L, 11223344L, 67890L, "Pediatría", "Dr. Pedro Martínez",
                "pedro.martinez@hospital.com", "password789", 1143210987L, "Argentina",
                "Rosario", "San Martín 789", "Santa Fe", "MEDICO"
        ));
    }

    @Override
    public Optional<Usuario> buscarPorCuil(Long cuil) {
        return usuarios.stream()
                .filter(usuario -> usuario.getCuil().equals(cuil))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return buscarPorEmail(email).isPresent();
    }

    @Override
    public Optional<Usuario> autenticarUsuario(String email, String contrasenia) {
        return usuarios.stream()
                .filter(usuario -> usuario.getEmail().equals(email) && usuario.getContrasenia().equals(contrasenia))
                .findFirst();
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        buscarPorCuil(usuario.getCuil())
                .ifPresentOrElse(
                        existente -> {
                            int index = usuarios.indexOf(existente);
                            usuarios.set(index, usuario);
                        },
                        () -> usuarios.add(usuario)
                );
    }
}
