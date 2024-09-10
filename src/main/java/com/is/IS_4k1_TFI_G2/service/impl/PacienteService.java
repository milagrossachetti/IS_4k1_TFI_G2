package com.is.IS_4k1_TFI_G2.service.impl;

import com.is.IS_4k1_TFI_G2.model.Paciente;
import com.is.IS_4k1_TFI_G2.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente buscarPaciente(Long cuil) {
        return pacienteRepository.findByCuil(cuil);
    }
}
