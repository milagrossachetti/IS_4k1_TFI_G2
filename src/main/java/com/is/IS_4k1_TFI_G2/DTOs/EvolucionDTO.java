package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvolucionDTO {
    private String texto;
    private PlantillaControlDTO plantillaControl;
    private List<PlantillaLaboratorioDTO> plantillasLaboratorio;

    public EvolucionDTO() {
    }

    public EvolucionDTO(String texto, PlantillaControlDTO plantillaControl, List<PlantillaLaboratorioDTO> plantillasLaboratorio) {
        this.texto = texto;
        this.plantillaControl = plantillaControl;
        this.plantillasLaboratorio = plantillasLaboratorio;
    }
}
