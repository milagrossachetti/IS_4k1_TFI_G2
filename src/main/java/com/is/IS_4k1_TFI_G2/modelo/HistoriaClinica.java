package com.is.IS_4k1_TFI_G2.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date fechaCreacion;
    @OneToOne
    @JoinColumn(name = "paciente_cuil", referencedColumnName = "cuil")
    private Paciente paciente;

}
