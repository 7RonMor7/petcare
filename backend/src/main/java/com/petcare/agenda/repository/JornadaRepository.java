package com.petcare.agenda.repository;

import com.petcare.agenda.domain.JornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface JornadaRepository extends JpaRepository<JornadaLaboral, Long> {

    List<JornadaLaboral> findByEmpleadoIdOrderByDiaSemanaAscHoraInicioAsc(Long empleadoId);

    List<JornadaLaboral> findByEmpleadoIdInAndDiaSemana(List<Long> empleadoIds, DayOfWeek diaSemana);

    void deleteByEmpleadoId(Long empleadoId);
}
