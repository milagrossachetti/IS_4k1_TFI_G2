package com.is.IS_4k1_TFI_G2.servicios;

import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioEvolucion;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioEvolucion {

    @Autowired
    private RepositorioHistoriaClinica repositorioHistorialClinico;

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioEvolucion repositorioEvolucion;

    public List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long pacienteCuil){
        Optional<HistoriaClinica> historialClinico = repositorioHistorialClinico.findByPacienteCuil(pacienteCuil);
        return historialClinico.get().getDiagnosticos();
    }

    public Diagnostico seleccionarDiagnostico(Long diagnosticoId){
        return repositorioDiagnostico.findById(diagnosticoId)
                .orElseThrow(()-> new RuntimeException("Error al abrir Diagnostico-no existe"));
    }

    public List <Evolucion> obtenerEvolucionesDelDiagnostico (Long diagnosticoId) {
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);
        return diagnostico.getEvoluciones();
    }

    public Evolucion agregarEvolucion(Long diagnosticoId, String texto, Medico medico) {
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);

        Evolucion nuevaEvolucion = new Evolucion(texto, LocalDateTime.now(), medico);
        diagnostico.agregarEvolucion(nuevaEvolucion);

        repositorioEvolucion.save(nuevaEvolucion);
        repositorioDiagnostico.save(diagnostico);

        return nuevaEvolucion;
    }

    public Evolucion modificarEvolucion(Long evolucionId, String nuevoTexto, Medico medicoAutenticado) {
        Evolucion evolucion = repositorioEvolucion.findById(evolucionId)
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada"));

        if (!evolucion.getMedico().getId().equals(medicoAutenticado.getId())) {
            throw new RuntimeException("Solo el médico que creó la evolución puede modificarla.");
        }

        LocalDateTime fechaCreacion = evolucion.getFechaEvolucion();  // Asegúrate que esto sea un LocalDateTime
        LocalDateTime ahora = LocalDateTime.now();

        Duration duracion = Duration.between(fechaCreacion, ahora);

        if (duracion.toHours() < 48) {
            evolucion.setTexto(nuevoTexto);
            repositorioEvolucion.save(evolucion);
            return evolucion;
        } else {
            throw new RuntimeException("La evolución no puede ser modificada después de 48 horas.");
        }
    }

}
