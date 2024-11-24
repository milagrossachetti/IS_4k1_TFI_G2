package com.is.IS_4k1_TFI_G2.modelo.listaDeDato;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TipoDiagnostico {
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    // Constructor
    public TipoDiagnostico(String nombre) {
        this.nombre = nombre;
    }

    // Lista estática que simula el repositorio de diagnósticos
    public static final List<TipoDiagnostico> DIAGNOSTICOS = new ArrayList<>();

    // Inicialización de datos
    static {
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre del dengue [Dengue clásico]"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre del dengue hemorrágico"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Gastroenteropatía aguda debida al agente de Norwalk"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Diarrea y gastroenteritis de presunto origen infeccioso"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Mucormicosis gastrointestinal"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Sinusitis esfenoidal aguda"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Sinusitis etmoidal aguda"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Sinusitis frontal aguda"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Sinusitis maxilar aguda"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre paratifoidea, no especificada"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre paratifoidea C"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre paratifoidea B"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre paratifoidea A"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fiebre tifoidea"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Fatiga por calor, transitoria"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Malestar y fatiga"));
        DIAGNOSTICOS.add(new TipoDiagnostico("Síndrome de fatiga postviral"));
    }

    // Método para buscar un diagnóstico por nombre
    public static List<TipoDiagnostico> buscarPorNombre(String nombre) {
        return DIAGNOSTICOS.stream()
                .filter(diagnostico -> diagnostico.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }
    // Método para obtener todos los diagnósticos registrados
    public static List<TipoDiagnostico> obtenerTodos() {
        return new ArrayList<>(DIAGNOSTICOS); // Devuelve una copia para proteger la lista original
    }

}
