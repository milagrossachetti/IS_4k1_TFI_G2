package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicamentoRecetado {

    private String nombre;

    public MedicamentoRecetado() {}

    public MedicamentoRecetado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
