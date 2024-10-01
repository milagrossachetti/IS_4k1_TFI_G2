package com.is.IS_4k1_TFI_G2.DTOs;

import com.is.IS_4k1_TFI_G2.modelo.TipoEstudioLaboratorio;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlantillaLaboratorioDTO {
    private String tipoEstudio;
    private List<String> items;
    private String estado;

    public PlantillaLaboratorioDTO() {
    }

    public PlantillaLaboratorioDTO(String tipoEstudio, List<String> items, String estado) {
        this.tipoEstudio = tipoEstudio;
        this.items = TipoEstudioLaboratorio.getItemsPorEstudio(tipoEstudio);
        this.estado = estado;
    }
}
