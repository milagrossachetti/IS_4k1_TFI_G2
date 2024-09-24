package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelos.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioHistoriaClinica extends JpaRepository<HistoriaClinica, Long> {

    Optional<HistoriaClinica> findByPacienteCuil (Long pacienteCuil);
}
