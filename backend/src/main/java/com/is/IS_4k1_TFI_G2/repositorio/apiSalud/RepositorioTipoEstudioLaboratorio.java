package com.is.IS_4k1_TFI_G2.repositorio.apiSalud;

import com.is.IS_4k1_TFI_G2.modelo.PlantillaLaboratorio;

import java.util.List;

public interface RepositorioTipoEstudioLaboratorio {

    /**
     * Obtiene todos los tipos de estudios de laboratorio disponibles.
     *
     * @return Lista de todos los estudios.
     */
    List<PlantillaLaboratorio> obtenerTodos();

    /**
     * Obtiene los ítems asociados a un tipo de estudio de laboratorio por su nombre.
     *
     * @param nombreEstudio Nombre del estudio.
     * @return Lista de ítems asociados al estudio.
     * @throws IllegalArgumentException Si el estudio no existe.
     */
    List<String> getItemsPorEstudio(String nombreEstudio);
}
