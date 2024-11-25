package com.is.IS_4k1_TFI_G2.excepciones;

/**
 * Excepción lanzada cuando no hay un usuario autenticado.
 */
public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException(String mensaje) {
        super(mensaje);
    }
}
