package com.petcare.mascotas.domain;

import com.petcare.usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "mascota")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 10)
    private Especie especie;

    @Column(length = 60)
    private String raza;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 10)
    private Sexo sexo;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "peso_kg", nullable = false, precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    public Mascota(Usuario cliente, String nombre, Especie especie, String raza,
                   Sexo sexo, LocalDate fechaNacimiento, BigDecimal pesoKg,
                   String observaciones) {
        this.cliente = cliente;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.pesoKg = pesoKg;
        this.observaciones = observaciones;
    }

    @PrePersist
    void alCrear() {
        creadoEn = actualizadoEn = Instant.now();
    }

    @PreUpdate
    void alActualizar() {
        actualizadoEn = Instant.now();
    }

    public void actualizarDatos(String nombre, Especie especie, String raza, Sexo sexo,
                                LocalDate fechaNacimiento, BigDecimal pesoKg, String observaciones) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.pesoKg = pesoKg;
        this.observaciones = observaciones;
    }

    public void desactivar() {
        this.activo = false;
    }
}
