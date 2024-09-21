package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;

public interface ServicioUsuario {
        void crearUsuario(Usuario usuario) throws Exception;
        void eliminarUsuario(String cuil) throws Exception;
        boolean verificarMatriculaMedica(String matricula);

}
