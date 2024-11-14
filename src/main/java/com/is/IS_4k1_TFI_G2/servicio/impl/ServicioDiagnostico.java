package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ServicioDiagnostico {

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    private TipoDiagnostico tipoDiagnostico;

    // Método privado para crear diagnóstico con primera evolución sin validación de TipoDiagnostico
    private Diagnostico crearDiagnosticoConPrimeraEvolucion(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinica)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, medico);
        nuevoDiagnostico.setHistoriaClinica(historiaClinica);

        Evolucion primeraEvolucion = servicioEvolucion.crearEvolucion(nuevoDiagnostico, evolucionDTO, medico);
        nuevoDiagnostico.agregarEvolucion(primeraEvolucion);

        try {
            repositorioDiagnostico.save(nuevoDiagnostico);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el diagnóstico: " + e.getMessage(), e);
        }

        servicioEvolucion.generarPdfYGuardarRuta(primeraEvolucion);
        servicioEvolucion.enviarPdfPorEmail(primeraEvolucion, emailManual);

        return nuevoDiagnostico;
    }

    // Método público que verifica si el diagnóstico está en TipoDiagnostico
    public Diagnostico crearDiagnosticoPermitido(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        try {
            tipoDiagnostico = TipoDiagnostico.fromNombre(nombreDiagnostico);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El diagnóstico ingresado no está permitido.");
        }
        return crearDiagnosticoConPrimeraEvolucion(idHistoriaClinica, tipoDiagnostico.getNombre(), evolucionDTO, medico, emailManual);
    }

    // Método público para crear un diagnóstico no permitido en TipoDiagnostico para un paciente específico
    public Diagnostico crearDiagnosticoNoPermitido(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        return crearDiagnosticoConPrimeraEvolucion(idHistoriaClinica, nombreDiagnostico, evolucionDTO, medico, emailManual);
    }

    // Verificar si el diagnóstico ya está en la historia clínica
    public boolean existeDiagnosticoEnHistoriaClinica(Long historiaClinicaId, String nombreDiagnostico) {
        return repositorioDiagnostico.existsByNombreAndHistoriaClinicaId(nombreDiagnostico, historiaClinicaId);
    }
}
