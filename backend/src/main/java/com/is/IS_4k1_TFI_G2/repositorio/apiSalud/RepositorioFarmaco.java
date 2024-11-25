package com.is.IS_4k1_TFI_G2.repositorio.apiSalud;

import com.is.IS_4k1_TFI_G2.modelo.Medicamento;

import java.util.List;

public interface RepositorioFarmaco {

    /**
     * Busca medicamentos cuyos nombres contengan el texto dado (búsqueda parcial).
     *
     * @param nombre Texto parcial del nombre del medicamento.
     * @return Una lista de medicamentos que coincidan.
     */
    List<Medicamento> buscarPorNombre(String nombre);

    /**
     * Verifica si un medicamento con el nombre exacto existe.
     *
     * @param nombre Nombre del medicamento a verificar.
     * @return true si el medicamento existe, false en caso contrario.
     */
    boolean existeMedicamento(String nombre);
}
