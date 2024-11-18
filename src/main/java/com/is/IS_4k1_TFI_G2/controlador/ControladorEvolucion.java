package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.DTOs.DiagnosticoDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioDiagnostico;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioEvolucion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/evoluciones")
public class ControladorEvolucion {

    @Autowired
    private ServicioEvolucion servicioEvolucion;

    @Autowired
    private ServicioDiagnostico servicioDiagnostico;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    // CREAR DIAGNÓSTICO CON PRIMERA EVOLUCIÓN SI ESTÁ PERMITIDO EN TipoDiagnostico
    //anda
    @PostMapping("/crear-diagnostico")
    public ResponseEntity<Object> crearDiagnostico(@RequestBody DiagnosticoDTO diagnosticoDTO, @RequestParam(required = false) String emailManual) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado para CUIL: " + cuilDelMedico);
                    return new RuntimeException("Usuario no encontrado");
                });

        try {
            // Intentar crear el diagnóstico solo si está en TipoDiagnostico
            Diagnostico nuevoDiagnostico = servicioDiagnostico.crearDiagnosticoPermitido(
                    diagnosticoDTO.getIdHistoriaClinica(),
                    diagnosticoDTO.getNombreDiagnostico(),
                    diagnosticoDTO.getEvolucionDTO(),
                    medicoAutenticado,
                    emailManual
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDiagnostico);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            String errorMessage = "El diagnóstico ingresado no está permitido. ¿Desea agregarlo solo para este paciente?";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    // CONFIRMAR Y AGREGAR DIAGNÓSTICO NO PERMITIDO SOLO PARA ESTE PACIENTE
    @PostMapping("/confirmar-diagnostico-especial")
    public ResponseEntity<Object> confirmarDiagnosticoEspecial(@RequestBody DiagnosticoDTO diagnosticoDTO, @RequestParam(required = false) String emailManual) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear el diagnóstico especial en la historia clínica del paciente y agregar la primera evolución
        Diagnostico nuevoDiagnostico = servicioDiagnostico.crearDiagnosticoNoPermitido(
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
    //anda
    @PostMapping("/diagnostico/{diagnosticoId}/agregar")
    public ResponseEntity<?> agregarEvolucion(
            @PathVariable Long diagnosticoId,
            @RequestBody EvolucionDTO evolucionDTO,
            @RequestParam(required = false) String emailManual) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Evolucion nuevaEvolucion = servicioEvolucion.agregarEvolucion(diagnosticoId, evolucionDTO, medicoAutenticado, emailManual);

        // Recopilar todas las rutas de los PDFs generados
        List<String> pdfPaths = nuevaEvolucion.getRecetas().stream()
                .map(Receta::getRutaPdf)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (nuevaEvolucion.getRutaPdf() != null) {
            pdfPaths.add(nuevaEvolucion.getRutaPdf());
        }

        // Crear una respuesta con las rutas de los PDFs
        Map<String, Object> response = new HashMap<>();
        response.put("evolucion", nuevaEvolucion);
        response.put("pdfUrls", pdfPaths.stream().map(path -> "/ruta/del/pdf/" + path).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    // OBTENER TODAS LAS EVOLUCIONES DE UN DIAGNÓSTICO
    @GetMapping("/diagnostico/{diagnosticoId}/evoluciones")
    public ResponseEntity<List<Evolucion>> obtenerEvoluciones(@PathVariable Long diagnosticoId) {
        List<Evolucion> evoluciones = servicioEvolucion.obtenerEvolucionesDelDiagnostico(diagnosticoId);
        return ResponseEntity.ok(evoluciones);
    }

    // MODIFICAR UNA EVOLUCIÓN
    @PutMapping("/{evolucionId}/modificar")
    public ResponseEntity<?> modificarEvolucion(
            @PathVariable Long evolucionId,
            @RequestBody EvolucionDTO evolucionDTO,
            @RequestParam(required = false) String emailManual) {
        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado

        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Evolucion evolucionModificada = servicioEvolucion.modificarEvolucion(evolucionId, evolucionDTO, medicoAutenticado, emailManual);
        return ResponseEntity.ok(evolucionModificada);
    }

    // SOLICITUD PARA CREAR RECETA DESDE LA HISTORIA CLÍNICA
    @PostMapping("/crear-desde-historia-clinica")
    public ResponseEntity<Object> crearRecetaDesdeHistoriaClinica(
            @RequestBody DiagnosticoDTO diagnosticoDTO,
            @RequestParam List<String> medicamentos,
            @RequestParam(required = false) String emailManual) {

        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado
        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            // Convertimos la lista de nombres de medicamentos a una lista de objetos Medicamento
            List<Medicamento> listaMedicamentos = servicioEvolucion.convertirListaStringAMedicamento(medicamentos);

            // Intentamos crear el diagnóstico solo si está en TipoDiagnostico
            Diagnostico diagnostico = servicioDiagnostico.crearDiagnosticoPermitido(
                    diagnosticoDTO.getIdHistoriaClinica(),
                    diagnosticoDTO.getNombreDiagnostico(),
                    diagnosticoDTO.getEvolucionDTO(),
                    medicoAutenticado,
                    emailManual
            );

            // Si el diagnóstico es permitido, creamos la evolución y añadimos la receta
            Evolucion evolucion = servicioEvolucion.crearEvolucion(diagnostico, diagnosticoDTO.getEvolucionDTO(), medicoAutenticado);
            servicioEvolucion.agregarRecetaAEvolucion(evolucion, listaMedicamentos);

            return ResponseEntity.status(HttpStatus.CREATED).body("Receta creada y asignada a la evolución del diagnóstico permitido.");

        } catch (IllegalArgumentException e) {
            // Si el diagnóstico no es permitido, pedimos confirmación del usuario
            String errorMessage = "El diagnóstico ingresado no está permitido. ¿Desea agregarlo solo para este paciente?";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    // CONFIRMACIÓN PARA CREAR DIAGNÓSTICO NO PERMITIDO Y ASIGNAR RECETA
    @PostMapping("/confirmar-crear-diagnostico-no-permitido")
    public ResponseEntity<Object> confirmarCrearDiagnosticoNoPermitido(
            @RequestBody DiagnosticoDTO diagnosticoDTO,
            @RequestParam List<String> medicamentos,
            @RequestParam(required = false) String emailManual) {

        Long cuilDelMedico = 20123456789L; // Simulación del CUIL del médico autenticado
        Usuario medicoAutenticado = repositorioUsuario.findByCuil(cuilDelMedico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convertimos la lista de nombres de medicamentos a una lista de objetos Medicamento
        List<Medicamento> listaMedicamentos = servicioEvolucion.convertirListaStringAMedicamento(medicamentos);

        // Crear diagnóstico no permitido, asociado solo al paciente específico
        Diagnostico diagnosticoNoPermitido = servicioDiagnostico.crearDiagnosticoNoPermitido(
                diagnosticoDTO.getIdHistoriaClinica(),
                diagnosticoDTO.getNombreDiagnostico(),
                diagnosticoDTO.getEvolucionDTO(),
                medicoAutenticado,
                emailManual
        );

        // Crear la evolución y asignar la receta a esta evolución
        Evolucion evolucion = servicioEvolucion.crearEvolucion(diagnosticoNoPermitido, diagnosticoDTO.getEvolucionDTO(), medicoAutenticado);
        servicioEvolucion.agregarRecetaAEvolucion(evolucion, listaMedicamentos);

        return ResponseEntity.status(HttpStatus.CREATED).body("Receta creada y asignada a la evolución del diagnóstico no permitido.");
    }

    // Endpoint para enviar PDF de Laboratorio asociado a una evolución
    @PostMapping("/evoluciones/{evolucionId}/enviar-pdf-laboratorio")
    public ResponseEntity<String> enviarPdfLaboratorio(@PathVariable Long evolucionId, @RequestParam(required = false) String emailManual) {
        Evolucion evolucion = servicioEvolucion.getEvolucion(evolucionId);

        // Enviar el PDF de laboratorio solo si la plantilla existe
        if (evolucion.getPlantillaLaboratorio() != null) {
            servicioEvolucion.enviarPdfPorEmail(evolucion, emailManual);
            return ResponseEntity.ok("PDF de laboratorio enviado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La evolución no tiene una plantilla de laboratorio.");
        }
    }

    // Endpoint para enviar PDF de Receta específico de una evolución
    @PostMapping("/evoluciones/{evolucionId}/recetas/{recetaId}/enviar-pdf-receta")
    public ResponseEntity<String> enviarPdfReceta(
            @PathVariable Long evolucionId,
            @PathVariable Long recetaId,
            @RequestParam(required = false) String emailManual) {

        Evolucion evolucion = servicioEvolucion.getEvolucion(evolucionId);
        Optional<Receta> recetaOpt = evolucion.getRecetas().stream()
                .filter(r -> r.getId().equals(recetaId))
                .findFirst();

        if (recetaOpt.isPresent()) {
            servicioEvolucion.enviarPdfRecetaPorEmail(recetaOpt.get(), emailManual);
            return ResponseEntity.ok("PDF de receta enviado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La receta especificada no está asociada a esta evolución.");
        }
    }


}
