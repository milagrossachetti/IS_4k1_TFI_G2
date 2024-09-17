package com.is.IS_4k1_TFI_G2.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter

public class Medico {
    @Id
    @GeneratedValue

    private Long Id;
    private Long cuil;
    private Long dni;
    private String Nombre;
    private String Apellido;
    private LocalDate fecNac;
    private String telefono;
    private String email;
    private String localidad;
    private String pais;
    private String provincia;

    //@ManyToOne
    //private ObraSocial obraSocial;
}
