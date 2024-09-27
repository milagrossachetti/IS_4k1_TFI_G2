package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository

public interface RepositorioPaciente extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByCuil(Long cuil);
}
