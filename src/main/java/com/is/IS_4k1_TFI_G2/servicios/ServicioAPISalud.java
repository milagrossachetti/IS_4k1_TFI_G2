package com.is.IS_4k1_TFI_G2.servicios;

public interface ServicioAPISalud {
    boolean verificarObraSocial(Long id);
    boolean verificarNumeroAfiliado(Long id, String nroAfiliado);
}