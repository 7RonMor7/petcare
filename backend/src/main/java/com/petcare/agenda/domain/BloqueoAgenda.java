package com.petcare.agenda.domain;

import com.petcare.usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "bloqueo_agenda")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloqueoAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Usuario empleado;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private Instant fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private Instant fechaHoraFin;

    @Column(length = 200)
    private String motivo;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @PrePersist
    void alCrear() { creadoEn = Instant.now(); }
}
