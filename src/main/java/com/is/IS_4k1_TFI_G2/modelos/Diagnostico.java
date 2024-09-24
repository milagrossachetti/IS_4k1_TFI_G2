package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter


public class Diagnostico {
    @Id
    @GeneratedValue

    private Long Id;
    private String nombre;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn (name = "historia_clinica_id", nullable = false)
    private HistoriaClinica historiaClinica;

    @OneToMany(mappedBy= "diagnostico", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Evolucion> evoluciones =new ArrayList<>();


    public Diagnostico(){
    }

    public Diagnostico(String nombre, Medico medico) {
        this.nombre = nombre;
        this.medico = medico;
        this.evoluciones = new ArrayList<>();
    }

    public Diagnostico(String nombre, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, Medico medico) {
        this.nombre = nombre;
        this.medico = medico;
        this.historiaClinica = historiaClinica;
        agregarEvolucion(primeraEvolucion);
    }

    public void agregarEvolucion(Evolucion evolucion) {
        this.evoluciones.add(evolucion);
        evolucion.setDiagnostico(this);
    }

}
