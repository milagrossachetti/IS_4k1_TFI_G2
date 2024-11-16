package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Evolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaEvolucion;
    private String texto;

    @ManyToOne
    @JoinColumn(name = "diagnostico_id", nullable = false)
    @JsonBackReference
    private Diagnostico diagnostico;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "cuil", nullable = false)
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    private PlantillaControl plantillaControl;

    @OneToOne(cascade = CascadeType.ALL)
    private PlantillaLaboratorio plantillaLaboratorio;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "evolucion_id")
    private List<Receta> recetas;

    private String rutaPdf;

    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario) {
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.usuario = usuario;
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio,
                     List<Receta> recetas, String rutaPdf) {
        this.texto = texto;
        this.fechaEvolucion = fechaEvolucion;
        this.usuario = usuario;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = recetas;
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
