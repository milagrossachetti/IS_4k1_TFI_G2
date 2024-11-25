package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Diagnostico {
    private Long id;
    private HistoriaClinica historiaClinica;
    private String nombreDiagnostico;
    private Usuario medico; // Médico que creó el diagnóstico
    private List<Evolucion> evoluciones = new ArrayList<>();

    // Constructor por defecto
    public Diagnostico() {}

    // Constructor con solo el nombre del diagnóstico
    public Diagnostico(String nombreDiagnostico) {
        if (nombreDiagnostico == null) {
            throw new IllegalArgumentException("El nombre del diagnóstico no puede ser nulo.");
        }
        this.nombreDiagnostico = nombreDiagnostico;
    }

    // Constructor con nombre del diagnóstico y médico
    public Diagnostico(String nombreDiagnostico, Usuario medico) {
        if (nombreDiagnostico == null || medico == null) {
            throw new IllegalArgumentException("El nombre del diagnóstico y el médico no pueden ser nulos.");
        }
        this.nombreDiagnostico = nombreDiagnostico;
        this.medico = medico;
    }

    // Constructor con historia clínica
    public Diagnostico(String nombreDiagnostico, HistoriaClinica historiaClinica, Usuario medico) {
        this(nombreDiagnostico, medico);
        if (historiaClinica == null) {
            throw new IllegalArgumentException("La historia clínica no puede ser nula.");
        }
        this.historiaClinica = historiaClinica;
    }

    // Constructor con nombre, historia clínica, evolución inicial y usuario
    public Diagnostico(String nombreDiagnostico, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, Usuario medico) {
        this(nombreDiagnostico, historiaClinica, medico);
        agregarEvolucion(primeraEvolucion);
    }


    public String getNombreDiagnostico() {
        return this.nombreDiagnostico;
    }

    // Agregar evolución al diagnóstico
    public void agregarEvolucion(Evolucion evolucion) {
        if (evolucion != null && !evoluciones.contains(evolucion)) {
            this.evoluciones.add(evolucion);
            evolucion.setDiagnostico(this);
        }
    }
}
