package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter

public class Evolucion {
    @Id
    @GeneratedValue

    private Long Id;
    private LocalDate fechaEvolucion;
    private String texto;

    @ManyToOne
    @JoinColumn(name = "diagnostico_id",nullable = false)
    private Diagnostico diagnostico;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable=false)
    private Medico medico;
    //aqui es donde se tendria que conectar el medico que se autentico

    public Evolucion() {
    }

    public Evolucion(String texto, LocalDate fechaEvolucion, Medico medico) {
        this.texto = texto;
        this.fechaEvolucion =fechaEvolucion;
        this.medico= medico; //se modifica cuando tengamos autenticacion y se registre el medico
    }

}
