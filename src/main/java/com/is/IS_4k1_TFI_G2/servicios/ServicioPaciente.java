package com.is.IS_4k1_TFI_G2.servicios;

import com.is.IS_4k1_TFI_G2.modelos.Paciente;

public interface ServicioPaciente {
    void crearPaciente(Paciente paciente) throws Exception;
    void modificarPaciente(Long cuil, Paciente paciente) throws Exception;
    void eliminarPaciente(Long cuil);
}
