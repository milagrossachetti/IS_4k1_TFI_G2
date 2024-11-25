package com.is.IS_4k1_TFI_G2.repositorio.memoria;

import com.is.IS_4k1_TFI_G2.modelo.*;
import com.is.IS_4k1_TFI_G2.repositorio.RepositorioPaciente;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class RepositorioPacienteMemoria implements RepositorioPaciente {

    private final List<Paciente> pacientes = new ArrayList<>();
    private final List<Medicamento> medicamentos = new ArrayList<>();

    // Constructor sin dependencia de RepositorioUsuario
    public RepositorioPacienteMemoria() {
        inicializarDatos();
    }

    private void inicializarDatos() {
        // Crear usuarios directamente
        Usuario medico1 = new Usuario(
                20123456789L, 12345678L, 12345L, "Cardiología", "Dr. Juan Pérez",
                "juan.perez@hospital.com", "password123", 1145678901L, "Argentina",
                "CABA", "Av. Siempre Viva 123", "Buenos Aires", "MEDICO"
        );

        Usuario medico2 = new Usuario(
                20456789012L, 11223344L, 67890L, "Pediatría", "Dr. Pedro Martínez",
                "pedro.martinez@hospital.com", "password789", 1143210987L, "Argentina",
                "Rosario", "San Martín 789", "Santa Fe", "MEDICO"
        );

        // Crear Historia Clínica y Diagnósticos
        HistoriaClinica historia1 = new HistoriaClinica();
        historia1.setId(1L);

        Diagnostico diagnostico1 = new Diagnostico("Fiebre tifoidea", medico1);
        Evolucion evolucion1 = new Evolucion(
                "Paciente presenta fiebre persistente.",
                LocalDateTime.now(),
                medico1, // Mismo médico del diagnóstico
                new PlantillaControl(65.5, 1.70, "120/80", 80, 98, null),
                null,
                new ArrayList<>(),
                null
        );
        diagnostico1.agregarEvolucion(evolucion1);

        Evolucion evolucion2 = new Evolucion(
                "Fiebre disminuye, pero continúa malestar general.",
                LocalDateTime.now(),
                medico2, // Otro médico
                null,
                null,
                Arrays.asList(new Receta(
                        LocalDateTime.now(),
                        medico2,
                        Arrays.asList(
                                new MedicamentoRecetado("Paracetamol"),
                                new MedicamentoRecetado("Ibuprofeno")
                        ),
                        evolucion1,
                        null
                )),
                null
        );
        diagnostico1.agregarEvolucion(evolucion2);

        historia1.getDiagnosticos().add(diagnostico1);

        Diagnostico diagnostico2 = new Diagnostico("Sinusitis frontal aguda", medico2);
        Evolucion evolucion3 = new Evolucion(
                "Paciente con congestión severa y dolor frontal.",
                LocalDateTime.now(),
                medico2, // Mismo médico del diagnóstico
                new PlantillaControl(68.0, 1.75, "130/85", 85, 96, null),
                null,
                new ArrayList<>(),
                null
        );
        Receta receta = new Receta(
                LocalDateTime.now(),
                medico2,
                Arrays.asList(
                        new MedicamentoRecetado("Amoxicilina"),
                        new MedicamentoRecetado("Aspirina")
                ),
                evolucion3,
                null
        );
        evolucion3.getRecetas().add(receta);
        diagnostico2.agregarEvolucion(evolucion3);

        historia1.getDiagnosticos().add(diagnostico2);

        // Crear paciente con la historia clínica
        Paciente paciente1 = new Paciente(
                20304050607L,
                12345678L,
                "Brenda Marinelli",
                new Date(93, 4, 15),
                "3816404000",
                "marinellibrendaluciana@gmail.com",
                "Esteban Echeverria 2200",
                "San Miguel de Tucuman",
                "Tucuman",
                "Argentina",
                "AF123456",
                1L
        );
        paciente1.setHistoriaClinica(historia1);
        pacientes.add(paciente1);
    }

    @Override
    public Optional<Paciente> buscarPorCuil(Long cuil) {
        return pacientes.stream()
                .filter(paciente -> paciente.getCuil().equals(cuil))
                .findFirst();
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        buscarPorCuil(paciente.getCuil())
                .ifPresentOrElse(
                        existente -> {
                            int index = pacientes.indexOf(existente);
                            pacientes.set(index, paciente);
                        },
                        () -> pacientes.add(paciente)
                );
    }
}
