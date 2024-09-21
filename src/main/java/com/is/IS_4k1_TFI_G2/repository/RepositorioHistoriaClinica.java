package com.is.IS_4k1_TFI_G2.repository;

import com.is.IS_4k1_TFI_G2.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioHistoriaClinica extends  JpaRepository<HistoriaClinica, Long>{
    boolean existsByPacienteCuil(Long cuil);
}
