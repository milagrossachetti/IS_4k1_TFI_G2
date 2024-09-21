package com.is.IS_4k1_TFI_G2.controlador;


import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioHistoriaClinica;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioPacienteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //indica que esta clase es un controlador REST que maneja solicitudes HTTP
@RequestMapping("/historia-clinica") //define la ruta base para todas las solicitudes en este controlador
public class ControladorHistoriaClinica {
    @Autowired
    private ServicioHistoriaClinica servicioHistoriaClinica;

    @Autowired
    private ServicioPacienteImpl servicioPacienteImpl;

    @PostMapping("/crear-historia-clinica/{cuil}") //define el endpoint para crear una historia clínica. Usa el método HTTP POST porque estamos enviando datos para crear un nuevo recurso
    public ResponseEntity<String> crearHistoriaClinica(@PathVariable Long cuil) {
        //@PathVariable obtiene el cuil desde la URL
        try {
            //crea una hc llamando al servicio
            servicioHistoriaClinica.crearHistoriaClinica(cuil);
            return new ResponseEntity<>("Historia clínica creada con éxito", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/tiene-historia-clinica/{cuil}")
    public ResponseEntity<Boolean> verificarHistoriaClinica(@PathVariable Long cuil) {
        boolean tieneHistoria = servicioPacienteImpl.tieneHistoriaClinica(cuil);
        return ResponseEntity.ok(tieneHistoria);
    }
}

