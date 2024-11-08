package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaControlDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaLaboratorioDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioEvolucion {

    @Autowired
    private RepositorioHistoriaClinica repositorioHistoriaClinica;

    @Autowired
    private RepositorioDiagnostico repositorioDiagnostico;

    @Autowired
    private RepositorioEvolucion repositorioEvolucion;

    @Autowired
    private ServicioEmail emailService;

    @Autowired
    private RepositorioPlanitllaLaboratorio repositorioPlanitllaLaboratorio;

    @Autowired
    private RepositorioPlantillaControl repositorioPlantillaControl;

    public List<Diagnostico> obtenerDiagnosticosDelHistorialClinicoDelPaciente(Long pacienteCuil) {
        Optional<HistoriaClinica> historialClinica = repositorioHistoriaClinica.findByPacienteCuil(pacienteCuil);
        return historialClinica.get().getDiagnosticos();
    }

    public Diagnostico seleccionarDiagnostico(Long diagnosticoId) {
        return repositorioDiagnostico.findById(diagnosticoId)
                .orElseThrow(() -> new RuntimeException("Error al abrir Diagnóstico - no existe"));
    }

    public List<Evolucion> obtenerEvolucionesDelDiagnostico(Long diagnosticoId) {
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);
        return diagnostico.getEvoluciones();
    }

    public Evolucion agregarEvolucion(Long diagnosticoId, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);

        PlantillaControl plantillaControl = convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl());
        PlantillaLaboratorio plantillaLaboratorio = convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio());

        if (plantillaControl != null) {
            plantillaControl = repositorioPlantillaControl.save(plantillaControl);
        }
        if (plantillaLaboratorio != null) {
            plantillaLaboratorio = repositorioPlanitllaLaboratorio.save(plantillaLaboratorio);
        }

        Evolucion nuevaEvolucion = new Evolucion(
                evolucionDTO.getTexto(),
                LocalDateTime.now(),
                medico,
                plantillaControl,
                plantillaLaboratorio,
                ""
        );

        diagnostico.agregarEvolucion(nuevaEvolucion);
        repositorioEvolucion.save(nuevaEvolucion);
        repositorioDiagnostico.save(diagnostico);

        // Generar el PDF
        try {
            String pdfPath = GeneradorPDF.generarPdfLaboratorio(nuevaEvolucion);
            nuevaEvolucion.setRutaPdf(pdfPath);
            repositorioEvolucion.save(nuevaEvolucion);

            Paciente paciente = diagnostico.getHistoriaClinica().getPaciente();
            String emailDestino = (emailManual != null && !emailManual.isEmpty()) ? emailManual : paciente.getEmail();

            if (emailDestino != null && !emailDestino.isEmpty()) {
                emailService.enviarPdfPorEmail(emailDestino, "Pedido de Laboratorio - Policlinica", "Adjuntamos su PDF de laboratorio.", pdfPath);
            }

            System.out.println("PDF generado y enviado a: " + emailDestino);

        } catch (Exception e) {
            throw new RuntimeException("Error al generar o enviar el PDF: " + e.getMessage());
        }

        return nuevaEvolucion;
    }

    private PlantillaLaboratorio convertirPlantillaLaboratorioDTO(PlantillaLaboratorioDTO dto) {
        if (dto == null) {
            return null;
        }
        return new PlantillaLaboratorio(dto.getTiposEstudios(), dto.getItems(), dto.getEstado());
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

    public Evolucion modificarEvolucion(Long evolucionId, EvolucionDTO evolucionDTO, Usuario medicoAutenticado) {
        Evolucion evolucion = repositorioEvolucion.findById(evolucionId)
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada"));

        if (!evolucion.getUsuario().getCuil().equals(medicoAutenticado.getCuil())) {
            throw new RuntimeException("Solo el médico que creó la evolución puede modificarla.");
        }

        LocalDateTime fechaCreacion = evolucion.getFechaEvolucion();
        LocalDateTime ahora = LocalDateTime.now();
        Duration duracion = Duration.between(fechaCreacion, ahora);

        if (duracion.toHours() < 48) {
            evolucion.setTexto(evolucionDTO.getTexto());

            PlantillaControl plantillaExistente = evolucion.getPlantillaControl();
            PlantillaControlDTO plantillaDTO = evolucionDTO.getPlantillaControl();

            if (plantillaExistente != null && plantillaDTO != null) {
                if (plantillaDTO.getPeso() != null) plantillaExistente.setPeso(plantillaDTO.getPeso());
                if (plantillaDTO.getAltura() != null) plantillaExistente.setAltura(plantillaDTO.getAltura());
                if (plantillaDTO.getPresion() != null) plantillaExistente.setPresion(plantillaDTO.getPresion());
                if (plantillaDTO.getPulso() != null) plantillaExistente.setPulso(plantillaDTO.getPulso());
                if (plantillaDTO.getSaturacion() != null) plantillaExistente.setSaturacion(plantillaDTO.getSaturacion());
                if (plantillaDTO.getNivelAzucar() != null) plantillaExistente.setNivelAzucar(plantillaDTO.getNivelAzucar());
            } else if (plantillaDTO != null) {
                evolucion.setPlantillaControl(new PlantillaControl(
                        plantillaDTO.getPeso(), plantillaDTO.getAltura(), plantillaDTO.getPresion(),
                        plantillaDTO.getPulso(), plantillaDTO.getSaturacion(), plantillaDTO.getNivelAzucar()
                ));
            }

            PlantillaLaboratorio plantillaLaboratorio = evolucion.getPlantillaLaboratorio();
            PlantillaLaboratorioDTO plantillaLaboratorioDTO = evolucionDTO.getPlantillaLaboratorio();

            if (plantillaLaboratorio != null && plantillaLaboratorioDTO != null) {
                if ("Anulado".equals(plantillaLaboratorioDTO.getEstado())) {
                    plantillaLaboratorio.anular();
                }
            }

            try {
                String pdfPath = GeneradorPDF.generarPdfLaboratorio(evolucion);
                System.out.println("PDF generado o actualizado en: " + pdfPath);

                evolucion.setRutaPdf(pdfPath);
            } catch (Exception e) {
                throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
            }

            repositorioEvolucion.save(evolucion);

            return evolucion;
        } else {
            throw new RuntimeException("La evolución no puede ser modificada después de 48 horas.");
        }
    }


}