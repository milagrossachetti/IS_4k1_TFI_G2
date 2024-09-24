package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class HistoriaClinica {
    @Id
    private Long id;
    private Date fechaCreacion;
    @OneToOne(mappedBy = "historiaClinica")
    private Paciente paciente;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnostico> diagnosticos;

    public HistoriaClinica(){
    }

    public HistoriaClinica(Paciente paciente){
        this.paciente= paciente;
    }

    public void agregarDiagnostico(Diagnostico diagnostico){
        this.diagnosticos.add(diagnostico);
        diagnostico.setHistoriaClinica(this);
    }
}