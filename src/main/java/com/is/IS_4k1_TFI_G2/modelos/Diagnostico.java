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
    //segun el modelo del dominio tiene descripción, para que seria eso?

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;
    //aqui es donde se tendria que conectar el medico que se autentico

    @ManyToOne
    @JoinColumn (name = "historial_clinico_id", nullable = false)
    private HistorialClinico historialClinico;

    @OneToMany(mappedBy= "diagnostico", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Evolucion> evoluciones =new ArrayList<>();


    public Diagnostico(){
    }

    public Diagnostico(String nombre, HistorialClinico historialClinico, Evolucion primeraEvolucion) {
        this.nombre = nombre;
        this.medico = medico;
        this.historialClinico = historialClinico;
        agregarEvolucion(primeraEvolucion);
    }

    public void agregarEvolucion(Evolucion evolucion) {
        this.evoluciones.add(evolucion);
        evolucion.setDiagnostico(this);
    }




}
