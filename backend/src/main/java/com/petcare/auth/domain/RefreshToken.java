package com.petcare.auth.domain;

import com.petcare.usuarios.domain.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(name = "familia_id", nullable = false, length = 36)
    private String familiaId;

    @Column(name = "expira_en", nullable = false)
    private Instant expiraEn;

    @Column(nullable = false)
    private boolean revocado = false;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    public RefreshToken(Usuario usuario, String tokenHash, String familiaId, Instant expiraEn) {
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.familiaId = familiaId;
        this.expiraEn = expiraEn;
    }

    public void revocar() {
        this.revocado = true;
    }

    public boolean estaExpirado() {
        return Instant.now().isAfter(expiraEn);
    }

    public boolean esUtilizable() {
        return !revocado && !estaExpirado();
    }

    @PrePersist
    void alCrear() {
        this.creadoEn = Instant.now();
    }
}
