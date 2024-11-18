package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.DTOs.UsuarioDTO;
import com.is.IS_4k1_TFI_G2.DTOs.UsuarioInicioSesionDTO;
import com.is.IS_4k1_TFI_G2.excepcion.ElUsuarioYaExisteExcepcion;
import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.modelo.Rol;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioRol;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.ServicioUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class ControladorUsuario {
    @Autowired
    ServicioUsuario servicioUsuario;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private SecurityContextRepository securityContextRepository;

    @PostMapping("/registro")
    public ResponseEntity<RespuestaAPI<Usuario>> registro(@RequestBody UsuarioDTO usuarioDTO) throws Exception {
        RespuestaAPI<Usuario> respuesta = new RespuestaAPI<>(servicioUsuario.crearUsuario(usuarioDTO), "Usuario creado con éxito");
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioInicioSesionDTO usuarioInicioSesionDTO, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(usuarioInicioSesionDTO.getEmail(), usuarioInicioSesionDTO.getContrasenia());
        Authentication authenticationRequest = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationRequest);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return ResponseEntity.ok().body("usuario autenticado");
    }

    // Eliminar  usuario
    @DeleteMapping("/eliminar/{cuil}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("cuil") Long cuil) {
        try {
            servicioUsuario.eliminarUsuario(cuil);
            return ResponseEntity.status(HttpStatus.OK).body("El usuario fue eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{cuil}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long cuil) {
        Usuario buscarUsuario = servicioUsuario.buscarUsuario(cuil);
        return ResponseEntity.ok(buscarUsuario);
    }

}


