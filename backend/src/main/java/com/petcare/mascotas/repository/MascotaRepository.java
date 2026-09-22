package com.petcare.mascotas.repository;

import com.petcare.mascotas.domain.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByClienteIdAndActivoTrueOrderByNombreAsc(Long clienteId);

    Optional<Mascota> findByIdAndClienteIdAndActivoTrue(Long id, Long clienteId);
}
