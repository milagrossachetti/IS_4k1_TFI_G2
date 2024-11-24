package com.is.IS_4k1_TFI_G2.controlador;

import com.is.IS_4k1_TFI_G2.DTOs.UsuarioInicioSesionDTO;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.servicio.impl.ServicioUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/usuario")
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    // Inyección de dependencias por constructor
    public ControladorUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    /**
     * Endpoint para autenticar a un usuario.
     *
     * @param usuarioInicioSesionDTO DTO con email y contraseña.
     * @return Respuesta con estado HTTP y mensaje de autenticación o error.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid UsuarioInicioSesionDTO usuarioInicioSesionDTO) {
        try {
            Usuario usuario = servicioUsuario.autenticarUsuario(
                    usuarioInicioSesionDTO.getEmail(),
                    usuarioInicioSesionDTO.getContrasenia()
            );

            // Crear respuesta detallada
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario autenticado");
            response.put("usuario", Map.of(
                    "id", usuario.getCuil(),
                    "nombre", usuario.getNombreCompleto(),
                    "rol", usuario.getRol() // Asegúrate de que `Usuario` tenga este campo o su equivalente.
            ));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ocurrió un error inesperado."));
        }
    }
}
