package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.EvolucionDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaControlDTO;
import com.is.IS_4k1_TFI_G2.DTOs.PlantillaLaboratorioDTO;
import com.is.IS_4k1_TFI_G2.DTOs.RecetaDTO;
import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.*;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private Receta receta;

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
        // Validar que el diagnóstico esté persistido
        if (diagnostico.getId() == null) {
            throw new IllegalArgumentException("El diagnóstico debe estar persistido antes de crear una evolución.");
        }

        // Convertir y guardar plantilla de control si está presente
        PlantillaControl plantillaControl = null;
        if (evolucionDTO.getPlantillaControl() != null) {
            plantillaControl = convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl());
            plantillaControl = repositorioPlantillaControl.save(plantillaControl);
        }

        // Crear la evolución
        Evolucion nuevaEvolucion = new Evolucion(
                evolucionDTO.getTexto(),      // Texto de la evolución
                LocalDateTime.now(),          // Fecha actual
                medico,                       // Médico responsable
                plantillaControl,             // Plantilla de control si existe
                null,                         // Plantilla de laboratorio no aplica
                null,                         // Recetas no aplica
                ""                            // Ruta PDF vacía por defecto
        );

        // Asociar la evolución al diagnóstico
        nuevaEvolucion.setDiagnostico(diagnostico);
        diagnostico.agregarEvolucion(nuevaEvolucion);

        // Persistir la evolución
        nuevaEvolucion = repositorioEvolucion.save(nuevaEvolucion);

        // Generar el PDF si tiene plantilla de control


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
        if (evolucionDTO.getTexto() != null) {
            evolucion.setTexto(evolucionDTO.getTexto());
        }

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
                PlantillaLaboratorio nuevaPlantilla = convertirPlantillaLaboratorioDTO(plantillaLaboratorioDTO);
                repositorioPlantillaLaboratorio.save(nuevaPlantilla); // Guardar la nueva plantilla
                evolucion.setPlantillaLaboratorio(nuevaPlantilla);
                actualizarPlantillaLaboratorio = true; // Se crea una nueva
            }
        } else if (plantillaLaboratorio == null && plantillaLaboratorioDTO != null) {
            // Caso: Crear una nueva plantilla si no existe ninguna
            PlantillaLaboratorio nuevaPlantilla = convertirPlantillaLaboratorioDTO(plantillaLaboratorioDTO);
            repositorioPlantillaLaboratorio.save(nuevaPlantilla); // Guardar la nueva plantilla
            evolucion.setPlantillaLaboratorio(nuevaPlantilla);
            actualizarPlantillaLaboratorio = true;
        }

        // Lista para almacenar las rutas de los PDFs generados

        // Generar el PDF si hay una nueva plantilla o si se actualizó
        List<String> pdfPaths = new ArrayList<>();

        if (actualizarPlantillaLaboratorio) {
            generarPdfLaboratorio(evolucion);
            if (evolucion.getRutaPdf() != null) {
                pdfPaths.add(evolucion.getRutaPdf());  // Agregar la ruta del PDF de laboratorio a la lista
                System.out.println("Ruta del PDF de laboratorio agregada: " + evolucion.getRutaPdf());
            } else {
                System.out.println("Ruta del PDF de laboratorio es nula.");
            }
        } else {
            System.out.println("PDF no generado porque la plantilla de laboratorio está anulada o no existe.");
        }

        // Manejo de recetas
        if (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()) {
            // Convertir la lista de medicamentos de la receta
            List<Medicamento> medicamentos = convertirListaStringAMedicamento(evolucionDTO.getRecetas().get(0).getMedicamentos());

            // Verificar que no haya más de 2 medicamentos
            if (medicamentos.size() > 2) {
                throw new IllegalArgumentException("Solo se pueden agregar hasta 2 medicamentos por receta.");
            }

            // Si la receta es diferente, anular la receta anterior y crear una nueva
            if (evolucion.getRecetas() != null) {
                // Verificar si alguna receta tiene los mismos medicamentos
                for (Receta receta : evolucion.getRecetas()) {
                    if (!receta.getMedicamentos().equals(medicamentos)) {
                        receta.anular(); // Anular la receta si los medicamentos no coinciden
                    }
                }
            }

            // Crear la nueva receta
            Receta nuevaReceta = new Receta(LocalDateTime.now(), medicoAutenticado, medicamentos, evolucion);
            repositorioReceta.save(nuevaReceta); // Guardar la nueva receta

            // Agregar la nueva receta a la lista de recetas de la evolución
            evolucion.getRecetas().add(nuevaReceta);

            // Generar el PDF para la nueva receta
            generarPdfReceta(evolucion, nuevaReceta);
            if (nuevaReceta.getRutaPdf() != null) {
                pdfPaths.add(nuevaReceta.getRutaPdf());  // Agregar la ruta del PDF de receta a la lista
                System.out.println("Ruta del PDF de receta agregada: " + nuevaReceta.getRutaPdf());
            } else {
                System.out.println("Ruta del PDF de receta es nula.");
            }


        }

        // Guardar los cambios en el repositorio
        repositorioEvolucion.save(evolucion);

        // Enviar un solo email con todos los PDFs adjuntos
        if (!pdfPaths.isEmpty()) {
            try {
                enviarPdfPorEmailConAdjuntos(evolucion, emailManual, pdfPaths);
            } catch (MessagingException e) {
                throw new RuntimeException("Error al enviar los PDFs: " + e.getMessage());
            }
        } else {
            System.out.println("No se generaron PDFs para enviar.");
        }
        return evolucion;
    }

    public Evolucion agregarEvolucion(Long diagnosticoId, EvolucionDTO evolucionDTO, Usuario medico, String emailManual) {
        if (evolucionDTO == null) {
            throw new IllegalArgumentException("El EvolucionDTO no puede ser nulo.");
        }

        // Obtener el diagnóstico asociado
        Diagnostico diagnostico = seleccionarDiagnostico(diagnosticoId);

        // Crear la nueva evolución
        Evolucion nuevaEvolucion = crearEvolucion(diagnostico, evolucionDTO, medico);

        // Plantilla de laboratorio es opcional, si existe la agregamos
        if (evolucionDTO.getPlantillaLaboratorio() != null) {
            PlantillaLaboratorio pedidoLaboratorio = convertirPlantillaLaboratorioDTO(evolucionDTO.getPlantillaLaboratorio());
            repositorioPlantillaLaboratorio.save(pedidoLaboratorio); // Guardar la plantilla de laboratorio
            nuevaEvolucion.setPlantillaLaboratorio(pedidoLaboratorio);
        }

        // Texto adicional es opcional, si existe lo agregamos
        if (evolucionDTO.getTexto() != null && !evolucionDTO.getTexto().isEmpty()) {
            nuevaEvolucion.setTexto(evolucionDTO.getTexto());
        }

        // Plantilla de control es opcional, si existe la agregamos
        if (evolucionDTO.getPlantillaControl() != null) {
            PlantillaControl plantillaControl = convertirPlantillaControlDTO(evolucionDTO.getPlantillaControl());
            nuevaEvolucion.setPlantillaControl(plantillaControl);
        }

        // Guardar la evolución antes de generar los PDFs
        repositorioEvolucion.save(nuevaEvolucion);

        // Lista para almacenar las rutas de los PDFs generados
        List<String> pdfPaths = new ArrayList<>();

        // Procesar recetas si están presentes
        if (evolucionDTO.getRecetas() != null && !evolucionDTO.getRecetas().isEmpty()) {
            for (RecetaDTO recetaDTO : evolucionDTO.getRecetas()) {
                // Validar los medicamentos antes de agregar la receta
                List<Medicamento> medicamentos = convertirListaStringAMedicamento(recetaDTO.getMedicamentos());
                if (medicamentos.size() > 2) {
                    throw new IllegalArgumentException("Solo se pueden agregar hasta 2 medicamentos por receta.");
                }

                // Crear y agregar la receta a la evolución
                Receta nuevaReceta = new Receta(LocalDateTime.now(), nuevaEvolucion.getUsuario(), medicamentos, nuevaEvolucion);
                nuevaEvolucion.getRecetas().add(nuevaReceta);
                repositorioReceta.save(nuevaReceta);

                // Verificar si la receta no está anulada para generar su PDF
                if (!nuevaReceta.isAnulada()) {
                    generarPdfReceta(nuevaEvolucion, nuevaReceta);  // Generación del PDF para esta receta
                    if (nuevaReceta.getRutaPdf() != null) {
                        pdfPaths.add(nuevaReceta.getRutaPdf());  // Agregar la ruta del PDF a la lista
                        System.out.println("Ruta del PDF de receta agregada: " + nuevaReceta.getRutaPdf());
                    } else {
                        System.out.println("Ruta del PDF de receta es nula.");
                    }
                } else {
                    System.out.println("Receta anulada, no se generará el PDF.");
                }
            }
        } else {
            System.out.println("No se encontraron recetas en el EvolucionDTO.");
        }

        // Si hay un pedido de laboratorio y no está anulado, también se genera el PDF
        boolean generarPdfLaboratorio = nuevaEvolucion.getPlantillaLaboratorio() != null &&
                !"Anulado".equals(nuevaEvolucion.getPlantillaLaboratorio().getEstado());

        if (generarPdfLaboratorio) {
            generarPdfLaboratorio(nuevaEvolucion);
            if (nuevaEvolucion.getRutaPdf() != null) {
                pdfPaths.add(nuevaEvolucion.getRutaPdf());  // Agregar la ruta del PDF de laboratorio a la lista
                System.out.println("Ruta del PDF de laboratorio agregada: " + nuevaEvolucion.getRutaPdf());
            } else {
                System.out.println("Ruta del PDF de laboratorio es nula.");
            }
        } else {
            System.out.println("PDF no generado porque la plantilla de laboratorio está anulada o no existe.");
        }

        // Enviar un solo email con todos los PDFs adjuntos
        if (!pdfPaths.isEmpty()) {
            try {
                enviarPdfPorEmailConAdjuntos(nuevaEvolucion, emailManual, pdfPaths);
            } catch (MessagingException e) {
                throw new RuntimeException("Error al enviar los PDFs: " + e.getMessage());
            }
        } else {
            System.out.println("No se generaron PDFs para enviar.");
        }

        return nuevaEvolucion;
    }

    private void enviarPdfPorEmailConAdjuntos(Evolucion evolucion, String emailManual, List<String> pdfPaths) throws MessagingException {
        String emailDestino = obtenerEmailDestino(evolucion, emailManual);

        if (emailDestino != null && !emailDestino.isEmpty()) {
            emailService.enviarPdfPorEmailConAdjuntos(emailDestino, "Documentos - Policlinica", "Adjuntamos sus documentos en PDF.", pdfPaths);
            System.out.println("PDFs enviados a: " + emailDestino);
        } else {
            System.out.println("Email de destino no especificado, no se pudieron enviar los PDFs.");
        }
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

    public void generarPdfLaboratorio(Evolucion evolucion) {
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