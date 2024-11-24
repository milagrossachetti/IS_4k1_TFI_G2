package com.is.IS_4k1_TFI_G2.DTOs;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.Medicamento;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RecetaDTO {
    @NotNull
    @Size(max = 2, message = "Solo se permiten hasta dos medicamentos.")
    private List<String> medicamentos;

    private boolean anulado;
    private LocalDateTime fecha;
    private Long idMedico;
    private String nombreMedico;

    public RecetaDTO() {}

    public RecetaDTO(List<String> medicamentos, boolean anulado, LocalDateTime fecha, Long idMedico, String nombreMedico) {
        this.medicamentos = medicamentos;
        this.anulado = anulado;
        this.fecha = fecha;
        this.idMedico = idMedico;
        this.nombreMedico = nombreMedico;
    }
}
