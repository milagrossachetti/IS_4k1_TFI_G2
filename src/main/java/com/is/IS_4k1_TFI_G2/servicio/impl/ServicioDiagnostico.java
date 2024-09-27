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

@Service
public class ServicioDiagnostico {

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioEvolucion repositorioEvolucion;

    @Autowired
    private RepositorioUsuario repositorioMedico;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    public Diagnostico crearDiagnosticoConPrimeraEvolucion(Long idHistoriaClinica, String nombreDiagnostico, String textoPrimeraEvolucion, Usuario medico) {
        // Validación de entradas
        if (idHistoriaClinica == null) {
            throw new IllegalArgumentException("El ID de historia clínica no puede ser nulo");
        }
        if (nombreDiagnostico == null || nombreDiagnostico.isEmpty()) {
            throw new IllegalArgumentException("El nombre del diagnóstico no puede ser nulo o vacío");
        }
        if (textoPrimeraEvolucion == null || textoPrimeraEvolucion.isEmpty()) {
            throw new IllegalArgumentException("El texto de la primera evolución no puede ser nulo o vacío");
        }

        // Buscar historia clínica
        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinica)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        // Crear nuevo diagnóstico
        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, medico);
        nuevoDiagnostico.setHistoriaClinica(historiaClinica);

        // Crear primera evolución
        Evolucion primeraEvolucion = new Evolucion(textoPrimeraEvolucion, LocalDateTime.now(), medico);
        nuevoDiagnostico.agregarEvolucion(primeraEvolucion);

        // Persistir el diagnóstico
        try {
            repositorioDiagnostico.save(nuevoDiagnostico);
            System.out.println("Diagnóstico creado y guardado con éxito.");
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el diagnóstico: " + e.getMessage(), e);
        }

        return nuevoDiagnostico;
    }

}