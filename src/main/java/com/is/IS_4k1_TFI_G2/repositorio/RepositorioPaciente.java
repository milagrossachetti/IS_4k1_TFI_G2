package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioPaciente extends JpaRepository<Paciente, Long> {
}
