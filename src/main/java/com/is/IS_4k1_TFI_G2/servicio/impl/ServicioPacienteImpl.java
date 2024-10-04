package com.is.IS_4k1_TFI_G2.servicio.impl;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import com.is.IS_4k1_TFI_G2.servicio.ServicioAPISalud;
import com.is.IS_4k1_TFI_G2.servicio.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void crearPaciente(Paciente paciente) throws Exception {
        Optional<Paciente> existePaciente = repositorioPaciente.findById(paciente.getCuil());
        verificarDatosObraSocial(paciente.getObraSocialId(), paciente.getNroAfiliado());
        if (existePaciente.isEmpty()){
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
        Paciente pacienteEncontrado = buscarPaciente(cuil);
        if (pacienteEncontrado.getEstado().equals(SUSPENDIDO)){
            throw new Exception("No se puede modificar porque el paciente está SUSPENDIDO");
        }
        verificarDatosObraSocial(pacienteActualizado.getObraSocialId(), pacienteActualizado.getNroAfiliado());
        pacienteEncontrado.modificarPaciente(pacienteActualizado.getDni(), pacienteActualizado.getNombreCompleto(), pacienteActualizado.getFechaNacimiento(),
                pacienteActualizado.getNumeroTelefono(), pacienteActualizado.getEmail(), pacienteActualizado.getDireccion(), pacienteActualizado.getLocalidad(),
                pacienteActualizado.getProvincia(), pacienteActualizado.getPais(), pacienteActualizado.getNroAfiliado(), pacienteActualizado.getObraSocialId());
        repositorioPaciente.save(pacienteEncontrado);
    }

    //Solo al eliminar el paciente se pasa del estado "activo" a "suspendido"
    @Override
    public void eliminarPaciente(Long cuil) {
        Paciente pacienteEncontrado = buscarPaciente(cuil);
        pacienteEncontrado.bajaPaciente();
        repositorioPaciente.save(pacienteEncontrado);
    }

    public Paciente buscarPaciente(Long cuil){
        return repositorioPaciente.findById(cuil).orElseThrow(()-> new RuntimeException("El paciente no existe en el sistema"));
    }


    public boolean verificarDatosObraSocial(Long obraSocialId, String nroAfiliado) throws Exception {
        boolean verificarNroAfiliado = servicioAPISalud.verificarNumeroAfiliado(obraSocialId, nroAfiliado);
        boolean verificarObraSocial = servicioAPISalud.verificarObraSocial(obraSocialId);
        boolean esCorrecto = false;
        if (verificarObraSocial && verificarNroAfiliado){
            esCorrecto = true;
        } else throw new Exception("Los datos de la obra social no son correctos");
        return esCorrecto;
    }
}