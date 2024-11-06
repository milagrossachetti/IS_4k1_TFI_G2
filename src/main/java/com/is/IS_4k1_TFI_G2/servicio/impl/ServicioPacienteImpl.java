package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.DTOs.PacienteDTO;
import com.is.IS_4k1_TFI_G2.excepcion.*;
import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import com.is.IS_4k1_TFI_G2.servicio.ServicioAPISalud;
import com.is.IS_4k1_TFI_G2.servicio.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.is.IS_4k1_TFI_G2.modelo.Estado.SUSPENDIDO;

@Service
public class ServicioPacienteImpl implements ServicioPaciente {
    @Autowired
    RepositorioPaciente repositorioPaciente;
    @Autowired
    ServicioAPISalud servicioAPISalud;

    //al crear el paciente se crea automáticamente con el estado "activo"
    @Override
    public Paciente crearPaciente(Paciente paciente) throws Exception {
        Optional<Paciente> existePaciente = repositorioPaciente.findById(paciente.getCuil());
        verificarDatosObraSocial(paciente.getObraSocialId(), paciente.getNroAfiliado());
        if (existePaciente.isEmpty()){
            Paciente pacienteCreado = new Paciente(paciente.getCuil(), paciente.getDni(),
                    paciente.getNombreCompleto(), paciente.getFechaNacimiento(),
                    paciente.getNumeroTelefono(), paciente.getEmail(),
                    paciente.getDireccion(), paciente.getLocalidad(), paciente.getProvincia(),
                    paciente.getPais(), paciente.getNroAfiliado(), paciente.getObraSocialId());
            repositorioPaciente.save(pacienteCreado);
            return pacienteCreado;
        } else {
            throw new ElPacienteYaExisteExcepcion("El paciente ya existe en el sistema");
        }
    }

    //Como es posible modificar el nro de afiliado y la obra social, se debe verificar nuevamente
    @Override
    public Paciente modificarPaciente(Long cuil, PacienteDTO pacienteActualizadoDTO) throws Exception {
        Paciente pacienteEncontrado = buscarPaciente(cuil);
        if (pacienteEncontrado.getEstado().equals(SUSPENDIDO)){
            throw new ElPacienteEstaSuspendidoExcepcion("No se puede modificar porque el paciente está SUSPENDIDO");
        }
        verificarDatosObraSocial(pacienteActualizadoDTO.getObraSocialId(), pacienteActualizadoDTO.getNroAfiliado());
        pacienteEncontrado.modificarPaciente(pacienteActualizadoDTO.getNombreCompleto(), pacienteActualizadoDTO.getFechaNacimiento(),
                pacienteActualizadoDTO.getNumeroTelefono(), pacienteActualizadoDTO.getEmail(), pacienteActualizadoDTO.getDireccion(), pacienteActualizadoDTO.getLocalidad(),
                pacienteActualizadoDTO.getProvincia(), pacienteActualizadoDTO.getPais(), pacienteActualizadoDTO.getNroAfiliado(), pacienteActualizadoDTO.getObraSocialId());
        return repositorioPaciente.save(pacienteEncontrado);
    }

    //Solo al eliminar el paciente se pasa del estado "activo" a "suspendido"
    @Override
    public Paciente eliminarPaciente(Long cuil) {
        Paciente pacienteEncontrado = buscarPaciente(cuil);
        pacienteEncontrado.bajaPaciente();
        return repositorioPaciente.save(pacienteEncontrado);
    }

    public Paciente buscarPaciente(Long cuil){
        return repositorioPaciente.findById(cuil).orElseThrow(()-> new ElPacienteNoExisteExcepcion("El paciente no existe en el sistema"));
    }

    @Override
    public List<Paciente> obtenerPacientes() {
        return repositorioPaciente.findAll();
    }


    public boolean verificarDatosObraSocial(Long obraSocialId, String nroAfiliado) {
        boolean verificarNroAfiliado = servicioAPISalud.verificarNumeroAfiliado(obraSocialId, nroAfiliado);
        boolean verificarObraSocial = servicioAPISalud.verificarObraSocial(obraSocialId);
        boolean esCorrecto = false;
        if (verificarObraSocial && verificarNroAfiliado){
            esCorrecto = true;
        } else if (!verificarObraSocial){
            throw new ObraSocialIncorrectaExcepcion("La obra social ingresada no es correcta");
        } else if (!verificarNroAfiliado){
            throw new NroAfiliadoIncorrectoExcepcion("El numero de afiliado ingresado no es correcto o no existe en la obra social");
        };
        return esCorrecto;
    }
}