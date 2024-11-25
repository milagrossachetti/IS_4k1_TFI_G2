package com.is.IS_4k1_TFI_G2.repositorio.memoria;

import com.is.IS_4k1_TFI_G2.modelo.Medicamento;
import com.is.IS_4k1_TFI_G2.modelo.MedicamentoRecetado;
import org.springframework.stereotype.Repository;
import com.is.IS_4k1_TFI_G2.repositorio.apiSalud.RepositorioFarmaco;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RepositorioFarmacoMemoria implements RepositorioFarmaco {

    private final List<Medicamento> medicamentos = new ArrayList<>();

    // Inicialización de datos
    public RepositorioFarmacoMemoria() {
        medicamentos.add(new Medicamento("Aspirina"));
        medicamentos.add(new Medicamento("Omeprazol"));
        medicamentos.add(new Medicamento("Lexotiroxina sódica"));
        medicamentos.add(new Medicamento("Ramipril"));
        medicamentos.add(new Medicamento("Amlodipina"));
        medicamentos.add(new Medicamento("Paracetamol"));
        medicamentos.add(new Medicamento("Atorvastatina"));
        medicamentos.add(new Medicamento("Salbutamol"));
        medicamentos.add(new Medicamento("Lansoprazol"));
        medicamentos.add(new Medicamento("Amoxicilina"));
        medicamentos.add(new Medicamento("Ibuprofeno"));
        medicamentos.add(new Medicamento("Sertal Compuesto"));
        medicamentos.add(new Medicamento("Sertal Perla"));
        medicamentos.add(new Medicamento("Buscapina"));
    }



    @Override
    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentos.stream()
                .filter(medicamento -> medicamento.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Verificar si un medicamento con el nombre exacto existe
    public boolean existeMedicamento(String nombre) {
        return medicamentos.stream()
                .anyMatch(medicamento -> medicamento.getNombre().equalsIgnoreCase(nombre));
    }
}
