package com.petcare.reservas.domain;

import com.petcare.common.error.ReglaNegocioException;
import com.petcare.mascotas.domain.Mascota;
import com.petcare.servicios.domain.Servicio;
import com.petcare.usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "reserva")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Usuario empleado;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private Instant fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private Instant fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    public Reserva(Usuario cliente, Mascota mascota, Servicio servicio, Usuario empleado,
                   Instant fechaHoraInicio, Instant fechaHoraFin, BigDecimal total) {
        this.cliente = cliente;
        this.mascota = mascota;
        this.servicio = servicio;
        this.empleado = empleado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.total = total;
        this.estado = EstadoReserva.PENDIENTE_PAGO;
    }

    public void cambiarEstado(EstadoReserva destino) {
        if (!estado.puedePasarA(destino)) {
            throw new ReglaNegocioException("TRANSICION_INVALIDA",
                    "Una reserva " + estado + " no puede pasar a " + destino);
        }
        this.estado = destino;
    }

    @PrePersist
    void alCrear() { creadoEn = actualizadoEn = Instant.now(); }

    @PreUpdate
    void alActualizar() { actualizadoEn = Instant.now(); }
}
