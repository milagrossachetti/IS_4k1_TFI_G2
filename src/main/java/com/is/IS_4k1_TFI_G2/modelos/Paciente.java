package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Paciente {
    @Id
    private Long cuil;

    private String name;

    @OneToOne(mappedBy = "paciente")
    private HistoriaClinica historiaClinica;

}

