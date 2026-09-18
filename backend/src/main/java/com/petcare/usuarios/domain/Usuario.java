package com.petcare.usuarios.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Column(nullable = false, unique = true, length = 160)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(name = "contrasena_hash", nullable = false, length = 72)
    private String contrasenaHash;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_aceptacion_politica")
    private Instant fechaAceptacionPolitica;

    @Column(name = "version_politica", length = 10)
    private String versionPolitica;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    public Usuario(String nombre, String apellido, String correo, String telefono,
                   String contrasenaHash, String versionPolitica) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasenaHash = contrasenaHash;
        this.versionPolitica = versionPolitica;
        this.fechaAceptacionPolitica = Instant.now();
    }

    public void asignarRol(Rol rol) {
        this.roles.add(rol);
    }

    @PrePersist
    void alCrear(){
        Instant ahora = Instant.now();
        this.creadoEn = ahora;
        this.actualizadoEn = ahora;
    }

    @PreUpdate
    void alActualizar(){
        this.actualizadoEn = Instant.now();
    }

}
