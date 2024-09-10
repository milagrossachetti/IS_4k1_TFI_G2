package com.is.IS_4k1_TFI_G2.repository;

import com.is.IS_4k1_TFI_G2.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCuil(Long cuil);
}
