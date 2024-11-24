package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;

public interface ServicioUsuarioInterface {
    /**
     * Autentica un usuario verificando sus credenciales.
     *
     * @param email        el email del usuario.
     * @param contrasenia  la contraseña del usuario.
     * @return Usuario autenticado.
     */
    Usuario autenticarUsuario(String email, String contrasenia);
}
