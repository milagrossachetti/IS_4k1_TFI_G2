package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class Evolucion {
    @Id
    @GeneratedValue

    private Long Id;
    private LocalDateTime fechaEvolucion;
    private String texto;

    @ManyToOne
    @JoinColumn(name = "diagnostico_id",nullable = false)
    private Diagnostico diagnostico;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable=false)
    private Medico medico;


    public Evolucion() {
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Medico medico) {
        this.texto = texto;
        this.fechaEvolucion =LocalDateTime.now();
        this.medico= medico;
    }

}
