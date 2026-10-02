package com.petcare.reservas.repository;

import com.petcare.reservas.domain.OcupacionFranja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcupacionFranjaRepository extends JpaRepository<OcupacionFranja, Long> {
    void deleteByReservaId(Long reservaId);
}
