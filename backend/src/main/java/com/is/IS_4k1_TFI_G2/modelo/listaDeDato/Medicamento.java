package com.is.IS_4k1_TFI_G2.modelo.listaDeDato;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Medicamento {
    private String nombre;

    // Constructor
    public Medicamento(String nombre) {
        this.nombre = nombre;
    }

    // Lista estática que simula el repositorio de medicamentos
    public static final List<Medicamento> MEDICAMENTOS = new ArrayList<>();

    // Inicialización de datos
    static {
        MEDICAMENTOS.add(new Medicamento("Aspirina"));
        MEDICAMENTOS.add(new Medicamento("Omeprazol"));
        MEDICAMENTOS.add(new Medicamento("Lexotiroxina sódica"));
        MEDICAMENTOS.add(new Medicamento("Ramipril"));
        MEDICAMENTOS.add(new Medicamento("Amlodipina"));
        MEDICAMENTOS.add(new Medicamento("Paracetamol"));
        MEDICAMENTOS.add(new Medicamento("Atorvastatina"));
        MEDICAMENTOS.add(new Medicamento("Salbutamol"));
        MEDICAMENTOS.add(new Medicamento("Lansoprazol"));
        MEDICAMENTOS.add(new Medicamento("Amoxicilina"));
        MEDICAMENTOS.add(new Medicamento("Ibuprofeno"));
        MEDICAMENTOS.add(new Medicamento("Sertal Compuesto"));
        MEDICAMENTOS.add(new Medicamento("Sertal Perla"));
        MEDICAMENTOS.add(new Medicamento("Buscapina"));
    }

    // Devolver una lista de nombres de medicamentos
    public static List<String> buscarPorNombre(String nombre) {
        return MEDICAMENTOS.stream()
                .filter(medicamento -> medicamento.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .map(Medicamento::getNombre) // Convertir a nombres
                .toList();
    }

    // Verifica si un medicamento con el nombre exacto existe
    public static boolean existeMedicamento(String nombre) {
        return MEDICAMENTOS.stream()
                .anyMatch(medicamento -> medicamento.getNombre().equalsIgnoreCase(nombre));
    }


}
