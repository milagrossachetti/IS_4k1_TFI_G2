package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelo.Evolucion;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioDiagnostico;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioEvolucion;
import com.is.IS_4k1_TFI_G2.DTOs.DiagnosticoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    //DIAGNOSTICO
    //crear
    @PostMapping("/crear-diagnostico")
    public ResponseEntity<Object> crearDiagnostico(@RequestBody DiagnosticoDTO diagnosticoDTO) {
        // Obtiene el médico autenticado desde el contexto de seguridad
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Usuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario no autenticado o tipo incorrecto.");
        }

        Usuario medicoAutenticado = (Usuario) principal;*/

        //SIMULACION DEL MEDICO
        Long cuilDelMedico = 20123456789L; // Asegúrate de que esto sea un Long (sin guiones)

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Llama al servicio para crear un nuevo diagnóstico
        Diagnostico nuevoDiagnostico = servicioDiagnostico.crearDiagnosticoConPrimeraEvolucion(
                diagnosticoDTO.getIdHistoriaClinica(),
                diagnosticoDTO.getNombreDiagnostico(),
                diagnosticoDTO.getTextoPrimeraEvolucion(),
                medicoAutenticado
        );

        // Retorna una respuesta 201 Created junto con el diagnóstico creado
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiagnostico);
    }

    //obtener
    @GetMapping("/paciente/{pacienteId}/diagnosticos")
    public ResponseEntity<List<Diagnostico>> obtenerDiagnosticos(@PathVariable Long pacienteId) {
        List<Diagnostico> diagnosticos = servicioEvolucion.obtenerDiagnosticosDelHistorialClinicoDelPaciente(pacienteId);
        return ResponseEntity.ok(diagnosticos);
    }

    //EVOLUCION
    //agregar
    @PostMapping("/diagnostico/{diagnosticoId}/agregar")
    public ResponseEntity<Evolucion> agregarEvolucion(@PathVariable Long diagnosticoId, @RequestBody String texto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario medicoAutenticado = (Usuario) authentication.getPrincipal();

        Evolucion nuevaEvolucion = servicioEvolucion.agregarEvolucion(diagnosticoId, texto, medicoAutenticado);
        return ResponseEntity.ok(nuevaEvolucion);
    }

    //obtener
    @GetMapping("/diagnostico/{diagnosticoId}/evoluciones")
    public ResponseEntity<List<Evolucion>> obtenerEvoluciones(@PathVariable Long diagnosticoId) {
        List<Evolucion> evoluciones = servicioEvolucion.obtenerEvolucionesDelDiagnostico(diagnosticoId);
        return ResponseEntity.ok(evoluciones);
    }

    //modificar
    @PutMapping("/{evolucionId}/modificar")
    public ResponseEntity<Evolucion> modificarEvolucion(@PathVariable Long evolucionId,
                                                        @RequestBody String nuevoTexto, Usuario medicoAtuenticado) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario medicoAutenticado = (Usuario) authentication.getPrincipal();

        Evolucion evolucionModificada = servicioEvolucion.modificarEvolucion(evolucionId, nuevoTexto, medicoAutenticado);

        return ResponseEntity.ok(evolucionModificada);
    }
}
