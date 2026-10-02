package com.petcare.reservas.domain;

import com.petcare.mascotas.domain.Mascota;
import com.petcare.usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "ocupacion_franja")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OcupacionFranja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Usuario empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(name = "inicio_franja", nullable = false)
    private Instant inicioFranja;

    public OcupacionFranja(Reserva reserva, Instant inicioFranja) {
        this.reserva = reserva;
        this.empleado = reserva.getEmpleado();
        this.mascota = reserva.getMascota();
        this.inicioFranja = inicioFranja;
    }
}
