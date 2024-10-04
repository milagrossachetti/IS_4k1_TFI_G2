package com.is.IS_4k1_TFI_G2.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Paciente {
    @Id
    private Long cuil;
    private Long dni;
    private String nombreCompleto;
    private Date fechaNacimiento;
    private String numeroTelefono;
    private String email;
    private String direccion;
    private String localidad;
    private String provincia;
    private String pais;
    private String nroAfiliado;
    @Enumerated(value = EnumType.STRING)
    private Estado estado;
    private Long obraSocialId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "historia_clinica_id")
    private HistoriaClinica historiaClinica;


    public Paciente(Long cuil, Long dni, String nombreCompleto, Date fechaNacimiento, String numeroTelefono, String email, String direccion, String localidad, String provincia, String pais, String nroAfiliado, Long obraSocialId) {
        this.cuil = cuil;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.nroAfiliado = nroAfiliado;
        this.obraSocialId = obraSocialId;
        this.estado = Estado.ACTIVO;
    }

    public void modificarPaciente(Long dni, String nombreCompleto, Date fechaNacimiento, String numeroTelefono, String email, String direccion, String localidad, String provincia, String pais, String nroAfiliado, Long obraSocialId){
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.nroAfiliado = nroAfiliado;
        this.obraSocialId = obraSocialId;
    }

    public void bajaPaciente(){
        this.estado = Estado.SUSPENDIDO;
    }
}
