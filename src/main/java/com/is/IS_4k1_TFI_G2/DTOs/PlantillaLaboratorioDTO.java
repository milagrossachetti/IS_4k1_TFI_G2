package com.is.IS_4k1_TFI_G2.DTOs;

import com.is.IS_4k1_TFI_G2.modelo.TipoEstudioLaboratorio;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlantillaLaboratorioDTO {
    private List<String> tiposEstudios;
    private List<String> items;
    private String estado;

    public PlantillaLaboratorioDTO() {
    }

    public PlantillaLaboratorioDTO(List<String> tiposEstudios, List<String> items, String estado) {
        this.tiposEstudios = tiposEstudios;
        this.items = items;
        this.estado = estado;
    }

}