package com.petcare.servicios.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table( name = "servicio")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(length = 400)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "unidad_cobro", nullable = false, length = 20)
    private UnidadCobro unidadCobro;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    @PrePersist
    void alCrear() { creadoEn = actualizadoEn = Instant.now(); }

    @PreUpdate
    void alActualizar() { actualizadoEn = Instant.now(); }
}
