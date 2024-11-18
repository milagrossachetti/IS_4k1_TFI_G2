package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.DTOs.UsuarioDTO;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;

import java.util.Optional;

public interface ServicioUsuario {
    Usuario crearUsuario(UsuarioDTO usuario) throws Exception;
    void eliminarUsuario(Long cuil) throws Exception;
    boolean verificarMatriculaMedica(Long matriculaMedica);
    Usuario buscarUsuario(Long cuil);
}