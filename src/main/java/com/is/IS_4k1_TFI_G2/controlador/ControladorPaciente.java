package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.servicio.ServicioPaciente;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioHistoriaClinica;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioPacienteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paciente")
public class ControladorPaciente {
    @Autowired
    ServicioPaciente servicioPaciente;

    @Autowired
    private ServicioPacienteImpl servicioPacienteImpl;

    @Autowired
    private ServicioHistoriaClinica servicioHistoriaClinica;

    //anda
    @PostMapping("/crear")
    public ResponseEntity<String> crearPaciente(@RequestBody Paciente paciente){
        try {
            servicioPaciente.crearPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body("El paciente fue creado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //anda
    @PutMapping("/modificar/{cuil}")
    public ResponseEntity<String> modificarPaciente(@PathVariable("cuil") Long cuil, @RequestBody Paciente paciente){
        try {
            servicioPaciente.modificarPaciente(cuil, paciente);
            return ResponseEntity.status(HttpStatus.OK).body("El paciente fue modificado con éxito");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //anda
    @DeleteMapping("/eliminar/{cuil}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable("cuil") Long cuil){
        try{
            servicioPaciente.eliminarPaciente(cuil);
            return ResponseEntity.status(HttpStatus.OK).body("El paciente fue eliminado con éxito");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //anda
    @GetMapping("/buscar/{cuil}")
    public ResponseEntity<Paciente> verificarPaciente(@PathVariable Long cuil) {
       Paciente existePaciente = servicioPacienteImpl.buscarPaciente(cuil);
       return ResponseEntity.ok(existePaciente);
    }

}