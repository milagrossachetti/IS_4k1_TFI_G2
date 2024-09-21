package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.servicio.ServicioAPISalud;
import org.springframework.stereotype.Service;

@Service
public class ServicioAPISaludImpl implements ServicioAPISalud{
    @Override
    public boolean verificarMatriculaMedica(String matriculaMedica) {
        return true;
    }
}
