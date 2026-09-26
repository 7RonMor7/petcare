package com.petcare.agenda.domain;

import com.petcare.servicios.domain.Servicio;
import com.petcare.usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "empleado_servicio")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmpleadoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Usuario empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    public EmpleadoServicio(Usuario empleado, Servicio servicio) {
        this.empleado = empleado;
        this.servicio = servicio;
    }

    @PrePersist
    void alCrear() { creadoEn = Instant.now(); }
}
