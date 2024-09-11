package com.is.IS_4k1_TFI_G2.service;

import com.is.IS_4k1_TFI_G2.model.Estado;
import com.is.IS_4k1_TFI_G2.model.Paciente;

public interface ServicioPaciente {
    void crearPaciente(Paciente paciente) throws Exception;
    void modificarPaciente(Long cuil, Paciente paciente) throws Exception;
    void eliminarPaciente(Long cuil);
}
