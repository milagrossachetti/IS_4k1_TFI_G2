package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class EvolucionDTO {
    @NotNull
    private String texto;

    private PlantillaControlDTO plantillaControl;

    private PlantillaLaboratorioDTO plantillaLaboratorio;

    private List<RecetaDTO> recetas; // Lista de recetas asociadas a la evolución (opcional)

    public EvolucionDTO() {}

    public EvolucionDTO(String texto, PlantillaControlDTO plantillaControl, PlantillaLaboratorioDTO plantillaLaboratorio, List<RecetaDTO> recetas) {
        this.texto = texto;
        this.plantillaControl = plantillaControl;
        this.plantillaLaboratorio = plantillaLaboratorio;
        this.recetas = recetas;
    }
}

