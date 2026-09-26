package com.petcare.agenda.repository;

import com.petcare.agenda.domain.EmpleadoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoServicioRepository extends JpaRepository<EmpleadoServicio, Long> {

    List<EmpleadoServicio> findByEmpleadoIdOrderByServicioNombreAsc(Long empleadoId);

    List<EmpleadoServicio> findByServicioIdAndEmpleadoActivoTrue(Long servicioId);

    void deleteByEmpleadoId(Long empleadoId);
}
