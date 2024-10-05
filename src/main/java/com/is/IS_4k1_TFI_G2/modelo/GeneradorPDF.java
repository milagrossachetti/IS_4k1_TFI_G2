package com.is.IS_4k1_TFI_G2.modelo;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class GeneradorPDF {

    public static String generarPdfLaboratorio(Evolucion evolucion) throws Exception {
        String pdfPath = "pedido_laboratorio_" + evolucion.getId() + ".pdf";
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Datos del Paciente:"));
        document.add(new Paragraph("Nombre: " + evolucion.getDiagnostico().getHistoriaClinica().getPaciente().getNombreCompleto()));
        document.add(new Paragraph("Obra Social: " + evolucion.getDiagnostico().getHistoriaClinica().getPaciente().getObraSocialId()));
        document.add(new Paragraph("Número de Afiliado: " + evolucion.getDiagnostico().getHistoriaClinica().getPaciente().getNroAfiliado()));
        document.add(new Paragraph("DNI: " + evolucion.getDiagnostico().getHistoriaClinica().getPaciente().getDni()));
        document.add(new Paragraph("CUIL: " + evolucion.getDiagnostico().getHistoriaClinica().getPaciente().getCuil()));

        document.add(new Paragraph("Datos del Médico:"));
        document.add(new Paragraph("Nombre: " + evolucion.getUsuario().getNombreCompleto()));
        document.add(new Paragraph("Especialidad: " + evolucion.getUsuario().getEspecialidad()));

        document.add(new Paragraph("Fecha de emisión: " + LocalDateTime.now().toString()));

        PlantillaLaboratorio plantillaLaboratorio = evolucion.getPlantillaLaboratorio();

        if (plantillaLaboratorio != null) {
            document.add(new Paragraph("Tipos de Estudio Solicitados:"));
            for (String tipoEstudio : plantillaLaboratorio.getTiposEstudios()) {
                document.add(new Paragraph("Tipo de Estudio: " + tipoEstudio));

                document.add(new Paragraph("Ítems del estudio:"));
                plantillaLaboratorio.getItems().forEach(item -> document.add(new Paragraph("- " + item)));
            }
        } else {
            document.add(new Paragraph("No se solicitaron estudios de laboratorio."));
        }

        document.close();

        return pdfPath;
    }
}
