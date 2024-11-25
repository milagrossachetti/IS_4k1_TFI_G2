package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.excepciones.*;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.*;
import com.is.IS_4k1_TFI_G2.repositorio.apiSalud.RepositorioCieDiez;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioDiagnostico {

    private final RepositorioPaciente repositorioPaciente;
    private final ServicioEvolucion servicioEvolucion;
    private final RepositorioCieDiez repositorioCieDiez;

    private static final Logger logger = LoggerFactory.getLogger(ServicioDiagnostico.class);

    public ServicioDiagnostico(RepositorioPaciente repositorioPaciente, ServicioEvolucion servicioEvolucion, RepositorioCieDiez repositorioCieDiez) {
        this.repositorioPaciente = repositorioPaciente;
        this.servicioEvolucion = servicioEvolucion;
        this.repositorioCieDiez = repositorioCieDiez;
    }

    public List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long cuilPaciente) {
        Paciente paciente = repositorioPaciente.buscarPorCuil(cuilPaciente)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente));
        HistoriaClinica historiaClinica = paciente.getHistoriaClinica();
        if (historiaClinica == null) {
            throw new HistoriaClinicaNoEncontradaException("El paciente no tiene una historia clínica asociada.");
        }
        return historiaClinica.getDiagnosticos();
    }

    public Diagnostico seleccionarDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        return obtenerDiagnosticosDelHistorialClinicoDelPaciente(cuilPaciente).stream()
                .filter(diagnostico -> diagnostico.getId().equals(diagnosticoId))
                .findFirst()
                .orElseThrow(() -> new DiagnosticoNoEncontradoException("Diagnóstico no encontrado para el ID: " + diagnosticoId));
    }

    private Diagnostico crearDiagnosticoConPrimeraEvolucion(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico) {
        if (medico == null) {
            throw new UsuarioNoAutenticadoException("El usuario médico es obligatorio.");
        }

        Paciente paciente = repositorioPaciente.buscarPorCuil(cuilPaciente)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuilPaciente));

        HistoriaClinica historiaClinica = paciente.getHistoriaClinica();
        if (historiaClinica == null) {
            throw new HistoriaClinicaNoEncontradaException("El paciente no tiene una historia clínica asociada.");
        }

        if (existeDiagnosticoEnHistoriaClinica(cuilPaciente, nombreDiagnostico)) {
            throw new DiagnosticoDuplicadoException("El diagnóstico ya existe en la historia clínica.");
        }

        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, historiaClinica, medico);
        historiaClinica.getDiagnosticos().add(nuevoDiagnostico);

        repositorioPaciente.guardarPaciente(paciente);

        servicioEvolucion.crearEvolucion(
                cuilPaciente,
                nuevoDiagnostico.getId(),
                evolucionDTO,
                medico
        );

        logger.info("Diagnóstico '{}' creado para el paciente con CUIL: {}", nombreDiagnostico, cuilPaciente);
        return nuevoDiagnostico;
    }

    public Diagnostico crearDiagnosticoPermitido(Long cuilPaciente, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico) {
        if (!esDiagnosticoPermitido(nombreDiagnostico)) {
            throw new DiagnosticoNoPermitidoException("Diagnóstico no permitido: " + nombreDiagnostico);
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
        return repositorioCieDiez.obtenerTodos().stream()
                .anyMatch(tipo -> tipo.getNombreDiagnostico().equalsIgnoreCase(nombreDiagnostico));
    }

}
