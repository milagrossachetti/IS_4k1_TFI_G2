package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Diagnostico;
import com.is.IS_4k1_TFI_G2.modelo.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioDiagnostico extends JpaRepository<Diagnostico, Long> {

    Optional<Diagnostico> findByNombreAndHistoriaClinicaId(String nombre, Long historiaClinicaId);

    List<Diagnostico> findByHistoriaClinica(HistoriaClinica historiaClinica);

    boolean existsByNombreAndHistoriaClinicaId(String nombre, Long historiaClinicaId);
}
