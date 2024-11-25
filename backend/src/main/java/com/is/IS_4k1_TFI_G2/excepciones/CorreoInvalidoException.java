package com.is.IS_4k1_TFI_G2.excepciones;

/**
 * Excepción lanzada cuando se intenta enviar un correo no válido.
 */
public class CorreoInvalidoException extends RuntimeException {
    public CorreoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
