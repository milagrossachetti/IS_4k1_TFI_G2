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
    @JoinColumn(name = "medico_id", nullable = false)
    private Usuario usuario;

    @OneToOne
    private PlantillaControl plantillaControl;

    @OneToOne
    private PlantillaLaboratorio plantillaLaboratorio;

    private String rutaPdf;

    public Evolucion() {}

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario) {
        this.texto = texto;
        this.fechaEvolucion = LocalDateTime.now();
        this.usuario = usuario;
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario,
                     PlantillaControl plantillaControl, PlantillaLaboratorio plantillaLaboratorio, String rutaPdf) {
        this.texto = texto;
        this.fechaEvolucion = LocalDateTime.now();
        this.usuario = usuario;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.rutaPdf = rutaPdf;
    }

    public void anularPlantillaLaboratorio() {
        if (plantillaLaboratorio != null) {
            plantillaLaboratorio.anular();
        }
    }

}