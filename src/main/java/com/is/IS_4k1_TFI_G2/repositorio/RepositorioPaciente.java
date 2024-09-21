package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RepositorioPaciente extends JpaRepository<Paciente, Long> {
    Paciente findByCuil(Long cuil);
}
