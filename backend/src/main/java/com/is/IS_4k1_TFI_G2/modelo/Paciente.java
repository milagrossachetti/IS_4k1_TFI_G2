package com.is.IS_4k1_TFI_G2.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Paciente {
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
    private Estado estado;
    private Long obraSocialId;
    private HistoriaClinica historiaClinica; // Se gestiona manualmente, no por JPA.

    public Paciente() {}

    public Paciente(Long cuil, Long dni, String nombreCompleto, Date fechaNacimiento, String numeroTelefono,
                    String email, String direccion, String localidad, String provincia, String pais,
                    String nroAfiliado, Long obraSocialId) {
        if (cuil == null || nombreCompleto == null || fechaNacimiento == null) {
            throw new IllegalArgumentException("CUIL, nombre completo y fecha de nacimiento son obligatorios.");
        }
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

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }
}
