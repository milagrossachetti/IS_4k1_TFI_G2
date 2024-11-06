package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.DTOs.PacienteDTO;
import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.servicio.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paciente")
public class ControladorPaciente {
    @Autowired
    ServicioPaciente servicioPaciente;

    @PostMapping("/crear")
    public ResponseEntity<RespuestaAPI<Paciente>> crearPaciente(@RequestBody Paciente paciente) throws Exception {
        RespuestaAPI<Paciente> response = new RespuestaAPI<>(servicioPaciente.crearPaciente(paciente), "Paciente con cuil " +paciente.getCuil()+ " creado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/modificar/{cuil}")
    public ResponseEntity<RespuestaAPI<Paciente>> modificarPaciente(@PathVariable("cuil") Long cuil, @RequestBody PacienteDTO paciente) throws Exception {
        RespuestaAPI<Paciente> response = new RespuestaAPI<>(servicioPaciente.modificarPaciente(cuil, paciente), "Paciente con cuil "+cuil+" modificado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{cuil}")
    public ResponseEntity<RespuestaAPI<Paciente>> eliminarPaciente(@PathVariable("cuil") Long cuil){
        RespuestaAPI<Paciente> response = new RespuestaAPI<>(servicioPaciente.eliminarPaciente(cuil), "Paciente con cuil "+cuil+" eliminado exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/buscar/{cuil}")
    public ResponseEntity<RespuestaAPI<Paciente>> verificarPaciente(@PathVariable Long cuil) {
        RespuestaAPI<Paciente> response = new RespuestaAPI<>(servicioPaciente.buscarPaciente(cuil), "Se obtuvo el paciente con cuil " + cuil + " exitosamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}