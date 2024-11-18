package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.ServicioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    ServicioUsuario servicioUsuario;

    @Autowired
    ServicioUsuarioImpl servicioUsuarioImpl;

    @Autowired
    RepositorioUsuario repositorioUsuario;

    //anda
    @PostMapping("/crear")
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) throws Exception {
        try{
            servicioUsuario.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("El usuario fue creado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //anda
    @DeleteMapping("/eliminar/{cuil}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("cuil") Long cuil) {
        try {
            servicioUsuario.eliminarUsuario(cuil);
            return ResponseEntity.status(HttpStatus.OK).body("El usuario fue eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //anda
    @GetMapping("/buscar/{cuil}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long cuil) {
        Usuario buscarUsuario = servicioUsuario.buscarUsuario(cuil);
        return ResponseEntity.ok(buscarUsuario);
    }

}


