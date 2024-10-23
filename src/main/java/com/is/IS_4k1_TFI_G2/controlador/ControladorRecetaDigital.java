package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.modelo.RecetaDigital;
import com.is.IS_4k1_TFI_G2.servicio.ServicioRecetaDigital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ControladorRecetaDigital {

    @Autowired
    ServicioRecetaDigital servicioRecetaDigital;

    @PostMapping("/crear")
    public ResponseEntity<String> crearReceta(@RequestBody RecetaDigital recetaDigital) {
        try {
            servicioRecetaDigital.crearReceta(recetaDigital);
            return ResponseEntity.status(HttpStatus.CREATED).body("La receta digital fue creada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/modificar")
    public ResponseEntity<String> modificarReceta(@RequestBody RecetaDigital recetaDigital) {
        try {
            servicioRecetaDigital.modificarReceta(recetaDigital);
            return ResponseEntity.status(HttpStatus.CREATED).body("La receta digital fue editada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/eliminar")
    public ResponseEntity<String> eliminarReceta(@RequestBody RecetaDigital recetaDigital) {
        try {
            servicioRecetaDigital.eliminarReceta(recetaDigital);
            return ResponseEntity.status(HttpStatus.CREATED).body("La receta digital fue eliminada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
