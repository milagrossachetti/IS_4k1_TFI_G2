package com.is.IS_4k1_TFI_G2.servicios;

import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.modelos.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioEvolucion;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioMedico;
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
    private RepositorioMedico repositorioMedico;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    public Diagnostico crearDiagnosticoConPrimeraEvolucion(Long idHistoriaClinica, String nombreDiagnostico, String textoPrimeraEvolucion, Medico medico) {

        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinica)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, medico);

        nuevoDiagnostico.setHistoriaClinica(historiaClinica);

        Evolucion primeraEvolucion = new Evolucion(textoPrimeraEvolucion, LocalDateTime.now(), medico);

        nuevoDiagnostico.agregarEvolucion(primeraEvolucion);

        repositorioDiagnostico.save(nuevoDiagnostico);

        return nuevoDiagnostico;
    }
}
