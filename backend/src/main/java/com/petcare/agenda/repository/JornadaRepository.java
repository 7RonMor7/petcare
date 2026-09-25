package com.petcare.agenda.repository;

import com.petcare.agenda.domain.JornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JornadaRepository extends JpaRepository<JornadaLaboral, Long> {

    List<JornadaLaboral> findByEmpleadoIdOrderByDiaSemanaAscHoraInicioAsc(Long empleadoId);

    void deleteByEmpleadoId(Long empleadoId);
}
