package com.is.IS_4k1_TFI_G2.modelo.listaDeDato;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class TipoEstudioLaboratorio {
    private String nombreEstudio;
    private List<String> items;

    public String getNombreEstudio() {
        return nombreEstudio;
    }

    public List<String> getItems() {
        return items;
    }

    // Constructor
    public TipoEstudioLaboratorio(String nombreEstudio, List<String> items) {
        this.nombreEstudio = nombreEstudio;
        this.items = items;
    }

    // Lista estática que simula el repositorio
    public static final List<TipoEstudioLaboratorio> ESTUDIOS = new ArrayList<>();

    // Inicialización de datos
    static {
        ESTUDIOS.add(new TipoEstudioLaboratorio("Hemograma completo", Arrays.asList(
                "Globulos Rojos (Eritrocitos)", "Globulos Blancos (Leucocitos)", "Plaquetas (Trombocitos)",
                "Hemoglobina (Hb)", "Hematrocito(Hto)", "Indices eritrocitarios"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Lipidograma", Arrays.asList(
                "Colesterol total", "Colesterol HDL (colesterol bueno)", "Colesterol LDL (colesterol malo)", "Triglicéridos"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Electrolitos en sangre", Arrays.asList(
                "Sodio (Na)", "Potasio (K)", "Cloruro (Cl)", "Calcio (Ca)"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Función renal", Arrays.asList(
                "Creatinina", "Nitrógeno ureico en sangre (BUM)"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Función hepática", Arrays.asList(
                "Bilirrubina", "Transaminasas (AST, ALT)", "Fosfatasa alcalina (ALP)", "Albúmina"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Orina completo", Arrays.asList(
                "Proteinuria", "Gluccosuria", "Cetonuria", "Hemoglobinuria", "Bilirrubinuria",
                "Urobilinuria", "Hematuria", "Cilndruria", "Leucocituria", "Piuria", "Cristaluria",
                "Celulas mucus", "Ionograma urinario", "pH", "Densidad", "Espuma", "Olor",
                "Coloración", "Consistencia", "Aspecto"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Función tiroidea", Arrays.asList(
                "TSH", "T4 libre", "T3"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Glucemia", Arrays.asList(
                "Glucosa en ayunas", "Hemoglobina glicosilada (HbA1c)"
        )));
        ESTUDIOS.add(new TipoEstudioLaboratorio("Coprocultivo", Arrays.asList(
                "examen físico", "Bacteriologico/parasitario"
        )));
    }

    // Método para obtener todos los estudios
    public static List<TipoEstudioLaboratorio> obtenerTodos() {
        return new ArrayList<>(ESTUDIOS);
    }
    // Método para obtener items por nombre de estudio
    public static List<String> getItemsPorEstudio(String nombreEstudio) {
        return ESTUDIOS.stream()
                .filter(estudio -> estudio.getNombreEstudio().equalsIgnoreCase(nombreEstudio))
                .findFirst()
                .map(TipoEstudioLaboratorio::getItems)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de estudio no reconocido: " + nombreEstudio));
    }
}
