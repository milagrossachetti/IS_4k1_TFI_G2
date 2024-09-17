package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioHistoriaClinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //indica que esta clase es un controlador REST que maneja solicitudes HTTP
@RequestMapping("/historia-clinica") //define la ruta base para todas las solicitudes en este controlador

public class ControladorHistoriaClinica {
    @Autowired
    private ServicioHistoriaClinica servicioHistoriaClinica;

    @PostMapping("/crear") //define el endpoint para crear una historia clínica. Usa el método HTTP POST porque estamos enviando datos para crear un nuevo recurso
    public ResponseEntity<String> crearHistoriaClinica(@RequestParam Long cuil) {
        //@RequestParam obtiene el cuil desde los parametros de la solicitud
        try{
            //crea una hc llamando al servicio
            servicioHistoriaClinica.crearHistoriaClinica(cuil);
            return new ResponseEntity<>("Historia clínica creaada con éxito", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
