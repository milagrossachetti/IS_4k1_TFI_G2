package com.is.IS_4k1_TFI_G2;

import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.servicios.ServicioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ServicioDiagnosticoIntegracionTest {

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    @Autowired
    private ServicioDiagnostico servicioDiagnostico;

    @Test
    public void testCrearDiagnosticoConEvolucionInicial() {
        String nombreDiagnostico = "Neumonía";
        String textoEvolucion = "Evolución inicial - paciente muestra signos leves";

        Medico medico = new Medico();
        medico.setCuil(123456789L);

        Long idHistoriaClinicaExistente = 101L;
        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinicaExistente)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));


        Diagnostico nuevoDiagnostico = servicioDiagnostico.crearDiagnosticoConPrimeraEvolucion(
                historiaClinica.getId(), nombreDiagnostico, textoEvolucion, medico);


        assertNotNull(nuevoDiagnostico);
        assertEquals(nombreDiagnostico, nuevoDiagnostico.getNombre());
        assertNotNull(nuevoDiagnostico.getEvoluciones());
        assertFalse(nuevoDiagnostico.getEvoluciones().isEmpty());

        Evolucion primeraEvolucion = nuevoDiagnostico.getEvoluciones().get(0);
        assertNotNull(primeraEvolucion);
        assertEquals(textoEvolucion, primeraEvolucion.getTexto());
        assertEquals(medico, primeraEvolucion.getMedico());
        assertNotNull(primeraEvolucion.getFechaEvolucion());
    }

}
