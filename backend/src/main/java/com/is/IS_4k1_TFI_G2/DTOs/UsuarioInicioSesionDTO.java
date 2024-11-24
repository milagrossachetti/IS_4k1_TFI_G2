package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
public class UsuarioInicioSesionDTO {
    @NotNull(message = "El email es obligatorio.")
    @Email(message = "El formato del email es inválido.")
    private String email;

    @NotNull(message = "La contraseña es obligatoria.")
    private String contrasenia;
}

