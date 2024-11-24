package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.*;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.Medicamento;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServicioEvolucion {

    private final ServicioEmail emailService;
    private final RepositorioPaciente pacienteRepositorio;
    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucion(RepositorioPaciente pacienteRepositorio, ServicioEmail emailService) {
        this.pacienteRepositorio = pacienteRepositorio;
        this.emailService = emailService;
    }

    // Obtener evoluciones de un diagnóstico específico
    public List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        Paciente paciente = Optional.ofNullable(pacienteRepositorio.buscarPorCuil(cuilPaciente))
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con CUIL: " + cuilPaciente));
        Diagnostico diagnostico = paciente.getHistoriaClinica().getDiagnosticos().stream()
                .filter(d -> d.getId().equals(diagnosticoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado."));
        return diagnostico.getEvoluciones();
    }

    // Crear evolución para un diagnóstico
    public Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, Usuario medico) {
        if (medico == null) {
            throw new RuntimeException("El médico autenticado es obligatorio.");
        }

        Paciente paciente = Optional.ofNullable(pacienteRepositorio.buscarPorCuil(cuilPaciente))
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con CUIL: " + cuilPaciente));
        Diagnostico diagnostico = paciente.getHistoriaClinica().getDiagnosticos().stream()
                .filter(d -> d.getId().equals(diagnosticoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado."));

        Evolucion nuevaEvolucion = new Evolucion(
                evolucionDTO.getTexto(),
                LocalDateTime.now(),
                medico,
                convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl()),
                convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio()),
                new ArrayList<>(),
                ""
        );

        diagnostico.agregarEvolucion(nuevaEvolucion);

        pacienteRepositorio.guardarPaciente(paciente);

        logger.info("Evolución creada exitosamente para el diagnóstico ID: {}", diagnosticoId);
        return nuevaEvolucion;
    }

    // Crear receta y asociarla a una evolución
    public Receta crearReceta(List<String> nombresMedicamentos, Evolucion evolucion, Usuario medico) {
        if (nombresMedicamentos.size() > 2) {
            throw new IllegalArgumentException("Solo se permiten hasta 2 medicamentos por receta.");
        }

        List<String> medicamentosValidos = Medicamento.MEDICAMENTOS.stream()
                .map(Medicamento::getNombre)
                .collect(Collectors.toList());

        nombresMedicamentos.forEach(nombre -> {
            if (!medicamentosValidos.contains(nombre)) {
                throw new IllegalArgumentException("El medicamento " + nombre + " no es válido.");
            }
        });

        List<MedicamentoRecetado> medicamentos = nombresMedicamentos.stream()
                .map(MedicamentoRecetado::new)
                .collect(Collectors.toList());

        Receta receta = new Receta(LocalDateTime.now(), medico, medicamentos, evolucion, null);
        evolucion.getRecetas().add(receta);

        logger.info("Receta creada con medicamentos: {}", nombresMedicamentos);
        return receta;
    }

    // Generar PDF de receta
    public void generarPdfReceta(Receta receta) {
        if (receta == null) {
            throw new RuntimeException("Receta no puede ser nula para generar el PDF.");
        }

        if (receta.getMedicamentos() == null || receta.getMedicamentos().isEmpty()) {
            throw new RuntimeException("La receta no contiene medicamentos.");
        }

        String pdfPath = "ruta/ficticia/receta_" + receta.getId() + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(new File(pdfPath))) {
            fos.write(("PDF generado para receta ID: " + receta.getId()).getBytes());
            receta.setRutaPdf(pdfPath);
            logger.info("PDF generado en: {}", pdfPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }

    // Enviar PDF asociado a una evolución
    public void enviarPdfEvolucion(Long cuilPaciente, Long diagnosticoId, Long evolucionId, String email) {
        Evolucion evolucion = obtenerEvolucionesDelDiagnostico(cuilPaciente, diagnosticoId).stream()
                .filter(e -> e.getId().equals(evolucionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada."));

        if (evolucion.getRutaPdf() == null) {
            throw new RuntimeException("No hay un PDF generado para esta evolución.");
        }

        String destinatario = (email != null && !email.isEmpty()) ? email : evolucion.getUsuario().getEmail();

        if (destinatario == null || destinatario.isEmpty()) {
            throw new RuntimeException("No se especificó un correo electrónico válido para el envío.");
        }

        try {
            emailService.enviarEmailConAdjunto(
                    destinatario,
                    "PDF de Evolución",
                    "Por favor, revise el archivo adjunto correspondiente a la evolución.",
                    evolucion.getRutaPdf()
            );
            logger.info("Correo enviado a {} con el PDF de evolución ID: {}", destinatario, evolucionId);
        } catch (jakarta.mail.MessagingException e) {
            logger.error("Error al enviar el correo para la evolución {}: {}", evolucionId, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo: " + e.getMessage(), e);
        }
    }

    // Convertir DTOs a Plantillas
    private PlantillaControl convertirPlantillaControlDTO(PlantillaControlDTO dto) {
        if (dto == null) return null;
        return new PlantillaControl(dto.getPeso(), dto.getAltura(), dto.getPresion(), dto.getPulso(), dto.getSaturacion(), dto.getNivelAzucar());
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) return null;
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems(), dto.getEstado());
    }
}
