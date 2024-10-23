package com.is.IS_4k1_TFI_G2.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class RecetaDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReceta;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Usuario medico;
    private LocalDateTime fechayHora;
    private String obraSocial;
    private String numeroAfiliado;
    private String medicamentoComercial;
    private String medicamentoGenerico;
    private Diagnostico diagnostico;
    private String firmaElectronicaMedico;
    private String logoEmpresa;
    private String pieDePagina;
    private String qrCode; // Link de descarga

    private boolean activo; // activa o anulada (por defect activa)

    public RecetaDigital(Long idReceta, Paciente paciente, Usuario medico, LocalDateTime fechayHora, String obraSocial, String numeroAfiliado, String medicamentoComercial, String medicamentoGenerico, Diagnostico diagnostico, String firmaElectronicaMedico, String logoEmpresa, String pieDePagina, String qrCode, boolean activo) {
        this.idReceta = idReceta;
        this.paciente = paciente;
        this.medico = medico;
        this.fechayHora = fechayHora;
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
        this.medicamentoComercial = medicamentoComercial;
        this.medicamentoGenerico = medicamentoGenerico;
        this.diagnostico = diagnostico;
        this.firmaElectronicaMedico = firmaElectronicaMedico;
        this.logoEmpresa = logoEmpresa;
        this.pieDePagina = pieDePagina;
        this.qrCode = qrCode;
        this.activo = activo;
    }
}
