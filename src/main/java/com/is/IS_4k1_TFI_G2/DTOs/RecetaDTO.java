package com.is.IS_4k1_TFI_G2.DTOs;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RecetaDTO {
    private List<String> medicamentos;
    private boolean anulado;
    private LocalDateTime fecha;
    private Usuario medico;
    private String estado;

    public RecetaDTO() {
    }

    public RecetaDTO(List<String> medicamentos, boolean anulado, LocalDateTime fecha, Usuario medico, String estado) {
        if (medicamentos.size() > 2) {
            throw new IllegalArgumentException("Solo se pueden seleccionar hasta dos medicamentos por receta.");
        }
        this.medicamentos = medicamentos;
        this.anulado = anulado;
        this.fecha = fecha;
        this.medico = medico;
        this.estado = estado != null ? estado : "Activo";
    }
}