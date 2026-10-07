package com.petcare.pagos.domain;

import com.petcare.common.error.ReglaNegocioException;
import com.petcare.reservas.domain.Reserva;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orden_pago")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdenPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private EstadoOrdenPago estado;

    @Column(name = "referencia_externa", length = 100)
    private String referenciaExterna;

    @Column(name = "url_checkout", length = 500)
    private String urlCheckout;

    @Column(length = 30)
    private String proveedor;

    @Column(name = "expira_en", nullable = false)
    private Instant expiraEn;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    public OrdenPago(Reserva reserva, Instant expiraEn) {
        this.reserva = reserva;
        this.monto = reserva.getTotal();        // el monto sale de la reserva, no del catálogo
        this.moneda = "COP";
        this.estado = EstadoOrdenPago.PENDIENTE;
        this.expiraEn = expiraEn;
    }

    public void cambiarEstado(EstadoOrdenPago destino) {
        if (!estado.puedePasarA(destino)) {
            throw new ReglaNegocioException("TRANSICION_INVALIDA",
                    "Una orden " + estado + " no puede pasar a " + destino);
        }
        this.estado = destino;
    }

    public void registrarEnPasarela(String proveedor, String referenciaExterna, String urlCheckout) {
        this.proveedor = proveedor;
        this.referenciaExterna = referenciaExterna;
        this.urlCheckout = urlCheckout;
    }

    public boolean tieneCheckoutAbierto() {
        return referenciaExterna != null && urlCheckout != null;
    }

    public boolean estaVencida(Instant ahora) {
        return estado == EstadoOrdenPago.PENDIENTE && ahora.isAfter(expiraEn);
    }

    @PrePersist
    void alCrear() { creadoEn = actualizadoEn = Instant.now(); }

    @PreUpdate
    void alActualizar() { actualizadoEn = Instant.now(); }
}
