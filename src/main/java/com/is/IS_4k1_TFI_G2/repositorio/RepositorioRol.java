package com.is.IS_4k1_TFI_G2.repositorio;

import com.is.IS_4k1_TFI_G2.modelo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioRol extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
