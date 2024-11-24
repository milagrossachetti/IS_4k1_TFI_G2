package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PacienteDTO {
    private Long cuil;
    private String nombre;
    private String apellido;
    private List<DiagnosticoDTO> diagnosticos; // Diagnósticos como parte del paciente
}
