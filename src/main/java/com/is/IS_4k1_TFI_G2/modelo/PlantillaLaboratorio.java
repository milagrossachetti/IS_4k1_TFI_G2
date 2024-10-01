package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlantillaLaboratorio {
    private String tipoEstudio;
    private List<String> items;
    private String estado;

    public PlantillaLaboratorio() {
    }

    public PlantillaLaboratorio(String tipoEstudio) {
        this.tipoEstudio = tipoEstudio;
        this.items = TipoEstudioLaboratorio.getItemsPorEstudio(tipoEstudio);
        this.estado = "Activo";
    }

    public void anular() {
        this.estado = "Anulado";
    }
}
