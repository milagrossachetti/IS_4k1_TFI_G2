package com.is.IS_4k1_TFI_G2.controlador;
import com.is.IS_4k1_TFI_G2.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    ServicioUsuario servicioUsuario;

    // Crear un nuevo usuario
    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        try {
            servicioUsuario.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("El usuario fue creado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Eliminar  usuario
    @DeleteMapping("/eliminar/{cuil}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("cuil") String cuil) {
        try {
            servicioUsuario.eliminarUsuario(cuil);
            return ResponseEntity.status(HttpStatus.OK).body("El usuario fue eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Verificar matrícula médica mediante la API de salud
    @GetMapping("/verificar-matricula/{matricula}")
    public ResponseEntity<String> verificarMatricula(@PathVariable("matricula") String matricula) {
        try {
            boolean matriculaValida = servicioUsuario.verificarMatriculaMedica(matricula);
            if (matriculaValida) {
                return ResponseEntity.status(HttpStatus.OK).body("La matrícula es válida");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La matrícula no es válida");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}



