package com.is.IS_4k1_TFI_G2.modelo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {
    private Long cuil;
    private Long dni;
    private String nombreCompleto;
    private Long matricula; // Opcional para roles no médicos
    private String especialidad; // Opcional para roles no médicos
    private String email;
    private String contrasenia; // Necesario para autenticación
    private Long telefono;
    private String direccion;
    private String localidad;
    private String provincia;
    private String pais;
    private String rol; // Ejemplo: "MEDICO", "RECEPCIONISTA", "ADMIN"
    private boolean activo = true;

    // Constructor con validaciones
    public Usuario(Long cuil, Long dni, Long matricula, String especialidad, String nombreCompleto, String email,
                   String contrasenia, Long telefono, String pais, String localidad, String direccion, String provincia, String rol) {
        if (cuil == null || dni == null || nombreCompleto == null || email == null || contrasenia == null ||
                telefono == null || pais == null || localidad == null || direccion == null || provincia == null || rol == null) {
            throw new IllegalArgumentException("Los campos obligatorios no pueden ser nulos.");
        }

        this.cuil = cuil;
        this.dni = dni;
        this.matricula = matricula; // Puede ser null
        this.especialidad = especialidad; // Puede ser null
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.pais = pais;
        this.localidad = localidad;
        this.direccion = direccion;
        this.provincia = provincia;
        this.rol = rol;
        this.activo = true;
    }

    // Métodos adicionales de utilidad
    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }
}
