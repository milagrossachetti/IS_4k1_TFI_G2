package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.RecetaDigital;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioRecetaDigital;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.ServicioAPISalud;
import com.is.IS_4k1_TFI_G2.servicio.ServicioRecetaDigital;
import org.springframework.beans.factory.annotation.Autowired;

public class ServicioRecetaDigitalImpl implements ServicioRecetaDigital {

    private RepositorioRecetaDigital repositorioRecetaDigital;

    @Autowired
    private RepositorioPaciente repositorioPaciente;

    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private ServicioAPISalud servicioAPISalud;

    @Autowired
    private ServicioHistoriaClinica servicioHistoriaClinica;


    public void crearReceta (RecetaDigital recetaDigital) throws Exception {
    }



    @Override
    public void modificarReceta (RecetaDigital recetaDigital) throws Exception {

    }

    @Override
    public void eliminarReceta (RecetaDigital recetaDigital) throws Exception {

    }

    @Override
    public void validarRecetaDigital (RecetaDigital recetaDigital){
    }
}


