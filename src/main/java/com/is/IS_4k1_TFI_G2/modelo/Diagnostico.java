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
    @JoinColumn(name = "historia_clinica_id", nullable = false)
    @JsonBackReference
    private HistoriaClinica historiaClinica;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "diagnostico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Evolucion> evoluciones = new ArrayList<>();  // Inicialización de la lista

    // Constructor principal: centraliza la inicialización de la lista de evoluciones
    public Diagnostico(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.evoluciones = new ArrayList<>();
    }

    // Constructor alternativo que llama al constructor principal para evitar duplicación
    public Diagnostico(String nombre, HistoriaClinica historiaClinica, Evolucion primeraEvolucion, Usuario usuario) {
        this(nombre, usuario); // Llamada al constructor principal
        this.historiaClinica = historiaClinica;
        agregarEvolucion(primeraEvolucion); // Añadir la primera evolución
    }

    // Método para agregar una evolución con verificación de null
    public void agregarEvolucion(Evolucion evolucion) {
        if (evolucion != null) { // Evitar agregar evoluciones nulas
            this.evoluciones.add(evolucion);
            evolucion.setDiagnostico(this);
        }
    }
}
