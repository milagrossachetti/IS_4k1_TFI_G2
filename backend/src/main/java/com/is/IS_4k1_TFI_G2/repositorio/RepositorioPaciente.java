package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.modelo.MedicamentoRecetado;


import java.util.Optional;

public interface RepositorioPaciente {
    Optional<Paciente> buscarPorCuil(Long cuil); // Busca un paciente por CUIL.
    void guardarPaciente(Paciente paciente);    // Guarda o actualiza un paciente.

}
