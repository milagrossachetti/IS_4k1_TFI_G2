package com.is.IS_4k1_TFI_G2.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paciente")
public class ControladorLogin {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public ControladorLogin(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        // Guarda el contexto de autenticación en la sesión (si es necesario)
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        return ResponseEntity.ok().body("usuario autenticado");
    }

    public record LoginRequest(String email, String password) {
    }

    /*es su responsabilidad guardar el usuario autenticado en SecurityContextRepository si es necesario. Por ejemplo, si utiliza HttpSession para conservar SecurityContext entre solicitudes, puede utilizar HttpSessionSecurityContextRepository.*/
}
