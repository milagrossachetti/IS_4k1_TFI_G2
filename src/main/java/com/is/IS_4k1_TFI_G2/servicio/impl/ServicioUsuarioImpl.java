package com.is.IS_4k1_TFI_G2.servicio.impl;

/*
Interfaz (Interface): Define el contrato o los métodos que deben ser implementados. No contiene la
lógica de negocio, solo las firmas de los métodos. Por ejemplo, UserService define los
métodos como createUser, deleteUser, y verifyMedicalLicense.

Implementación (Impl): Es la clase que proporciona
la lógica específica para los métodos definidos en la
interfaz. En el caso de UserServiceImpl, esta clase implementa
los métodos createUser, deleteUser, y verifyMedicalLicense con
la lógica de negocio real que realiza esas operaciones.
*/

import com.is.IS_4k1_TFI_G2.DTOs.UsuarioDTO;
import com.is.IS_4k1_TFI_G2.controlador.RespuestaAPI;
import com.is.IS_4k1_TFI_G2.excepcion.ElPacienteYaExisteExcepcion;
import com.is.IS_4k1_TFI_G2.excepcion.ElUsuarioYaExisteExcepcion;
import com.is.IS_4k1_TFI_G2.modelo.Rol;
import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioRol;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.ServicioAPISalud;
import com.is.IS_4k1_TFI_G2.servicio.ServicioUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    @Autowired
    RepositorioUsuario repositorioUsuario;
    @Autowired
    RepositorioRol repositorioRol;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ServicioAPISalud servicioAPISalud;

    @Override
    @Transactional
    public Usuario crearUsuario(UsuarioDTO usuarioDTO){
        if (repositorioUsuario.existsByEmail(usuarioDTO.getEmail()) || repositorioUsuario.existsById(usuarioDTO.getCuil())){
            throw new ElUsuarioYaExisteExcepcion("El usuario ya existe en el sistema, corrobore su email o cuil.");
        }
        String rolNombre = (usuarioDTO.getEspecialidad() == null && usuarioDTO.getMatricula() == null) ? "RECEPCIONISTA" : "MEDICO";
        Rol rol = repositorioRol.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol '" + rolNombre + "' no encontrado"));
        Usuario usuario = new Usuario(usuarioDTO.getCuil(),
                usuarioDTO.getEmail(),
                passwordEncoder.encode(usuarioDTO.getContrasenia()),
                rol,
                usuarioDTO.getMatricula(),
                usuarioDTO.getEspecialidad(),
                usuarioDTO.getDni(),
                usuarioDTO.getNombreCompleto(),
                usuarioDTO.getTelefono(),
                usuarioDTO.getDireccion(),
                usuarioDTO.getLocalidad(),
                usuarioDTO.getProvincia(),
                usuarioDTO.getPais());
        repositorioUsuario.save(usuario);
        return usuario;
    }

    public boolean verificarMatricula(Long matriculaMedica) throws Exception {
        boolean verificarMatricula = servicioAPISalud.verificarMatriculaMedica(matriculaMedica);
        boolean esCorrecto = false;
        if (verificarMatricula){
            esCorrecto = true;
        } else throw new Exception("Los datos de la matricula no son correctos");
        return esCorrecto;
    }

    @Override
    public void eliminarUsuario(Long cuil) throws Exception {
        // Buscar el usuario por su CUIL
        Optional<Usuario> usuario = repositorioUsuario.findByCuil(cuil);

        if (usuario.isPresent()) {
            // Eliminar el usuario si existe
            repositorioUsuario.delete(usuario.get());
        } else {
            // Lanzar excepción si el usuario no existe
            throw new Exception("El usuario no existe en el sistema.");
        }
    }

    @Override
    public boolean verificarMatriculaMedica(Long matricula) {
        return servicioAPISalud.verificarMatriculaMedica(matricula);
    }

    public Usuario buscarUsuario(Long cuil) {
        return repositorioUsuario.findByCuil(cuil).orElse(null);
    }

}