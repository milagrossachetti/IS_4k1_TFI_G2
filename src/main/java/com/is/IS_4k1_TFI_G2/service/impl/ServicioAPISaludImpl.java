package com.is.IS_4k1_TFI_G2.service.impl;

import com.is.IS_4k1_TFI_G2.service.ServicioAPISalud;
import org.springframework.stereotype.Service;

@Service
public class ServicioAPISaludImpl implements ServicioAPISalud {

    @Override
    public boolean verificarObraSocial(Long id) {
        return true;
    }

    @Override
    public boolean verificarNumeroAfiliado(Long id, String nroAfiliado) {
        return true;
    }
}
