package com.is.IS_4k1_TFI_G2.service.impl;

import com.is.IS_4k1_TFI_G2.model.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.model.Paciente;
import com.is.IS_4k1_TFI_G2.repository.HistoriaClinicaRepository;
import com.is.IS_4k1_TFI_G2.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class HistoriaClinicaService {
    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    public HistoriaClinica crearHistoriaClinica(Long cuil) throws Exception{
        //busca al paciente usando el servicio paciente
        Paciente paciente = pacienteService.buscarPaciente(cuil);

        //verifica si el paciente tiene una hc
        if (paciente.getHistoriaClinica() != null){
            throw new Exception("El paciente ya tiene una historia clínica.");
        }

        //crea una hc
        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setPaciente(paciente);
        paciente.setHistoriaClinica(historiaClinica);

        //guarda el paciente con la nueva hc
        pacienteService.save(paciente);
        return historiaClinicaRepository.save(historiaClinica); //guarda la hc
    }


}
