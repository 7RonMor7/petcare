package com.petcare.reservas.repository;

import com.petcare.reservas.domain.OcupacionFranja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcupacionFranjaRepository extends JpaRepository<OcupacionFranja, Long> {

    long countByReservaId(long reservaId);

    List<OcupacionFranja> findByReservaId(long reservaId);

    void deleteByReservaId(Long reservaId);
}
