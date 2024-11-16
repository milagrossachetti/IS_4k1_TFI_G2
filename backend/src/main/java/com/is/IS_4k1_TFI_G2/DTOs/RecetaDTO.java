package com.is.IS_4k1_TFI_G2.DTOs;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RecetaDTO {
    private List<String> medicamentos; // Lista de nombres de medicamentos seleccionados
    private boolean anulado; // Indicador de anulación de la receta
    private LocalDateTime fecha; // Fecha de creación de la receta
    private Usuario medico; // Usuario que representa al médico gestionando la receta

    public RecetaDTO() {
    }

    public RecetaDTO(List<String> medicamentos, boolean anulado, LocalDateTime fecha, Usuario medico) {
        if (medicamentos.size() > 2) {
            throw new IllegalArgumentException("Solo se pueden seleccionar hasta dos medicamentos por receta.");
        }
        this.medicamentos = medicamentos;
        this.anulado = anulado;
        this.fecha = fecha;
        this.medico = medico;
    }
}
