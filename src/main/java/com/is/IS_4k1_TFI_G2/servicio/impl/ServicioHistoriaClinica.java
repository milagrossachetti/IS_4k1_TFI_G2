package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ServicioHistoriaClinica {
    @Autowired
    private ServicioPacienteImpl servicioPacienteImpl;

    @Autowired
    private RepositorioPaciente repositorioPaciente;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    public void crearHistoriaClinica(Long cuil) throws Exception{
        //busca al paciente usando el servicio paciente
        Paciente paciente = servicioPacienteImpl.buscarPaciente(cuil);

        //verifica si el paciente tiene una hc
        if (paciente.getHistoriaClinica() != null){
            throw new Exception("El paciente ya tiene una historia clínica.");
        } else {
            //crea una hc
            HistoriaClinica historiaClinica = new HistoriaClinica(paciente);

            //asocia la hc al paciente
            paciente.setHistoriaClinica(historiaClinica);

            //guarda la hc y el paciente
            repositorioHistoriaClinica.save(historiaClinica);
            repositorioPaciente.save(paciente);

        }
    }

    //verifica que el paciente tiene hc
    public HistoriaClinica tieneHistoriaClinica(Long cuil) {
        return repositorioHistoriaClinica.findByPacienteCuil(cuil).orElse(null);
    }
}
