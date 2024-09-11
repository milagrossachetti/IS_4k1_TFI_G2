package com.is.IS_4k1_TFI_G2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HistoriaClinica {
    @Id
    private Long id;
    private Date fechaCreacion;
    @OneToOne(mappedBy = "historiaClinica")
    private Paciente paciente;
}
