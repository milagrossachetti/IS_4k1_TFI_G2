package com.is.IS_4k1_TFI_G2.servicio;

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

import com.is.IS_4k1_TFI_G2.modelo.Usuario;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario{
@Autowired
RepositorioUsuario repositorioUsuario;
@Autowired
ServicioAPISalud servicioAPISalud;

    @Override
    public void crearUsuario(Usuario usuario) throws Exception {
       //verifica automaticamente la matricula al crear

        Optional<Usuario> existeUsuario = repositorioUsuario.findByCuil(usuario.getCuil());
        boolean verificarMatricula = servicioAPISalud.verificarMatriculaMedica(usuario.getMatricula());

        if(existeUsuario.isEmpty() && verificarMatricula){
            Usuario usuarioCreado = new Usuario(
                    usuario.getCuil(),
                    usuario.getDni(),
                    usuario.getMatricula(),
                    usuario.getEspecialidad(),
                    usuario.getNombreCompleto(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getPais(),
                    usuario.getLocalidad(),
                    usuario.getDireccion(),
                    usuario.getProvincia());
            repositorioUsuario.save(usuarioCreado);
        }else {
                throw new Exception("El usuario ya existe en el sistema o la matricula no es valida");

        }
    }

    @Override
    public void eliminarUsuario(String cuil) throws Exception {
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
    public boolean verificarMatriculaMedica(String matricula) {
        return servicioAPISalud.verificarMatriculaMedica(matricula);
    }
}
