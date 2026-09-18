-- ---------------------------------------------------------------------------
-- V2 — Usuarios y roles                                    (HU-007, IRR-01/02)
--
-- Tres tablas:
--   rol          catalogo cerrado de roles del sistema
--   usuario      las personas que entran a la plataforma
--   usuario_rol  tabla intermedia N–M entre las dos anteriores
--
-- Por que usuario_rol y no una columna rol_id en usuario:
-- hoy cada persona tiene exactamente un rol, asi que una columna bastaria.
-- Pero pasar de "una columna" a "una tabla intermedia" mas adelante obliga a
-- migrar datos existentes y a tocar todas las consultas que ya usen el campo.
-- Al reves no cuesta nada: la tabla intermedia soporta un solo rol sin
-- problema. Es una de esas decisiones que son baratas ahora y caras despues.
-- ---------------------------------------------------------------------------

CREATE TABLE rol (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(30)  NOT NULL,
    descripcion VARCHAR(150) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_rol_nombre (nombre)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


CREATE TABLE usuario (
    id                        BIGINT       NOT NULL AUTO_INCREMENT,
    nombre                    VARCHAR(80)  NOT NULL,
    apellido                  VARCHAR(80)  NOT NULL,
    correo                    VARCHAR(160) NOT NULL,
    telefono                  VARCHAR(20)  NULL,

    -- 72 caracteres, no 255: un hash BCrypt mide siempre 60. El tamano de la
    -- columna no tiene nada que ver con lo larga que sea la contrasena.
    contrasena_hash           VARCHAR(72)  NOT NULL,

    activo                    BOOLEAN      NOT NULL DEFAULT TRUE,

    -- Ley 1581 de 2012 (Habeas Data): hay que poder demostrar cuando y sobre
    -- que version de la politica dio su autorizacion cada persona.
    fecha_aceptacion_politica TIMESTAMP    NULL,
    version_politica          VARCHAR(10)  NULL,

    creado_en                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
                                           ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),

    -- La unicidad del correo se garantiza AQUI, no solo en el servicio.
    -- Dos peticiones simultaneas con el mismo correo pueden pasar las dos la
    -- validacion de Java; solo la base de datos puede impedir el duplicado.
    UNIQUE KEY uk_usuario_correo (correo)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


CREATE TABLE usuario_rol (
    usuario_id BIGINT NOT NULL,
    rol_id     BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_usuario_rol_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_rol_rol
        FOREIGN KEY (rol_id) REFERENCES rol (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- Catalogo de roles (DEC-C01). Se carga por migracion y no a mano: asi existe
-- igual en tu portatil, en el de cualquiera y en el servidor.
INSERT INTO rol (nombre, descripcion) VALUES
    ('CLIENTE',       'Registra mascotas, reserva servicios y paga'),
    ('EMPLEADO',      'Consulta su agenda y opera sus reservas asignadas'),
    ('ADMINISTRADOR', 'Gestiona el negocio completo');
