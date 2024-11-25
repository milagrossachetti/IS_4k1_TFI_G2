package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.*;
import com.is.IS_4k1_TFI_G2.excepciones.*;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.*;
import com.is.IS_4k1_TFI_G2.repositorio.apiSalud.RepositorioFarmaco;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicioEvolucion {

    private final ServicioEmail emailService;
    private final RepositorioPaciente repositorioPaciente;
    private final RepositorioFarmaco repositorioFarmaco;

    private static final Logger logger = LoggerFactory.getLogger(ServicioEvolucion.class);

    public ServicioEvolucion(RepositorioPaciente repositorioPaciente, ServicioEmail emailService, RepositorioFarmaco repositorioFarmaco) {
        this.repositorioPaciente = repositorioPaciente;
        this.emailService = emailService;
        this.repositorioFarmaco = repositorioFarmaco;
    }

    // Obtener evoluciones de un diagnóstico específico
    public List<Evolucion> obtenerEvolucionesDelDiagnostico(Long cuilPaciente, Long diagnosticoId) {
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        Diagnostico diagnostico = obtenerDiagnosticoDePaciente(paciente, diagnosticoId);
        return diagnostico.getEvoluciones();
    }

    // Crear evolución para un diagnóstico
    public Evolucion crearEvolucion(Long cuilPaciente, Long diagnosticoId, EvolucionDTO evolucionDTO, Usuario medico) {
        validarMedico(medico);
        Paciente paciente = obtenerPacientePorCuil(cuilPaciente);
        Diagnostico diagnostico = obtenerDiagnosticoDePaciente(paciente, diagnosticoId);

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
        repositorioPaciente.guardarPaciente(paciente);

        logger.info("Evolución creada exitosamente para el diagnóstico ID: {}", diagnosticoId);
        return nuevaEvolucion;
    }

    // Crear receta y asociarla a una evolución
    public Receta crearReceta(List<String> nombresMedicamentos, Evolucion evolucion, Usuario medico) {
        if (nombresMedicamentos.size() > 2) {
            throw new RecetaInvalidaException("Solo se permiten hasta 2 medicamentos por receta.");
        }

        // Validar y crear los medicamentos recetados usando el método centralizado
        List<MedicamentoRecetado> medicamentos = nombresMedicamentos.stream()
                .map(this::crearMedicamentoRecetado)
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

        String pdfPath = "ruta/ficticia/receta_" + UUID.randomUUID() + ".pdf"; // Generar nombre único
        try (FileOutputStream fos = new FileOutputStream(new File(pdfPath))) {
            fos.write(("PDF generado para receta con medicamentos: " + receta.getMedicamentos()).getBytes());
            receta.setRutaPdf(pdfPath);
            logger.info("PDF generado en: {}", pdfPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }

    // Enviar PDF asociado a una evolución
    public void enviarPdfEvolucion(Long cuilPaciente, Long diagnosticoId, Long evolucionId, String email) {
        Evolucion evolucion = obtenerEvolucionPorId(cuilPaciente, diagnosticoId, evolucionId);

        if (evolucion.getRutaPdf() == null) {
            throw new RuntimeException("No hay un PDF generado para esta evolución.");
        }

        String destinatario = validarCorreo(email, evolucion.getUsuario().getEmail());

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

    // Métodos privados de ayuda
    private Paciente obtenerPacientePorCuil(Long cuil) {
        return repositorioPaciente.buscarPorCuil(cuil)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado con CUIL: " + cuil));
    }

    private Diagnostico obtenerDiagnosticoDePaciente(Paciente paciente, Long diagnosticoId) {
        return paciente.getHistoriaClinica().getDiagnosticos().stream()
                .filter(d -> d.getId().equals(diagnosticoId))
                .findFirst()
                .orElseThrow(() -> new DiagnosticoNoEncontradoException("Diagnóstico no encontrado."));
    }

    private Evolucion obtenerEvolucionPorId(Long cuilPaciente, Long diagnosticoId, Long evolucionId) {
        return obtenerEvolucionesDelDiagnostico(cuilPaciente, diagnosticoId).stream()
                .filter(e -> e.getId().equals(evolucionId))
                .findFirst()
                .orElseThrow(() -> new EvolucionNoEncontradaException("Evolución no encontrada."));
    }

    private void validarMedico(Usuario medico) {
        if (medico == null) {
            throw new UsuarioNoAutenticadoException("El médico autenticado es obligatorio.");
        }
    }

    private void validarMedicamentos(List<String> nombresMedicamentos) {
        List<Medicamento> medicamentosValidos = repositorioFarmaco.buscarPorNombre("");

        // Extraemos los nombres válidos
        List<String> nombresValidos = medicamentosValidos.stream()
                .map(Medicamento::getNombre)
                .collect(Collectors.toList());

        for (String nombre : nombresMedicamentos) {
            if (!nombresValidos.contains(nombre)) {
                throw new MedicamentoInvalidoException("El medicamento " + nombre + " no es válido.");
            }
        }
    }


    private String validarCorreo(String correo, String correoAlternativo) {
        String destinatario = (correo != null && !correo.isEmpty()) ? correo : correoAlternativo;

        if (destinatario == null || destinatario.isEmpty() || !destinatario.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new CorreoInvalidoException("No se especificó un correo electrónico válido.");
        }
        return destinatario;
    }

    private PlantillaControl convertirPlantillaControlDTO(PlantillaControlDTO dto) {
        if (dto == null) return null;
        return new PlantillaControl(dto.getPeso(), dto.getAltura(), dto.getPresion(), dto.getPulso(), dto.getSaturacion(), dto.getNivelAzucar());
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) return null;
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems(), dto.getEstado());
    }

    public MedicamentoRecetado crearMedicamentoRecetado(String nombreMedicamento) {
        if (!repositorioFarmaco.existeMedicamento(nombreMedicamento)) {
            throw new IllegalArgumentException("El medicamento '" + nombreMedicamento + "' no es válido.");
        }
        return new MedicamentoRecetado(nombreMedicamento);
    }

}
