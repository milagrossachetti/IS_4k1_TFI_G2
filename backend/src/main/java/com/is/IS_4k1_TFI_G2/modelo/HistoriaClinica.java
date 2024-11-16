package com.is.IS_4k1_TFI_G2.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaCreacion;

    @OneToOne
    @JoinColumn(name = "paciente_cuil", referencedColumnName = "cuil")
    @JsonBackReference
    private Paciente paciente;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Diagnostico> diagnosticos;

    public HistoriaClinica(Paciente paciente){
        this.paciente= paciente;
        this.fechaCreacion = LocalDate.now();
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now(); // Asigna la fecha de creación aquí
    }

    public void agregarDiagnostico(Diagnostico diagnostico){
        this.diagnosticos.add(diagnostico);
        diagnostico.setHistoriaClinica(this);
    }
}