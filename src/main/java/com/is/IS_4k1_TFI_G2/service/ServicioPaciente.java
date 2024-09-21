package com.is.IS_4k1_TFI_G2.service;

import com.is.IS_4k1_TFI_G2.model.Paciente;
import com.is.IS_4k1_TFI_G2.repository.RepositorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.is.IS_4k1_TFI_G2.model.Estado;

@Service
public interface ServicioPaciente {
    void crearPaciente(Paciente paciente) throws Exception;
    void modificarPaciente(Long cuil, Paciente paciente) throws Exception;
    void eliminarPaciente(Long cuil);
}
