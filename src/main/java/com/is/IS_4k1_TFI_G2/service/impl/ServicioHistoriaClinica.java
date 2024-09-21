package com.is.IS_4k1_TFI_G2.service.impl;

import com.is.IS_4k1_TFI_G2.model.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.model.Paciente;
import com.is.IS_4k1_TFI_G2.repository.RepositorioHistoriaClinica;
import com.is.IS_4k1_TFI_G2.repository.RepositorioPaciente;
import com.is.IS_4k1_TFI_G2.service.impl.ServicioPacienteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ServicioHistoriaClinica {
    @Autowired
    private ServicioPacienteImpl servicioPacienteImpl;

    @Autowired
    private RepositorioPaciente repositorioPaciente;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    public HistoriaClinica crearHistoriaClinica(Long cuil) throws Exception{
        //busca al paciente usando el servicio paciente
        Paciente paciente = servicioPacienteImpl.buscarPaciente(cuil);

        //verifica si el paciente tiene una hc
        if (paciente.getHistoriaClinica() != null){
            throw new Exception("El paciente ya tiene una historia clínica.");
        } else {
            //crea una hc
            HistoriaClinica historiaClinica = new HistoriaClinica();
            historiaClinica.setPaciente(paciente);
            paciente.setHistoriaClinica(historiaClinica);

            //asocia la hc al paciente
            paciente.setHistoriaClinica(historiaClinica);

            //guarda la hc y el paciente
            repositorioHistoriaClinica.save(historiaClinica);
            repositorioPaciente.save(paciente);

            return historiaClinica;
        }

    }
}
