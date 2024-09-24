package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.modelos.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelos.Evolucion;
import com.is.IS_4k1_TFI_G2.modelos.Medico;
import com.is.IS_4k1_TFI_G2.servicios.ServicioDiagnostico;
import com.is.IS_4k1_TFI_G2.servicios.ServicioEvolucion;
import com.is.IS_4k1_TFI_G2.DTOs.DiagnosticoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/evoluciones")
public class ControladorEvolucion {

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    @Autowired
    private ServicioDiagnostico servicioDiagnostico;


    @GetMapping("/paciente/{pacienteId}/diagnosticos")
    public ResponseEntity<List<Diagnostico>> obtenerDiagnosticos(@PathVariable Long pacienteId) {
        List<Diagnostico> diagnosticos = servicioEvolucion.obtenerDiagnosticosDelHistorialClinicoDelPaciente(pacienteId);
        return ResponseEntity.ok(diagnosticos);
    }

    @GetMapping("/diagnostico/{diagnosticoId}/evoluciones")
    public ResponseEntity<List<Evolucion>> obtenerEvoluciones(@PathVariable Long diagnosticoId) {
        List<Evolucion> evoluciones = servicioEvolucion.obtenerEvolucionesDelDiagnostico(diagnosticoId);
        return ResponseEntity.ok(evoluciones);
    }

    @PostMapping("/diagnostico/{diagnosticoId}/agregar")
    public ResponseEntity<Evolucion> agregarEvolucion(@PathVariable Long diagnosticoId, @RequestBody String texto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Medico medicoAutenticado = (Medico) authentication.getPrincipal();

        Evolucion nuevaEvolucion = servicioEvolucion.agregarEvolucion(diagnosticoId, texto, medicoAutenticado);
        return ResponseEntity.ok(nuevaEvolucion);
    }

    @PostMapping("/crear-diagnostico")
    public ResponseEntity<Diagnostico> crearDiagnostico(@RequestBody DiagnosticoDTO diagnosticoDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Medico medicoAutenticado = (Medico) authentication.getPrincipal();

        Diagnostico nuevoDiagnostico = servicioDiagnostico.crearDiagnosticoConPrimeraEvolucion(
                diagnosticoDTO.getIdHistoriaClinica(),
                diagnosticoDTO.getNombreDiagnostico(),
                diagnosticoDTO.getTextoPrimeraEvolucion(),
                medicoAutenticado
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiagnostico);
    }

    @PutMapping("/{evolucionId}/modificar")
    public ResponseEntity<Evolucion> modificarEvolucion(@PathVariable Long evolucionId,
                                                        @RequestBody String nuevoTexto, Medico medicoAtuenticado) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Medico medicoAutenticado = (Medico) authentication.getPrincipal();

        Evolucion evolucionModificada = servicioEvolucion.modificarEvolucion(evolucionId, nuevoTexto, medicoAutenticado);

        return ResponseEntity.ok(evolucionModificada);
    }
}

