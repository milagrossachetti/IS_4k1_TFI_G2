package com.is.IS_4k1_TFI_G2.servicios;

import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.HistorialClinico;
import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioEvolucion;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistorialClinico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioEvolucion {

    @Autowired
    private RepositorioHistorialClinico repositorioHistorialClinico;

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioEvolucion repositorioEvolucion;

    public List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long pacienteId){
        Optional<HistorialClinico> historialClinico = repositorioHistorialClinico.findByPacienteId(pacienteId);
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

    public Evolucion agregarEvolucion (Long diagnosticoId, String texto, Medico medico) {
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);

        Evolucion nuevaEvolucion = new Evolucion ();
        nuevaEvolucion.setTexto(texto);
        nuevaEvolucion.setFechaEvolucion(LocalDate.now());
        nuevaEvolucion.setMedico(medico);

        diagnostico.agregarEvolucion(nuevaEvolucion);

        repositorioEvolucion.save(nuevaEvolucion);
        repositorioDiagnostico.save(diagnostico);

        return nuevaEvolucion;
    }
}
