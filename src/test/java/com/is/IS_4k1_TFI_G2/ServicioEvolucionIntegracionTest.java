package com.is.IS_4k1_TFI_G2;

import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.servicios.ServicioEvolucion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ServicioEvolucionIntegracionTest {

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    @Test
    public void testAgregarEvolucionIntegracion() {

        Long diagnosticoId = 1L;

        String texto = "El paciente tiene problemitas";

        Medico medico = new Medico();
        medico.setId(3L);
        medico.setNombre("Juan");
        medico.setApellido("Pérez");
        medico.setMatricula(12345L);

        Evolucion nuevaEvolucion = servicioEvolucion.agregarEvolucion(diagnosticoId, texto, medico);

        assertNotNull(nuevaEvolucion);
        assertEquals(texto, nuevaEvolucion.getTexto());
        assertEquals(medico, nuevaEvolucion.getMedico());
        assertNotNull(nuevaEvolucion.getFechaEvolucion());
    }
}
