    package com.is.IS_4k1_TFI_G2.modelos;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.util.List;

    @Entity
    @Getter
    @Setter

    public class HistoriaClinica {
        @Id
        @GeneratedValue

        private Long Id;
        private LocalDate fecha;

        @OneToOne
        @JoinColumn(name= "paciente_cuil")
        private Paciente paciente;

        @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Diagnostico> diagnosticos;

        public HistoriaClinica(){
        }

        public HistoriaClinica(Paciente paciente){
            this.paciente= paciente;
        }

        public void agregarDiagnostico(Diagnostico diagnostico){
            this.diagnosticos.add(diagnostico);
            diagnostico.setHistoriaClinica(this);
        }
    }
