package com.is.IS_4k1_TFI_G2.repositorio.memoria;

import com.is.IS_4k1_TFI_G2.modelo.PlantillaLaboratorio;
import org.springframework.stereotype.Repository;
import com.is.IS_4k1_TFI_G2.repositorio.apiSalud.RepositorioTipoEstudioLaboratorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RepositorioTipoEstudioLaboratorioMemoria implements RepositorioTipoEstudioLaboratorio {

    private final List<PlantillaLaboratorio> estudios = new ArrayList<>();

    // Inicialización de datos
    public RepositorioTipoEstudioLaboratorioMemoria() {
        estudios.add(new PlantillaLaboratorio("Hemograma completo", Arrays.asList(
                "Globulos Rojos (Eritrocitos)", "Globulos Blancos (Leucocitos)", "Plaquetas (Trombocitos)",
                "Hemoglobina (Hb)", "Hematocrito (Hto)", "Indices eritrocitarios"
        )));
        estudios.add(new PlantillaLaboratorio("Lipidograma", Arrays.asList(
                "Colesterol total", "Colesterol HDL (colesterol bueno)", "Colesterol LDL (colesterol malo)", "Triglicéridos"
        )));
        estudios.add(new PlantillaLaboratorio("Electrolitos en sangre", Arrays.asList(
                "Sodio (Na)", "Potasio (K)", "Cloruro (Cl)", "Calcio (Ca)"
        )));
        estudios.add(new PlantillaLaboratorio("Función renal", Arrays.asList(
                "Creatinina", "Nitrógeno ureico en sangre (BUN)"
        )));
        estudios.add(new PlantillaLaboratorio("Función hepática", Arrays.asList(
                "Bilirrubina", "Transaminasas (AST, ALT)", "Fosfatasa alcalina (ALP)", "Albúmina"
        )));
        estudios.add(new PlantillaLaboratorio("Orina completo", Arrays.asList(
                "Proteinuria", "Gluccosuria", "Cetonuria", "Hemoglobinuria", "Bilirrubinuria",
                "Urobilinuria", "Hematuria", "Cilindruria", "Leucocituria", "Piuria", "Cristaluria",
                "Celulas mucus", "Ionograma urinario", "pH", "Densidad", "Espuma", "Olor",
                "Coloración", "Consistencia", "Aspecto"
        )));
        estudios.add(new PlantillaLaboratorio("Función tiroidea", Arrays.asList(
                "TSH", "T4 libre", "T3"
        )));
        estudios.add(new PlantillaLaboratorio("Glucemia", Arrays.asList(
                "Glucosa en ayunas", "Hemoglobina glicosilada (HbA1c)"
        )));
        estudios.add(new PlantillaLaboratorio("Coprocultivo", Arrays.asList(
                "Examen físico", "Bacteriológico/Parasitario"
        )));
    }

    @Override
    public List<PlantillaLaboratorio> obtenerTodos() {
        return new ArrayList<>(estudios); // Retorna una copia para proteger los datos originales
    }

    @Override
    public List<String> getItemsPorEstudio(String nombreEstudio) {
        if (nombreEstudio == null || nombreEstudio.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estudio no puede ser nulo o vacío.");
        }
        return estudios.stream()
                .filter(estudio -> estudio.getNombreEstudio().equalsIgnoreCase(nombreEstudio))
                .findFirst()
                .map(PlantillaLaboratorio::getItems)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de estudio no reconocido: " + nombreEstudio));
    }
}
