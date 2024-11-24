package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class Evolucion {
    private Long id;

    private LocalDateTime fechaEvolucion;
    private String texto;

    @JsonBackReference
    private Diagnostico diagnostico;

    @JsonIgnore
    private Usuario usuario; // Médico autenticado, obligatorio

    private PlantillaControl plantillaControl;
    private PlantillaLaboratorio plantillaLaboratorio;
    private List<Receta> recetas = new ArrayList<>();

    private String rutaPdf;

    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario) {
        if (texto == null || fechaEvolucion == null || usuario == null) {
            throw new IllegalArgumentException("Texto, fecha y usuario son obligatorios.");
        }
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.usuario = usuario;
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio,
                     List<Receta> recetas, String rutaPdf) {
        this(texto, fechaEvolucion, usuario);
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = (recetas != null) ? recetas : new ArrayList<>();
        this.rutaPdf = rutaPdf;
    }

    public void anularPlantillaLaboratorio() {
        if (plantillaLaboratorio != null) {
            plantillaLaboratorio.anular();
        }
    }

    public void anularReceta(Receta receta) {
        if (recetas != null && recetas.contains(receta)) {
            receta.anular();
        }
    }
}
