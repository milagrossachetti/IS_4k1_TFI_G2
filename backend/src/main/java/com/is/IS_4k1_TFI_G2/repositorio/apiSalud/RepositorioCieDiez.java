package com.is.IS_4k1_TFI_G2.repositorio.apiSalud;

import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;

import java.util.List;

public interface RepositorioCieDiez {

    /**
     * Busca diagnósticos cuyo nombre contenga el texto proporcionado.
     *
     * @param nombre Texto parcial del nombre del diagnóstico.
     * @return Lista de diagnósticos que coincidan con la búsqueda.
     */
    List<Diagnostico> buscarPorNombre(String nombre);

    /**
     * Obtiene todos los diagnósticos registrados.
     *
     * @return Lista de todos los diagnósticos.
     */
    List<Diagnostico> obtenerTodos();
}
