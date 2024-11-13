package com.is.IS_4k1_TFI_G2.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "historia_clinica_id", nullable = false)
    @JsonBackReference
    private HistoriaClinica historiaClinica;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy= "diagnostico", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<Evolucion> evoluciones =new ArrayList<>();


    public Diagnostico(){
    }

    public Diagnostico(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.evoluciones = new ArrayList<>();
    }

    public Diagnostico(String nombre, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.historiaClinica = historiaClinica;
        agregarEvolucion(primeraEvolucion);
    }

    public void agregarEvolucion(Evolucion evolucion) {
        this.evoluciones.add(evolucion);
        evolucion.setDiagnostico(this);
    }

}