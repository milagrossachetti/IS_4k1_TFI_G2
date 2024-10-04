package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.DTOs.PacienteDTO;
import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import org.springframework.stereotype.Service;

@Service
public interface ServicioPaciente {
    Paciente crearPaciente(Paciente paciente) throws Exception;
    Paciente modificarPaciente(Long cuil, PacienteDTO pacienteDTO) throws Exception;
    Paciente eliminarPaciente(Long cuil);
    Paciente buscarPaciente(Long cuil);
}
