package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelos.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioHistorialClinico extends JpaRepository<HistorialClinico, Long> {

    Optional<HistorialClinico> findByPacienteId (Long pacienteId);
}
