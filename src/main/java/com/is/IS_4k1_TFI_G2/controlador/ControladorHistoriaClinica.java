package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.modelo.HistoriaClinica;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioHistoriaClinica;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioPacienteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historia-clinica")
public class ControladorHistoriaClinica {
    @Autowired
    private ServicioHistoriaClinica servicioHistoriaClinica;

    @Autowired
    private ServicioPacienteImpl servicioPacienteImpl;

    //anda
    @PostMapping("/crear/{cuil}")
    public ResponseEntity<String> crearHistoriaClinica(@PathVariable Long cuil) {
        try {
            servicioHistoriaClinica.crearHistoriaClinica(cuil);
            return new ResponseEntity<>("Historia clínica creada con éxito", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //anda
    @GetMapping("/tiene/{cuil}")
    public ResponseEntity<HistoriaClinica> tieneHistoriaClinica(@PathVariable Long cuil) {
        HistoriaClinica tieneHistoriaClinica = servicioHistoriaClinica.tieneHistoriaClinica(cuil);
        return ResponseEntity.ok(tieneHistoriaClinica);
    }
}

