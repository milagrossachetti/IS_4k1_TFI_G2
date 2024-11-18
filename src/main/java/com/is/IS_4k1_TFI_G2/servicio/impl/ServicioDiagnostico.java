package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class ServicioDiagnostico {

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    private TipoDiagnostico tipoDiagnostico;

    @Transactional
    private Diagnostico crearDiagnosticoConPrimeraEvolucion(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinica)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        if (nombreDiagnostico == null || nombreDiagnostico.isEmpty()) {
            throw new IllegalArgumentException("El nombre del diagnóstico no puede ser nulo.");
        }
        if (repositorioDiagnostico.findByNombreAndHistoriaClinicaId(nombreDiagnostico, idHistoriaClinica).isPresent()) {
            throw new IllegalArgumentException("Ya existe un diagnóstico con ese nombre.");
        }

        if ((evolucionDTO == null) ||
                (evolucionDTO.getTexto() == null || evolucionDTO.getTexto().isEmpty()) &&
                (evolucionDTO.getPlantillaControl() == null ||
                        evolucionDTO.getPlantillaControl().getPeso() == null ||
                        evolucionDTO.getPlantillaControl().getAltura() == null ||
                        evolucionDTO.getPlantillaControl().getPresion() == null ||
                        evolucionDTO.getPlantillaControl().getPulso() == null ||
                        evolucionDTO.getPlantillaControl().getSaturacion() == null ||
                        evolucionDTO.getPlantillaControl().getNivelAzucar() == null)) {
            throw new IllegalArgumentException("La primera evolución debe contener al menos un texto o plantilla de control.");
        }


        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, historiaClinica, medico);

        repositorioDiagnostico.save(nuevoDiagnostico);

        Evolucion primeraEvolucion = servicioEvolucion.crearEvolucion(nuevoDiagnostico, evolucionDTO, medico);
        if (!nuevoDiagnostico.getEvoluciones().contains(primeraEvolucion)) {
            nuevoDiagnostico.agregarEvolucion(primeraEvolucion);
        }


        return nuevoDiagnostico;
    }

    public Diagnostico crearDiagnosticoPermitido(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        try {
            tipoDiagnostico = TipoDiagnostico.fromNombre(nombreDiagnostico);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El diagnóstico ingresado no está permitido.");
        }
        return crearDiagnosticoConPrimeraEvolucion(idHistoriaClinica, tipoDiagnostico.getNombre(), evolucionDTO, medico, emailManual);
    }

    public Diagnostico crearDiagnosticoNoPermitido(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        return crearDiagnosticoConPrimeraEvolucion(idHistoriaClinica, nombreDiagnostico, evolucionDTO, medico, emailManual);
    }

    public boolean existeDiagnosticoEnHistoriaClinica(Long historiaClinicaId, String nombreDiagnostico) {
        return repositorioDiagnostico.existsByNombreAndHistoriaClinicaId(nombreDiagnostico, historiaClinicaId);
    }
}