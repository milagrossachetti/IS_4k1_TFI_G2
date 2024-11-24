package com.is.IS_4k1_TFI_G2.modelo.listaDeDato;

import com.is.IS_4k1_TFI_G2.modelo.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PacienteDato {

    // Lista estática que simula el repositorio de pacientes
    public static final List<Paciente> PACIENTES = new ArrayList<>();

    // Inicialización de datos
    static {
        // Usamos un médico de UsuarioDato directamente
        Usuario medico = UsuarioDato.USUARIOS.get(0); // Dr. Juan Pérez

        // Crear instancia de historia clínica con lista de diagnósticos vacía
        HistoriaClinica historia1 = new HistoriaClinica();
        historia1.setId(1L);
        historia1.setDiagnosticos(new ArrayList<>());

        // Diagnóstico 1
        Diagnostico diagnostico1 = new Diagnostico();
        diagnostico1.setId(1L);
        diagnostico1.setNombreDiagnostico("Fiebre tifoidea");

        // Evoluciones con médico asignado
        Evolucion evolucion1 = new Evolucion(
                "Paciente presenta fiebre persistente.",
                LocalDateTime.now(),
                medico, // Médico asignado directamente
                new PlantillaControl(65.5, 1.70, "120/80", 80, 98, null),
                null, // Plantilla de laboratorio
                new ArrayList<>(), // Recetas
                null // Ruta del PDF
        );
        diagnostico1.getEvoluciones().add(evolucion1);

        Evolucion evolucion2 = new Evolucion(
                "Fiebre disminuye, pero continúa malestar general.",
                LocalDateTime.now(),
                medico, // Médico asignado directamente
                null, // Plantilla de control
                null, // Plantilla de laboratorio
                Arrays.asList(new Receta(
                        LocalDateTime.now(),
                        medico, // Médico asignado directamente
                        Arrays.asList(
                                crearMedicamentoRecetado("Paracetamol"),
                                crearMedicamentoRecetado("Ibuprofeno")
                        ),
                        evolucion1,
                        null // Ruta del PDF
                )),
                null // Ruta del PDF
        );
        diagnostico1.getEvoluciones().add(evolucion2);

        historia1.getDiagnosticos().add(diagnostico1);

        // Diagnóstico 2
        Diagnostico diagnostico2 = new Diagnostico();
        diagnostico2.setId(2L);
        diagnostico2.setNombreDiagnostico("Sinusitis frontal aguda");

        // Evolución con médico asignado
        Evolucion evolucion3 = new Evolucion(
                "Paciente con congestión severa y dolor frontal.",
                LocalDateTime.now(),
                medico, // Médico asignado directamente
                new PlantillaControl(68.0, 1.75, "130/85", 85, 96, null),
                null, // Plantilla de laboratorio
                new ArrayList<>(), // Inicializar recetas vacías
                null // Ruta del PDF
        );

        // Crear la receta y asociarla a evolucion3
        Receta receta = new Receta(
                LocalDateTime.now(),
                medico, // Médico asignado directamente
                Arrays.asList(
                        crearMedicamentoRecetado("Amoxicilina"),
                        crearMedicamentoRecetado("Aspirina")
                ),
                evolucion3,
                null // Ruta del PDF
        );

        // Añadir la receta a la lista de recetas de evolucion3
        evolucion3.getRecetas().add(receta);

        // Añadir la evolución al diagnóstico
        diagnostico2.getEvoluciones().add(evolucion3);

        historia1.getDiagnosticos().add(diagnostico2);

        // Crear primer paciente con esta historia clínica
        Paciente paciente1 = new Paciente(
                20304050607L,
                12345678L,
                "Brenda Marinelli",
                new Date(93, 4, 15), // Fecha de nacimiento
                "3816404000", // Teléfono como String
                "marinellibrendaluciana@gmail.com",
                "Esteban Echeverria 2200",
                "San Miguel de Tucuman",
                "Tucuman",
                "Argentina",
                "AF123456",
                1L // ID de la historia clínica
        );
        paciente1.setHistoriaClinica(historia1);
        PACIENTES.add(paciente1);

        // Paciente 2 con historia clínica vacía
        HistoriaClinica historia2 = new HistoriaClinica();
        historia2.setId(2L);
        historia2.setDiagnosticos(new ArrayList<>());

        Paciente paciente2 = new Paciente(
                30405060708L,
                87654321L,
                "Pia Romano",
                new Date(114, 8, 4), // Año ajustado correctamente
                "3816006090", // Teléfono como String
                "romanopia@gmail.com",
                "Calle 2 1182",
                "San Miguel de Tucuman",
                "Tucuman",
                "Argentina",
                "AF987654",
                2L // ID de la historia clínica
        );
        paciente2.setHistoriaClinica(historia2);
        PACIENTES.add(paciente2);
    }

    // Método para buscar un paciente por CUIL
    public static Paciente buscarPorCuil(Long cuil) {
        return PACIENTES.stream()
                .filter(paciente -> paciente.getCuil().equals(cuil))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con CUIL: " + cuil));
    }

    public static void actualizarPaciente(Paciente pacienteActualizado) {
        for (int i = 0; i < PACIENTES.size(); i++) {
            if (PACIENTES.get(i).getCuil().equals(pacienteActualizado.getCuil())) {
                PACIENTES.set(i, pacienteActualizado);
                return;
            }
        }
        throw new RuntimeException("Paciente no encontrado para actualizar.");
    }

    // Método para crear un medicamento recetado validando con Medicamento.MEDICAMENTOS
    private static MedicamentoRecetado crearMedicamentoRecetado(String nombre) {
        return Medicamento.MEDICAMENTOS.stream()
                .filter(medicamento -> medicamento.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .map(medicamento -> new MedicamentoRecetado(medicamento.getNombre()))
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no válido: " + nombre));
    }
}
