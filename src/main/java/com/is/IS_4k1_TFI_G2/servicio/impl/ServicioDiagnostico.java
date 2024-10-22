package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelo.Evolucion;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.modelo.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioEvolucion;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioDiagnostico {

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    public Diagnostico crearDiagnosticoConPrimeraEvolucion(Long idHistoriaClinica, String nombreDiagnostico, String textoPrimeraEvolucion, Usuario medico) {
        //validar hc !=nulll
        if (idHistoriaClinica == null) {
            throw new IllegalArgumentException("El ID de historia clínica no puede ser nulo");
        }

        //buscar hc
        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinica)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        //validar nombre del diagnóstico y existencia
        if (nombreDiagnostico == null || nombreDiagnostico.isEmpty()) {
            throw new IllegalArgumentException("El nombre del diagnóstico no puede ser nulo.");
        }

        if (repositorioDiagnostico.findByNombreAndHistoriaClinicaId(nombreDiagnostico, idHistoriaClinica).isPresent()) {
            throw new IllegalArgumentException("Ya existe un diagnóstico con ese nombre.");
        }

        //validar 1ra evolcuion !=null
        if (textoPrimeraEvolucion == null || textoPrimeraEvolucion.isEmpty()) {
            throw new IllegalArgumentException("El texto de la primera evolución no puede ser nulo o vacío");
        }

        //crear nuevo diagnóstico
        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, medico);
        nuevoDiagnostico.setHistoriaClinica(historiaClinica);

        //crear primera evolución
        Evolucion primeraEvolucion = new Evolucion(textoPrimeraEvolucion, LocalDateTime.now(), medico);
        nuevoDiagnostico.agregarEvolucion(primeraEvolucion);

        //persistir el diagnóstico
        try {
            repositorioDiagnostico.save(nuevoDiagnostico);
            System.out.println("Diagnóstico creado y guardado con éxito.");
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el diagnóstico: " + e.getMessage(), e);
        }

        return nuevoDiagnostico;
    }


}