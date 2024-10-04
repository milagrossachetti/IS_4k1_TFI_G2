package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;

public interface ServicioPaciente {
    void crearPaciente(Paciente paciente) throws Exception;
    void modificarPaciente(Long cuil, Paciente paciente) throws Exception;
    void eliminarPaciente(Long cuil);
}
