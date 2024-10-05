package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaControlDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaLaboratorioDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioDiagnostico;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioEvolucion;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioHistoriaClinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServicioDiagnostico {

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    @Autowired
    private RepositorioEvolucion repositorioEvolucion;

    @Autowired
    private ServicioEmail emailService;

    public Diagnostico crearDiagnosticoConPrimeraEvolucion(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        if (idHistoriaClinica == null) {
            throw new IllegalArgumentException("El ID de historia clínica no puede ser nulo");
        }

        HistoriaClinica historiaClinica = repositorioHistoriaClinica.findById(idHistoriaClinica)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        if (nombreDiagnostico == null || nombreDiagnostico.isEmpty()) {
            throw new IllegalArgumentException("El nombre del diagnóstico no puede ser nulo.");
        }

        if (repositorioDiagnostico.findByNombreAndHistoriaClinicaId(nombreDiagnostico, idHistoriaClinica).isPresent()) {
            throw new IllegalArgumentException("Ya existe un diagnóstico con ese nombre.");
        }

        if (evolucionDTO.getTexto() == null || evolucionDTO.getTexto().isEmpty()) {
            throw new IllegalArgumentException("El texto de la primera evolución no puede ser nulo o vacío");
        }

        Diagnostico nuevoDiagnostico = new Diagnostico(nombreDiagnostico, medico);
        nuevoDiagnostico.setHistoriaClinica(historiaClinica);

        PlantillaControl plantillaControl = convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl());
        PlantillaLaboratorio plantillaLaboratorio = convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio());

        Evolucion primeraEvolucion = new Evolucion(
                evolucionDTO.getTexto(),
                LocalDateTime.now(),
                medico,
                plantillaControl,
                plantillaLaboratorio,
                ""
        );

        nuevoDiagnostico.agregarEvolucion(primeraEvolucion);

        try {
            repositorioDiagnostico.save(nuevoDiagnostico);
            System.out.println("Diagnóstico creado y guardado con éxito.");
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el diagnóstico: " + e.getMessage(), e);
        }

        // Generar el PDF
        if (plantillaLaboratorio != null && !"Anulado".equals(plantillaLaboratorio.getEstado())) {
            try {
                String pdfPath = GeneradorPDF.generarPdfLaboratorio(primeraEvolucion);
                System.out.println("PDF generado en: " + pdfPath);

                primeraEvolucion.setRutaPdf(pdfPath);
                repositorioEvolucion.save(primeraEvolucion);

                Paciente paciente = historiaClinica.getPaciente();
                String emailDestino = (emailManual != null && !emailManual.isEmpty()) ? emailManual : paciente.getEmail();

                emailService.enviarPdfPorEmail(emailDestino, "Pedido de laboratorio - Policlinica", "Adjuntamos su PDF", pdfPath);
                System.out.println("Email enviado a: " + emailDestino);

            } catch (Exception e) {
                throw new RuntimeException("Error al generar o enviar el PDF: " + e.getMessage());
            }
        }

        return nuevoDiagnostico;
    }

    private PlantillaControl convertirPlantillaControlDTO(PlantillaControlDTO dto) {
        if (dto == null) {
            return null;
        }
        return new PlantillaControl(
                dto.getPeso(),
                dto.getAltura(),
                dto.getPresion(),
                dto.getPulso(),
                dto.getSaturacion(),
                dto.getNivelAzucar()
        );
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) {
            return null;
        }
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems(), dto.getEstado());
    }
}
