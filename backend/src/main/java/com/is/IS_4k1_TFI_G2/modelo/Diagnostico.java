package com.is.IS_4k1_TFI_G2.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Usuario usuario; // Médico que creó el diagnóstico
    private List<Evolucion> evoluciones = new ArrayList<>();

    public Diagnostico() {}

    public Diagnostico(String nombreDiagnostico, Usuario usuario) {
        if (nombreDiagnostico == null || usuario == null) {
            throw new IllegalArgumentException("Nombre de diagnóstico y usuario son obligatorios.");
        }
        this.nombreDiagnostico = nombreDiagnostico;
        this.usuario = usuario;
    }

    public Diagnostico(String nombreDiagnostico, HistoriaClinica historiaClinica, Usuario usuario) {
        this(nombreDiagnostico, usuario);
        if (historiaClinica == null) {
            throw new IllegalArgumentException("La historia clínica es obligatoria.");
        }
        this.historiaClinica = historiaClinica;
    }

    public Diagnostico(String nombreDiagnostico, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, Usuario usuario) {
        this(nombreDiagnostico, historiaClinica, usuario);
        agregarEvolucion(primeraEvolucion);
    }

    public void agregarEvolucion(Evolucion evolucion) {
        if (evolucion != null && !evoluciones.contains(evolucion)) {
            this.evoluciones.add(evolucion);
            evolucion.setDiagnostico(this);
        }
    }
}
