package com.is.IS_4k1_TFI_G2.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Usuario {

    @Id
    private Long cuil;

    @Column(nullable = false)
    private Long dni;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private Long matricula;

    @Column(nullable = true)
    private String especialidad;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long telefono;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String localidad;

    @Column(nullable = false)
    private String provincia;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    @NotNull
    private String rol;

    private boolean activo;

    public Usuario(Long cuil, Long dni, Long matricula, String especialidad, String nombreCompleto, String email, Long telefono, String pais, String localidad, String direccion, String provincia, String rol) {
        this.cuil = cuil;
        this.dni = dni;
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.pais = pais;
        this.localidad = localidad;
        this.direccion = direccion;
        this.provincia = provincia;
        this.rol = rol;
        this.activo = true;
    }

}

