package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.DTOs.DiagnosticoDTO;
import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelo.Evolucion;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioDiagnostico;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioEvolucion;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
@Validated // Para activar validaciones en DTOs
public class ControladorEvolucion {

    private final ServicioEvolucion servicioEvolucion;
    private final ServicioDiagnostico servicioDiagnostico;
    private final RepositorioUsuario repositorioUsuario;

    public ControladorEvolucion(
            ServicioEvolucion servicioEvolucion,
            ServicioDiagnostico servicioDiagnostico,
            RepositorioUsuario repositorioUsuario) {
        this.servicioEvolucion = servicioEvolucion;
        this.servicioDiagnostico = servicioDiagnostico;
        this.repositorioUsuario = repositorioUsuario;
    }

    private Usuario obtenerMedicoAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un médico autenticado en el contexto actual.");
        }
        String email = authentication.getName();
        return repositorioUsuario.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos."));
    }

    @PostMapping("/diagnosticos/crear-permitido")
    public ResponseEntity<Object> crearDiagnosticoPermitido(@RequestBody @Valid DiagnosticoDTO diagnosticoDTO) {
        try {
            Usuario medico = obtenerMedicoAutenticado();
            Diagnostico diagnostico = servicioDiagnostico.crearDiagnosticoPermitido(
                    diagnosticoDTO.getIdHistoriaClinica(),
                    diagnosticoDTO.getNombreDiagnostico(),
                    diagnosticoDTO.getEvolucionDTO(),
                    medico
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(diagnostico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/diagnosticos/crear-no-permitido")
    public ResponseEntity<Object> crearDiagnosticoNoPermitido(@RequestBody @Valid DiagnosticoDTO diagnosticoDTO) {
        try {
            Usuario medico = obtenerMedicoAutenticado();
            Diagnostico diagnostico = servicioDiagnostico.crearDiagnosticoNoPermitido(
                    diagnosticoDTO.getIdHistoriaClinica(),
                    diagnosticoDTO.getNombreDiagnostico(),
                    diagnosticoDTO.getEvolucionDTO(),
                    medico
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(diagnostico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/pacientes/{cuilPaciente}/diagnosticos/{diagnosticoId}/evoluciones")
    public ResponseEntity<Object> agregarEvolucion(
            @PathVariable Long cuilPaciente,
            @PathVariable Long diagnosticoId,
            @RequestBody @Valid EvolucionDTO evolucionDTO) {
        try {
            Usuario medico = obtenerMedicoAutenticado();
            Evolucion evolucion = servicioEvolucion.crearEvolucion(cuilPaciente, diagnosticoId, evolucionDTO, medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(evolucion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
