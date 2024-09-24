package com.is.IS_4k1_TFI_G2;

import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioEvolucion;
import com.is.IS_4k1_TFI_G2.servicios.ServicioEvolucion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ModificacionEvolucionTest {

    @Autowired
    private RepositorioEvolucion repositorioEvolucion;

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    @Test
    public void modificarEvolucion_Exitoso_MedicoCorrectoDentroDe48Horas() {
        Long evolucionId = 1L;
        String nuevoTexto = "Texto modificado";

        Medico medicoCreador = new Medico();
        medicoCreador.setId(1L);

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setNombre("Asma");

        Evolucion evolucion = new Evolucion();
        evolucion.setTexto("Texto original");
        evolucion.setFechaEvolucion(LocalDateTime.now().minusHours(24));
        evolucion.setMedico(medicoCreador);
        evolucion.setDiagnostico(diagnostico);

        repositorioDiagnostico.save(diagnostico);
        repositorioEvolucion.save(evolucion);

        Evolucion resultado = servicioEvolucion.modificarEvolucion(evolucion.getId(), nuevoTexto, medicoCreador);

        assertEquals(nuevoTexto, resultado.getTexto());
        assertEquals(diagnostico, resultado.getDiagnostico());
        assertEquals(medicoCreador, resultado.getMedico());
    }


    @Test
    public void modificarEvolucion_Falla_PasadasLas48Horas() {

        Long evolucionId = 1L;
        String nuevoTexto = "Texto modificado";

        Medico medicoCreador = new Medico();
        medicoCreador.setId(1L);

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setNombre("Diagnóstico de prueba");

        Evolucion evolucion = new Evolucion();
        evolucion.setTexto("Texto original");
        evolucion.setFechaEvolucion(LocalDateTime.now().minusHours(50));
        evolucion.setMedico(medicoCreador);
        evolucion.setDiagnostico(diagnostico);

        repositorioDiagnostico.save(diagnostico);
        repositorioEvolucion.save(evolucion);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioEvolucion.modificarEvolucion(evolucion.getId(), nuevoTexto, medicoCreador);
        });

        assertEquals("La evolución no puede ser modificada después de 48 horas.", exception.getMessage());
    }


    @Test
    public void modificarEvolucion_Falla_MedicoIncorrecto() {
        Long evolucionId = 1L;
        String nuevoTexto = "Texto modificado";

        Medico medicoCreador = new Medico();
        medicoCreador.setId(1L);

        Diagnostico diagnostico= new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setNombre("Asma");

        Medico otroMedico = new Medico();
        otroMedico.setId(2L);

        Evolucion evolucion = new Evolucion();
        evolucion.setTexto("Texto original");
        evolucion.setFechaEvolucion(LocalDateTime.now().minusHours(24));
        evolucion.setMedico(medicoCreador);
        evolucion.setDiagnostico(diagnostico);

        repositorioDiagnostico.save(diagnostico);
        repositorioEvolucion.save(evolucion);


        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioEvolucion.modificarEvolucion(evolucion.getId(), nuevoTexto, otroMedico);
        });

        assertEquals("Solo el médico que creó la evolución puede modificarla.", exception.getMessage());
    }
}
