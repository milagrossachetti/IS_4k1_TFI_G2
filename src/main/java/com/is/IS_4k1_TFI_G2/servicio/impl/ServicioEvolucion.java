package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaControlDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaLaboratorioDTO;
import com.is.IS_4k1_TFI_G2.DTOs.RecetaDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private RepositorioPlantillaLaboratorio repositorioPlantillaLaboratorio;

    @Autowired
    private RepositorioPlantillaControl repositorioPlantillaControl;

    @Autowired
    private RepositorioReceta repositorioReceta;

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

    public Evolucion getEvolucion(Long id) {
        return repositorioEvolucion.findById(id)
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada"));
    }

    public Evolucion crearEvolucion(Diagnostico diagnostico, EvolucionDTO evolucionDTO, Usuario medico) {
        // Convertir plantillas a entidades
        PlantillaControl plantillaControl = convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl());
        PlantillaLaboratorio plantillaLaboratorio = convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio());

        // Guardar plantillas si no son nulas
        if (plantillaControl != null) {
            plantillaControl = repositorioPlantillaControl.save(plantillaControl);
        }
        if (plantillaLaboratorio != null) {
            plantillaLaboratorio = repositorioPlantillaLaboratorio.save(plantillaLaboratorio);
        }

        // Crear evolución sin recetas inicialmente
        Evolucion nuevaEvolucion = new Evolucion(
                evolucionDTO.getTexto(),
                LocalDateTime.now(),
                medico,
                plantillaControl,
                plantillaLaboratorio,
                null,  // Inicializamos con una lista vacía de recetas
                ""
        );
        // Generar el PDF después de la creación
        if (nuevaEvolucion.getPlantillaLaboratorio() != null) {
            generarPdfYGuardarRuta(nuevaEvolucion); // Solo genera el PDF y guarda la ruta
        }

        // Asignar la evolución al diagnóstico
        diagnostico.agregarEvolucion(nuevaEvolucion);
        nuevaEvolucion = repositorioEvolucion.save(nuevaEvolucion);

        // Agregar recetas a la evolución si están presentes en el DTO
        if (evolucionDTO.getRecetas() != null) {
            for (RecetaDTO recetaDTO : evolucionDTO.getRecetas()) {
                List<Medicamento> medicamentos = convertirListaStringAMedicamento(recetaDTO.getMedicamentos());
                if (medicamentos.size() > 2) {
                    throw new IllegalArgumentException("Solo se pueden agregar hasta 2 medicamentos por receta.");
                }

                // Crear la receta y asociarla a la evolución
                Receta nuevaReceta = new Receta(LocalDateTime.now(), medico, medicamentos, nuevaEvolucion);
                nuevaReceta = repositorioReceta.save(nuevaReceta);
                nuevaEvolucion.getRecetas().add(nuevaReceta); // Agregar la receta a la lista de recetas de la evolución

                // Generar el PDF de la receta y guardar la ruta
                generarPdfReceta(nuevaEvolucion, nuevaReceta);
            }

            // Guardar la evolución nuevamente con todas las recetas asociadas
            repositorioEvolucion.save(nuevaEvolucion);
        }


        return nuevaEvolucion;
    }

    public Evolucion modificarEvolucion(Long evolucionId, EvolucionDTO evolucionDTO, Usuario medicoAutenticado, String emailManual) {
        Evolucion evolucion = repositorioEvolucion.findById(evolucionId)
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada"));

        // Verificación de permisos
        if (!evolucion.getUsuario().getCuil().equals(medicoAutenticado.getCuil())) {
            throw new RuntimeException("Solo el médico que creó la evolución puede modificarla.");
        }

        // Verificación de límite de 48 horas para modificar
        LocalDateTime fechaCreacion = evolucion.getFechaEvolucion();
        Duration duracion = Duration.between(fechaCreacion, LocalDateTime.now());
        if (duracion.toHours() >= 48) {
            throw new RuntimeException("La evolución no puede ser modificada después de 48 horas.");
        }

        // Actualizar texto de evolución
        evolucion.setTexto(evolucionDTO.getTexto());

        // Actualizar datos de PlantillaControl
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

        boolean actualizarPlantillaLaboratorio = false;

        // Actualizar o anular PlantillaLaboratorio según el estado en el DTO
        PlantillaLaboratorio plantillaLaboratorio = evolucion.getPlantillaLaboratorio(); // Obtener plantilla actual
        PlantillaLaboratorioDTO plantillaLaboratorioDTO = evolucionDTO.getPlantillaLaboratorio(); // Obtener datos del DTO


        // Verificar los casos de modificación de la plantilla de laboratorio
        if (plantillaLaboratorio != null) {
            // Caso: Anular la plantilla sin crear una nueva
            if ("Anulado".equals(plantillaLaboratorioDTO.getEstado()) && plantillaLaboratorioDTO.getTiposEstudios() == null) {
                plantillaLaboratorio.anular();
                actualizarPlantillaLaboratorio = false; // No se crea una nueva
            }
            // Caso: Anular la plantilla existente y crear una nueva
            else if ("Anulado".equals(plantillaLaboratorioDTO.getEstado()) && plantillaLaboratorioDTO.getTiposEstudios() != null) {
                plantillaLaboratorio.anular();
                evolucion.setPlantillaLaboratorio(convertirPlantillaLaboratorioDTO(plantillaLaboratorioDTO));
                actualizarPlantillaLaboratorio = true; // Se crea una nueva
            }
        } else if (plantillaLaboratorio == null && plantillaLaboratorioDTO != null) {
            // Caso: Crear una nueva plantilla si no existe ninguna
            evolucion.setPlantillaLaboratorio(convertirPlantillaLaboratorioDTO(plantillaLaboratorioDTO));
            actualizarPlantillaLaboratorio = true;
        }

        // Generar el PDF si hay una nueva plantilla o si se actualizó
        if (actualizarPlantillaLaboratorio) {
            generarPdfYGuardarRuta(evolucion);
        }


        // Manejo de recetas
        if (evolucionDTO.getRecetas() != null) {
            List<RecetaDTO> nuevasRecetasDTO = evolucionDTO.getRecetas();
            List<Receta> recetasExistentes = evolucion.getRecetas();

            // 1. Anular recetas existentes si están marcadas como anuladas en DTO
            for (Receta recetaExistente : recetasExistentes) {
                boolean anularReceta = nuevasRecetasDTO.stream()
                        .anyMatch(recetaDTO -> recetaDTO.isAnulado() && recetaDTO.getMedicamentos().equals(recetaExistente.getMedicamentos()));
                if (anularReceta) {
                    recetaExistente.anular();
                }
            }

            // 2. Agregar nuevas recetas y generar PDF solo para estas
            for (RecetaDTO recetaDTO : nuevasRecetasDTO) {
                if (!recetaDTO.isAnulado()) {
                    List<Medicamento> medicamentos = convertirListaStringAMedicamento(recetaDTO.getMedicamentos());

                    // Verificar si la receta ya existe
                    boolean recetaYaExiste = recetasExistentes.stream()
                            .anyMatch(r -> r.getMedicamentos().equals(medicamentos));

                    if (!recetaYaExiste) {
                        // Agregar la nueva receta a la evolución
                        Receta nuevaReceta = agregarRecetaAEvolucion(evolucion, medicamentos);

                        // Generar PDF para la nueva receta
                        generarPdfReceta(evolucion, nuevaReceta);
                    }
                }
            }
        }

        // Generar el PDF después de la creación
        if (evolucion.getPlantillaLaboratorio() != null) {
            generarPdfYGuardarRuta(evolucion); // Solo genera el PDF y guarda la ruta
        }

        // Guardar los cambios en el repositorio
        repositorioEvolucion.save(evolucion);

        return evolucion;
    }


    public Evolucion agregarEvolucion(Long diagnosticoId, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);
        Evolucion nuevaEvolucion = crearEvolucion(diagnostico, evolucionDTO, medico);

        // Generar el PDF después de la creación
        if (nuevaEvolucion.getPlantillaLaboratorio() != null) {
            generarPdfYGuardarRuta(nuevaEvolucion); // Solo genera el PDF y guarda la ruta
        }
        nuevaEvolucion.getRecetas().forEach(receta -> generarPdfReceta(nuevaEvolucion, receta));


        return nuevaEvolucion;
    }

    public Receta agregarRecetaAEvolucion(Evolucion evolucion, List<Medicamento> medicamentos) {
        if (medicamentos.size() > 2) {
            throw new IllegalArgumentException("Solo se pueden agregar hasta 2 medicamentos por receta.");
        }

        Receta nuevaReceta = new Receta(LocalDateTime.now(), evolucion.getUsuario(), medicamentos, evolucion);
        evolucion.getRecetas().add(nuevaReceta);
        repositorioReceta.save(nuevaReceta);
        repositorioEvolucion.save(evolucion);

        return nuevaReceta;
    }


    // Método de ayuda para convertir String a Medicamento
    public List<Medicamento> convertirListaStringAMedicamento(List<String> nombresMedicamentos) {
        return nombresMedicamentos.stream()
                .map(nombre -> Medicamento.valueOf(nombre.toUpperCase())) // Asegúrate de que los nombres coincidan con los del enum
                .collect(Collectors.toList());
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

    public void generarPdfYGuardarRuta(Evolucion evolucion) {
        if (evolucion.getPlantillaLaboratorio() == null || "Anulado".equals(evolucion.getPlantillaLaboratorio().getEstado())) {
            System.out.println("PDF no generado porque la plantilla de laboratorio está anulada o no existe.");
            return;
        }

        try {
            String pdfPath = GeneradorPDF.generarPdfLaboratorio(evolucion);
            evolucion.setRutaPdf(pdfPath);
            repositorioEvolucion.save(evolucion);
            System.out.println("PDF generado y ruta guardada en la evolución.");
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
        }
    }

    public void enviarPdfPorEmail(Evolucion evolucion, String emailManual) {
        String pdfPath = evolucion.getRutaPdf();

        if (pdfPath == null || pdfPath.isEmpty()) {
            throw new RuntimeException("La ruta del PDF no está disponible, no se puede enviar el archivo.");
        }

        String emailDestino = obtenerEmailDestino(evolucion, emailManual);

        if (emailDestino != null && !emailDestino.isEmpty()) {
            try {
                emailService.enviarPdfPorEmail(emailDestino, "Pedido de Laboratorio - Policlinica", "Adjuntamos su PDF de laboratorio.", pdfPath);
                System.out.println("PDF enviado a: " + emailDestino);
            } catch (Exception e) {
                throw new RuntimeException("Error al enviar el PDF: " + e.getMessage());
            }
        } else {
            System.out.println("Email de destino no especificado, no se pudo enviar el PDF.");
        }
    }

    private String obtenerEmailDestino(Evolucion evolucion, String emailManual) {
        return (emailManual != null && !emailManual.isEmpty())
                ? emailManual
                : evolucion.getDiagnostico().getHistoriaClinica().getPaciente().getEmail();
    }

    public void generarPdfReceta(Evolucion evolucion, Receta receta) {
        if (receta == null || receta.isAnulada()) {
            System.out.println("PDF no generado porque la receta está anulada o no existe.");
            return; // No generar PDF si la receta está anulada o no existe
        }

        try {
            String pdfPath = GeneradorPDF.generarPdfReceta(evolucion, receta); // Asume que este método genera y guarda el PDF para la receta
            receta.setRutaPdf(pdfPath); // Guarda la ruta del PDF en la receta
            repositorioReceta.save(receta); // Guarda la receta con la ruta en la base de datos
            System.out.println("PDF de receta generado y ruta guardada en la receta.");
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF de receta: " + e.getMessage());
        }
    }

    public void enviarPdfRecetaPorEmail(Receta receta, String emailManual) {
        String pdfPath = receta.getRutaPdf();

        if (pdfPath == null || pdfPath.isEmpty()) {
            throw new RuntimeException("La ruta del PDF de receta no está disponible, no se puede enviar el archivo.");
        }

        String emailDestino = (emailManual != null && !emailManual.isEmpty())
                ? emailManual
                : receta.getEvolucion().getDiagnostico().getHistoriaClinica().getPaciente().getEmail();

        if (emailDestino != null && !emailDestino.isEmpty()) {
            try {
                emailService.enviarPdfPorEmail(emailDestino, "Receta - Policlinica", "Adjuntamos su PDF de receta.", pdfPath);
                System.out.println("PDF de receta enviado a: " + emailDestino);
            } catch (Exception e) {
                throw new RuntimeException("Error al enviar el PDF de receta: " + e.getMessage());
            }
        } else {
            System.out.println("Email de destino no especificado, no se pudo enviar el PDF de receta.");
        }
    }



}
