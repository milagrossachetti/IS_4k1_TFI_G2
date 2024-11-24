package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.TipoDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServicioDiagnostico {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioEvolucion servicioEvolucion;
    private static final Logger logger = LoggerFactory.getLogger(ServicioDiagnostico.class);

    public ServicioDiagnostico(RepositorioPaciente repositorioPaciente, ServicioEvolucion servicioEvolucion) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioEvolucion = servicioEvolucion;
    }

    public List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long cuilPaciente) {
        Paciente paciente = Optional.ofNullable(repositorioPaciente.buscarPorCuil(cuilPaciente))
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con CUIL: " + cuilPaciente));
        return Optional.ofNullable(paciente.getHistoriaClinica())
                .map(HistoriaClinica::getDiagnosticos)
                .orElseThrow(() -> new RuntimeException("El paciente no tiene una historia clínica asociada."));
    }

    public Diagnostico seleccionarDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        return obtenerDiagnosticosDelHistorialClinicoDelPaciente(cuilPaciente).stream()
                .filter(diagnostico -> diagnostico.getId().equals(diagnosticoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado para el ID: " + diagnosticoId));
    }

    private Diagnostico crearDiagnosticoConPrimeraEvolucion(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico) {
        Paciente paciente = Optional.ofNullable(repositorioPaciente.buscarPorCuil(cuilPaciente))
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con CUIL: " + cuilPaciente));
        HistoriaClinica historiaClinica = Optional.ofNullable(paciente.getHistoriaClinica())
                .orElseThrow(() -> new RuntimeException("El paciente no tiene una historia clínica asociada."));
        if (medico == null) {
            throw new RuntimeException("El usuario médico es obligatorio.");
        }

        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, historiaClinica, medico);
        historiaClinica.getDiagnosticos().add(nuevoDiagnostico);

        if (existeDiagnosticoEnHistoriaClinica(cuilPaciente, nombreDiagnostico)) {
            throw new RuntimeException("El diagnóstico ya existe en la historia clínica.");
        }

        servicioEvolucion.crearEvolucion(
                paciente.getCuil(),
                nuevoDiagnostico.getId(),
                evolucionDTO,
                medico
        );

        repositorioPaciente.guardarPaciente(paciente);
        logger.info("Diagnóstico '{}' creado para el paciente con CUIL: {}", nombreDiagnostico, cuilPaciente);
        return nuevoDiagnostico;
    }

    public Diagnostico crearDiagnosticoPermitido(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico) {
        if (!esDiagnosticoPermitido(nombreDiagnostico)) {
            throw new IllegalArgumentException("Diagnóstico no permitido: " + nombreDiagnostico);
        }
        return crearDiagnosticoConPrimeraEvolucion(cuilPaciente, nombreDiagnostico, evolucionDTO, medico);
    }

    public Diagnostico crearDiagnosticoNoPermitido(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico) {
        return crearDiagnosticoConPrimeraEvolucion(cuilPaciente, nombreDiagnostico, evolucionDTO, medico);
    }

    public boolean existeDiagnosticoEnHistoriaClinica(Long cuilPaciente, String nombreDiagnostico) {
        return obtenerDiagnosticosDelHistorialClinicoDelPaciente(cuilPaciente).stream()
                .anyMatch(diagnostico -> diagnostico.getNombreDiagnostico().equalsIgnoreCase(nombreDiagnostico));
    }

    private boolean esDiagnosticoPermitido(String nombreDiagnostico) {
        return TipoDiagnostico.obtenerTodos().stream()
                .anyMatch(tipo -> tipo.getNombre().equalsIgnoreCase(nombreDiagnostico));
    }
}
