package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter

public class HistorialClinico {
    @Id
    @GeneratedValue

    private Long Id;
    private LocalDate fecha;

    @OneToOne
    private Paciente paciente;

    @OneToMany(mappedBy = "historialClinico", cascade = CascadeType.ALL, orphanRemoval = true) //mappedBy indica que la relacion es bidireccional y que historialClinico es la que gestiona la relacion
    private List<Diagnostico> diagnosticos;

    public HistorialClinico(){
    }

    public HistorialClinico(Paciente paciente){
        this.paciente= paciente;
    }

    public void agregarDiagnostico(Diagnostico diagnostico){
        this.diagnosticos.add(diagnostico);
        diagnostico.setHistorialClinico(this);
    }
}
