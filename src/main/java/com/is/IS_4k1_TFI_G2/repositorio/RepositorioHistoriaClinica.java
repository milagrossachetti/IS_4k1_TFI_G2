package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioHistoriaClinica extends  JpaRepository<HistoriaClinica, Long>{
    boolean existsByPacienteCuil(Long cuil);
}
