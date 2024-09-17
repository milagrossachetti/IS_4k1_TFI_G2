package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelos.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.modelos.Paciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ServicioHistoriaClinica {
    @Autowired
    private ServicioPaciente servicioPaciente;

    @Autowired
    private RepositorioPaciente repositorioPaciente;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    public HistoriaClinica crearHistoriaClinica(Long cuil) throws Exception{
        //busca al paciente usando el servicio paciente
        Paciente paciente = servicioPaciente.buscarPaciente(cuil);

        //verifica si el paciente tiene una hc
        if (paciente.getHistoriaClinica() != null){
            throw new Exception("El paciente ya tiene una historia clínica.");
        }

        //crea una hc
        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setPaciente(paciente);
        paciente.setHistoriaClinica(historiaClinica);

        //guarda el paciente con la nueva hc
        repositorioPaciente.save(paciente);
        return repositorioHistoriaClinica.save(historiaClinica); //guarda la hc
    }
}
