package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.DTOs.DiagnosticoDTO;
import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelo.Evolucion;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioEmail;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioDiagnostico;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioEvolucion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/evoluciones")
public class ControladorEvolucion {

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    @Autowired
    private ServicioDiagnostico servicioDiagnostico;

    @Autowired
    private ServicioEmail emailService;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    // CREAR DIAGNÓSTICO CON PRIMERA EVOLUCIÓN
    @PostMapping("/crear-diagnostico")
    public ResponseEntity<Object> crearDiagnostico(@RequestBody DiagnosticoDTO diagnosticoDTO, @RequestParam(required = false) String emailManual) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Diagnostico nuevoDiagnostico = servicioDiagnostico.crearDiagnosticoConPrimeraEvolucion(
                diagnosticoDTO.getIdHistoriaClinica(),
                diagnosticoDTO.getNombreDiagnostico(),
                diagnosticoDTO.getEvolucionDTO(),
                medicoAutenticado,
                emailManual
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiagnostico);
    }

    // OBTENER DIAGNÓSTICOS DE UN PACIENTE
    @GetMapping("/paciente/{pacienteCuil}/diagnosticos")
    public ResponseEntity<List<Diagnostico>> obtenerDiagnosticos(@PathVariable Long pacienteCuil) {
        List<Diagnostico> diagnosticos = servicioEvolucion.obtenerDiagnosticosDelHistorialClinicoDelPaciente(pacienteCuil);
        return ResponseEntity.ok(diagnosticos);
    }

    // AGREGAR UNA EVOLUCIÓN A UN DIAGNÓSTICO
    @PostMapping("/diagnostico/{diagnosticoId}/agregar")
    public ResponseEntity<Evolucion> agregarEvolucion(
            @PathVariable Long diagnosticoId,
            @RequestBody EvolucionDTO evolucionDTO,
            @RequestParam(required = false) String emailManual) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Evolucion nuevaEvolucion = servicioEvolucion.agregarEvolucion(diagnosticoId, evolucionDTO, medicoAutenticado, emailManual);
        return ResponseEntity.ok(nuevaEvolucion);
    }


    // OBTENER TODAS LAS EVOLUCIONES DE UN DIAGNÓSTICO
    @GetMapping("/diagnostico/{diagnosticoId}/evoluciones")
    public ResponseEntity<List<Evolucion>> obtenerEvoluciones(@PathVariable Long diagnosticoId) {
        List<Evolucion> evoluciones = servicioEvolucion.obtenerEvolucionesDelDiagnostico(diagnosticoId);
        return ResponseEntity.ok(evoluciones);
    }

    // MODIFICAR UNA EVOLUCIÓN
    @PutMapping("/{evolucionId}/modificar")
    public ResponseEntity<Evolucion> modificarEvolucion(@PathVariable Long evolucionId, @RequestBody EvolucionDTO evolucionDTO) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        Evolucion evolucionModificada = servicioEvolucion.modificarEvolucion(evolucionId, evolucionDTO, medicoAutenticado);

        return ResponseEntity.ok(evolucionModificada);
    }

}
