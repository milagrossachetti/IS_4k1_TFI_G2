package com.is.IS_4k1_TFI_G2.servicios.impl;

import com.is.IS_4k1_TFI_G2.modelos.Estado;
import com.is.IS_4k1_TFI_G2.modelos.Paciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import com.is.IS_4k1_TFI_G2.servicios.ServicioAPISalud;
import com.is.IS_4k1_TFI_G2.servicios.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicioPacienteImpl implements ServicioPaciente {
    @Autowired
    RepositorioPaciente repositorioPaciente;
    @Autowired
    ServicioAPISalud servicioAPISalud;

    //al crear el paciente se crea automáticamente con el estado "activo"
    @Override
    public void crearPaciente(Paciente paciente) throws Exception {
        Optional<Paciente> existePaciente = repositorioPaciente.findById(paciente.getCuil());
        boolean verificarNroAfiliado = servicioAPISalud.verificarNumeroAfiliado(paciente.getObraSocialId(), paciente.getNroAfiliado());
        boolean verificarObraSocial = servicioAPISalud.verificarObraSocial(paciente.getObraSocialId());
        if (existePaciente.isEmpty() && verificarNroAfiliado && verificarObraSocial){
            Paciente pacienteCreado = new Paciente(paciente.getCuil(), paciente.getDni(),
                    paciente.getNombreCompleto(), paciente.getFechaNacimiento(),
                    paciente.getNumeroTelefono(), paciente.getEmail(),
                    paciente.getDireccion(), paciente.getLocalidad(), paciente.getProvincia(),
                    paciente.getPais(), paciente.getNroAfiliado(), paciente.getObraSocialId());
            repositorioPaciente.save(pacienteCreado);
        } else {
            throw new Exception("El paciente ya existe en el sistema");
        }
    }

    //Como es posible modificar el nro de afiliado y la obra social, se debe verificar nuevamente
    @Override
    public void modificarPaciente(Long cuil, Paciente pacienteActualizado) throws Exception {
        Paciente bsucarPaciente = buscarPaciente(cuil);
        boolean verificarNroAfiliado = servicioAPISalud.verificarNumeroAfiliado(pacienteActualizado.getObraSocialId(), pacienteActualizado.getNroAfiliado());
        boolean verificarObraSocial = servicioAPISalud.verificarObraSocial(pacienteActualizado.getObraSocialId());
        if (verificarObraSocial && verificarNroAfiliado){
            ingresarCambios(bsucarPaciente, pacienteActualizado);
            repositorioPaciente.save(bsucarPaciente);
        }else {
            throw new Exception("Los datos de la obra social no son correctos");
        }
    }

    private void ingresarCambios(Paciente pacienteExistente, Paciente pacienteActualizado) {
        pacienteExistente.setCuil(pacienteActualizado.getCuil());
        pacienteExistente.setDni(pacienteActualizado.getDni());
        pacienteExistente.setNombreCompleto(pacienteActualizado.getNombreCompleto());
        pacienteExistente.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
        pacienteExistente.setNumeroTelefono(pacienteActualizado.getNumeroTelefono());
        pacienteExistente.setEmail(pacienteActualizado.getEmail());
        pacienteExistente.setDireccion(pacienteActualizado.getDireccion());
        pacienteExistente.setLocalidad(pacienteActualizado.getLocalidad());
        pacienteExistente.setProvincia(pacienteActualizado.getProvincia());
        pacienteExistente.setPais(pacienteActualizado.getPais());
        pacienteExistente.setNroAfiliado(pacienteActualizado.getNroAfiliado());
        pacienteExistente.setObraSocialId(pacienteActualizado.getObraSocialId());
    }

    public Paciente buscarPaciente(Long cuil){
        Paciente pacienteExistente = repositorioPaciente.findById(cuil).orElseThrow(()-> new RuntimeException("El paciente no existe en el sistema"));
        return pacienteExistente;
    }

    //Solo al eliminar el paciente se pasa del estado "activo" a "suspendido"
    @Override
    public void eliminarPaciente(Long cuil) {
        Paciente buscarPaciente = buscarPaciente(cuil);
        buscarPaciente.setEstado(Estado.SUSPENDIDO);
        repositorioPaciente.save(buscarPaciente);
    }
}
