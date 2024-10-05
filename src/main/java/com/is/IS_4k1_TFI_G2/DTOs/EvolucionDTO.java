package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvolucionDTO {
    private String texto;
    private PlantillaControlDTO plantillaControl;
    private PlantillaLaboratorioDTO plantillaLaboratorio;


    public EvolucionDTO() {
    }

    public EvolucionDTO(String texto, PlantillaControlDTO plantillaControl, PlantillaLaboratorioDTO plantillaLaboratorio) {
        this.texto = texto;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
    }

}
