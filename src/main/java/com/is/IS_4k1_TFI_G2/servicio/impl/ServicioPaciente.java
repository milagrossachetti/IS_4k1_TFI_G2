package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelos.Paciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;

public class ServicioPaciente {
    @Autowired
    private RepositorioPaciente repositorioPaciente;

    Paciente buscarPaciente(Long cuil) {
        return repositorioPaciente.findByCuil(cuil);
    }
}
