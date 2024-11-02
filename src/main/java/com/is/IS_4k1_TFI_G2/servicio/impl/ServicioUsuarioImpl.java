package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import com.is.IS_4k1_TFI_G2.servicio.ServicioAPISalud;
import com.is.IS_4k1_TFI_G2.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {
    @Autowired
    RepositorioUsuario repositorioUsuario;

    @Autowired
    ServicioAPISalud servicioAPISalud;

    @Override
    public void crearUsuario(Usuario usuario) throws Exception{
        Optional<Usuario> existeUsuario = repositorioUsuario.findById(usuario.getCuil());

        boolean verificarMatricula = servicioAPISalud.verificarMatriculaMedica(usuario.getMatricula());

        if (existeUsuario.isEmpty() && verificarMatricula && "MEDICO".equalsIgnoreCase(usuario.getRol())) {
            // Crear un médico con matrícula y especialidad
            Usuario usuarioCreado = new Usuario(
                    usuario.getCuil(),
                    usuario.getDni(),
                    usuario.getMatricula(),
                    usuario.getNombreCompleto(),
                    usuario.getEspecialidad(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getPais(),
                    usuario.getLocalidad(),
                    usuario.getDireccion(),
                    usuario.getProvincia(),
                    usuario.getRol()
            );
            repositorioUsuario.save(usuarioCreado);
        } else if (existeUsuario.isEmpty() && "RECEPCIONISTA".equalsIgnoreCase(usuario.getRol())) {
            // Crear un recepcionista sin matrícula ni especialidad
            Usuario usuarioCreado = new Usuario(
                    usuario.getCuil(),
                    usuario.getDni(),
                    null, // No necesita matrícula
                    usuario.getNombreCompleto(),
                    null, // No necesita especialidad
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getPais(),
                    usuario.getLocalidad(),
                    usuario.getDireccion(),
                    usuario.getProvincia(),
                    usuario.getRol()
            );
            repositorioUsuario.save(usuarioCreado);
        } else {
            throw new Exception("El usuario ya existe en el sistema");
        }
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