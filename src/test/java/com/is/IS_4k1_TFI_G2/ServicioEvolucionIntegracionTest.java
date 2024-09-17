package com.is.IS_4k1_TFI_G2;

import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.servicios.ServicioEvolucion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ServicioEvolucionIntegracionTest {

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    @Test
    public void testAgregarEvolucionIntegracion() {

        Long diagnosticoId=12L;
        String texto = "El paciente tiene problemitas";
        Medico medico = new Medico(); //cuando tengamos autenticación podremos verificar con el usuario conectado

        Diagnostico diagnostico = new Diagnostico ();

        Evolucion nuevaEvolucion= servicioEvolucion.agregarEvolucion(diagnosticoId,texto,medico);


        assertNotNull(nuevaEvolucion);
        assertEquals (texto,nuevaEvolucion.getTexto());
        assertEquals (medico, nuevaEvolucion.getMedico());
        assertNotNull (nuevaEvolucion.getFechaEvolucion());
    }

}
