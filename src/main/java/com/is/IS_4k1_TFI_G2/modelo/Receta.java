package com.is.IS_4k1_TFI_G2.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Usuario medico;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Medicamento> medicamentos;

    private boolean anulada;

    @ManyToOne
    @JoinColumn(name = "evolucion_id", nullable = false)
    @JsonBackReference
    private Evolucion evolucion;


    private String rutaPdf; // Ruta del archivo PDF generado para esta receta

    public Receta() {}

    public Receta(LocalDateTime fecha, Usuario medico, List<Medicamento> medicamentos, Evolucion evolucion) {
        this.fecha = fecha;
        this.medico = medico;
        this.medicamentos = medicamentos;
        this.evolucion = evolucion;
        this.anulada = false;
    }

    public void anular() {
        this.anulada = true;
    }

    public boolean isAnulada(){
        return this.anulada;
    }

    // Método para establecer la ruta del PDF
    public void setRutaPdf(String rutaPdf) {
        this.rutaPdf = rutaPdf;
    }
}