package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.PacienteDato;
import com.is.IS_4k1_TFI_G2.modelo.listaDeDato.TipoEstudioLaboratorio;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioPaciente {

    public Paciente buscarPorCuil(Long cuil) {
        return PacienteDato.buscarPorCuil(cuil);
    }

    public TipoEstudioLaboratorio buscarTipoEstudio(String nombre) {
        return TipoEstudioLaboratorio.obtenerTodos().stream()
                .filter(estudio -> estudio.getNombreEstudio().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Estudio no encontrado: " + nombre));
    }

    public void guardarPaciente(Paciente paciente) {
        Paciente existente = buscarPorCuil(paciente.getCuil());
        if (existente != null) {
            PacienteDato.actualizarPaciente(paciente);
        } else {
            throw new RuntimeException("El paciente no existe en el repositorio.");
        }
    }
}

