package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.modelo.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioPaciente {

    private final RepositorioPaciente repositorioPaciente;

    // Constructor con inyección de dependencias
    public ServicioPaciente(RepositorioPaciente repositorioPaciente) {

        this.repositorioPaciente = repositorioPaciente;
    }

    // Obtener un paciente por CUIL
    public Paciente obtenerPacientePorCuil(Long cuil) {
        return repositorioPaciente.buscarPorCuil(cuil)
                .orElseThrow(() -> new IllegalArgumentException("Paciente con CUIL " + cuil + " no encontrado."));
    }


    // Actualizar la historia clínica completa de un paciente
    public void actualizarHistoriaClinica(Long cuil, HistoriaClinica historiaActualizada) {
        Paciente paciente = obtenerPacientePorCuil(cuil);
        paciente.setHistoriaClinica(historiaActualizada);

        // Persistir el cambio
        repositorioPaciente.guardarPaciente(paciente);
    }

    // Obtener la historia clínica de un paciente
    public HistoriaClinica obtenerHistoriaClinica(Long cuil) {
        Paciente paciente = obtenerPacientePorCuil(cuil);
        return Optional.ofNullable(paciente.getHistoriaClinica())
                .orElseThrow(() -> new IllegalStateException("El paciente con CUIL " + cuil + " no tiene una historia clínica asociada."));
    }
}
