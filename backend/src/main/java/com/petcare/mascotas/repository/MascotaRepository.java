package com.petcare.mascotas.repository;

import com.petcare.mascotas.domain.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
}
