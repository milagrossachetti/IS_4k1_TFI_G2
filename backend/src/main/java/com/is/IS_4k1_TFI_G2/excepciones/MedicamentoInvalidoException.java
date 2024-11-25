package com.is.IS_4k1_TFI_G2.excepciones;

/**
 * Excepción lanzada cuando se intenta usar un medicamento no válido.
 */
public class MedicamentoInvalidoException extends RuntimeException {
    public MedicamentoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
