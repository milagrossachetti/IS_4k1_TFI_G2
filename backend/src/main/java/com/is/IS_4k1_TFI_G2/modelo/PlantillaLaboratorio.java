package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlantillaLaboratorio {
    private Long id; // Opcional si se usa para lógica de negocio, no para persistencia
    private List<String> tiposEstudios = new ArrayList<>();
    private List<String> items = new ArrayList<>();
    private String estado;

    public PlantillaLaboratorio() {
        this.estado = "Activo";
    }

    public PlantillaLaboratorio(List<String> tiposEstudios, List<String> items, String estado) {
        this.tiposEstudios = tiposEstudios != null ? tiposEstudios : new ArrayList<>();
        this.items = items != null ? items : new ArrayList<>();
        this.estado = estado != null ? estado : "Activo";
    }

    public void agregarItem(String tipoEstudio, String item) {
        if (tipoEstudio != null && item != null) {
            if (!tiposEstudios.contains(tipoEstudio)) {
                tiposEstudios.add(tipoEstudio);
            }
            items.add(item);
        }
    }

    public void anular() {
        this.estado = "Anulado";
    }
}
