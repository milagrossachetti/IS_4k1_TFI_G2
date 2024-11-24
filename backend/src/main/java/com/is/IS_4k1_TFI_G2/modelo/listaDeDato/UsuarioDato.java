package com.is.IS_4k1_TFI_G2.modelo.listaDeDato;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDato {

    // Lista estática que simula el repositorio de usuarios
    public static final List<Usuario> USUARIOS = new ArrayList<>();

    // Inicialización de datos
    static {
        USUARIOS.add(new Usuario(
                20123456789L, // CUIL (Long)
                12345678L,    // DNI (Long)
                12345L,       // Matrícula (Long)
                "Cardiología", // Especialidad (String)
                "Dr. Juan Pérez", // Nombre completo (String)
                "juan.perez@hospital.com", // Email (String)
                "password123", // Contraseña (String)
                1145678901L,   // Teléfono (Long)
                "Argentina",   // País (String)
                "CABA",        // Localidad (String)
                "Av. Siempre Viva 123", // Dirección (String)
                "Buenos Aires", // Provincia (String)
                "MEDICO"       // Rol (String)
        ));

        USUARIOS.add(new Usuario(
                20345678901L, // CUIL (Long)
                87654321L,    // DNI (Long)
                null,         // Matrícula (Long)
                null,         // Especialidad (String)
                "Ana López",  // Nombre completo (String)
                "ana.lopez@hospital.com", // Email (String)
                "password456", // Contraseña (String)
                1140987654L,   // Teléfono (Long)
                "Argentina",   // País (String)
                "CABA",        // Localidad (String)
                "Calle Falsa 456", // Dirección (String)
                "Buenos Aires", // Provincia (String)
                "RECEPCIONISTA" // Rol (String)
        ));

        USUARIOS.add(new Usuario(
                20456789012L, // CUIL (Long)
                11223344L,    // DNI (Long)
                67890L,       // Matrícula (Long)
                "Pediatría",  // Especialidad (String)
                "Dr. Pedro Martínez", // Nombre completo (String)
                "pedro.martinez@hospital.com", // Email (String)
                "password789", // Contraseña (String)
                1143210987L,   // Teléfono (Long)
                "Argentina",   // País (String)
                "Rosario",     // Localidad (String)
                "San Martín 789", // Dirección (String)
                "Santa Fe",    // Provincia (String)
                "MEDICO"       // Rol (String)
        ));
    }

    // Método para buscar un usuario por email
    public static Optional<Usuario> buscarPorEmail(String email) {
        return USUARIOS.stream()
                .filter(usuario -> usuario.getEmail().equals(email))
                .findFirst();
    }

    // Método para buscar un usuario por CUIL
    public static Optional<Usuario> buscarPorCuil(Long cuil) {
        return USUARIOS.stream()
                .filter(usuario -> usuario.getCuil().equals(cuil))
                .findFirst();
    }

    // Método para autenticar un usuario por email y contraseña
    public static Optional<Usuario> autenticarUsuario(String email, String contrasenia) {
        return USUARIOS.stream()
                .filter(usuario -> usuario.getEmail().equals(email) && usuario.getContrasenia().equals(contrasenia))
                .findFirst();
    }
}
