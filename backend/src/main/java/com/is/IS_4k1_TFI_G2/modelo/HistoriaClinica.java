package com.is.IS_4k1_TFI_G2.modelo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaCreacion;

    private Paciente paciente;

    private List<Diagnostico> diagnosticos = new ArrayList<>();

    // Constructor con validación
    public HistoriaClinica(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente es obligatorio.");
        }
        this.paciente = paciente;
        this.fechaCreacion = LocalDate.now();
    }

    // Método para agregar un diagnóstico a la lista
    public void agregarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null && !this.diagnosticos.contains(diagnostico)) {
            this.diagnosticos.add(diagnostico);
            diagnostico.setHistoriaClinica(this);
        }
    }

    // Método para eliminar un diagnóstico
    public void eliminarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null && this.diagnosticos.contains(diagnostico)) {
            this.diagnosticos.remove(diagnostico);
            diagnostico.setHistoriaClinica(null);
        }
    }
}
