package com.is.IS_4k1_TFI_G2.servicio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.modelo.RecetaDigital;

public interface ServicioRecetaDigital {

    void crearReceta(RecetaDigital recetaDigital) throws Exception;
    void validarRecetaDigital(RecetaDigital recetaDigital) throws Exception;
    void modificarReceta(RecetaDigital recetaDigital) throws Exception;

    void eliminarReceta(RecetaDigital recetaDigital) throws Exception;
    // (la baja es una suspensión)
}
