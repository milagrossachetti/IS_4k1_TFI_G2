package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter

public class DiagnosticoDTO {
    @NotNull
    private Long idHistoriaClinica;

    @NotNull
    private String nombreDiagnostico;

    private EvolucionDTO evolucionDTO;

    public DiagnosticoDTO() {}

    public DiagnosticoDTO(Long idHistoriaClinica, String nombreDiagnostico, EvolucionDTO evolucionDTO) {
        this.idHistoriaClinica = idHistoriaClinica;
        this.nombreDiagnostico = nombreDiagnostico;
        this.evolucionDTO = evolucionDTO;
    }
}