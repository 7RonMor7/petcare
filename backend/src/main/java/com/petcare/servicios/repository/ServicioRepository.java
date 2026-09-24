package com.petcare.servicios.repository;

import com.petcare.servicios.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByActivoTrueOrderByNombreAsc();

    Optional<Servicio> findByIdAndActivoTrue(Long id);
}
