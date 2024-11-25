package com.is.IS_4k1_TFI_G2.repositorio.memoria;

import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;
import org.springframework.stereotype.Repository;
import com.is.IS_4k1_TFI_G2.repositorio.apiSalud.RepositorioCieDiez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RepositorioCieDiezMemoria implements RepositorioCieDiez {

    private final List<Diagnostico> diagnosticos = new ArrayList<>();

    // Inicialización de datos
    public RepositorioCieDiezMemoria() {
        diagnosticos.add(new Diagnostico("Fiebre del dengue [Dengue clásico]"));
        diagnosticos.add(new Diagnostico("Fiebre del dengue hemorrágico"));
        diagnosticos.add(new Diagnostico("Gastroenteropatía aguda debida al agente de Norwalk"));
        diagnosticos.add(new Diagnostico("Diarrea y gastroenteritis de presunto origen infeccioso"));
        diagnosticos.add(new Diagnostico("Mucormicosis gastrointestinal"));
        diagnosticos.add(new Diagnostico("Sinusitis esfenoidal aguda"));
        diagnosticos.add(new Diagnostico("Sinusitis etmoidal aguda"));
        diagnosticos.add(new Diagnostico("Sinusitis frontal aguda"));
        diagnosticos.add(new Diagnostico("Sinusitis maxilar aguda"));
        diagnosticos.add(new Diagnostico("Fiebre paratifoidea, no especificada"));
        diagnosticos.add(new Diagnostico("Fiebre paratifoidea C"));
        diagnosticos.add(new Diagnostico("Fiebre paratifoidea B"));
        diagnosticos.add(new Diagnostico("Fiebre paratifoidea A"));
        diagnosticos.add(new Diagnostico("Fiebre tifoidea"));
        diagnosticos.add(new Diagnostico("Fatiga por calor, transitoria"));
        diagnosticos.add(new Diagnostico("Malestar y fatiga"));
        diagnosticos.add(new Diagnostico("Síndrome de fatiga postviral"));
    }

    @Override
    public List<Diagnostico> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of(); // Devuelve una lista vacía si el nombre es nulo o vacío
        }
        return diagnosticos.stream()
                .filter(diagnostico -> diagnostico.getNombreDiagnostico() != null &&
                        diagnostico.getNombreDiagnostico().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Diagnostico> obtenerTodos() {
        return new ArrayList<>(diagnosticos); // Devuelve una copia para proteger los datos originales
    }
}
