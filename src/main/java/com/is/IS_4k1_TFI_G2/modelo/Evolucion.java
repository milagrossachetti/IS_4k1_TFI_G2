package com.is.IS_4k1_TFI_G2.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonBackReference
    private Diagnostico diagnostico;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable=false)

    private Usuario usuario;


    public Evolucion() {
    }

    public Evolucion(String texto, LocalDateTime fechaEvolucion, Usuario usuario) {
        this.texto = texto;
        this.fechaEvolucion =LocalDateTime.now();
        this.usuario= usuario;
    }

}