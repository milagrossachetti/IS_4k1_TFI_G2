package com.is.IS_4k1_TFI_G2.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity

public class PlantillaLaboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> tiposEstudios;
    private List<String> items;
    private String estado;

    public PlantillaLaboratorio() {
        this.tiposEstudios = new ArrayList<>();
        this.items = new ArrayList<>();
        this.estado = "Activo";
    }

    public PlantillaLaboratorio(List<String> tiposEstudios, List<String> items, String estado) {
        this.tiposEstudios = tiposEstudios;
        this.items = items;
        this.estado = estado;
    }

    public void agregarItem(String tipoEstudio, String item) {
        if (!tiposEstudios.contains(tipoEstudio)) {
            tiposEstudios.add(tipoEstudio);
        }
        items.add(item);
    }

    public void anular() {
        this.estado = "Anulado";
    }
}