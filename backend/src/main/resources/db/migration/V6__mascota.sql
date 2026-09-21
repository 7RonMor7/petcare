-- ---------------------------------------------------------------------------
-- V6 — Mascotas                                            (HU-016, IRR-03)
--
-- Tipos contrastados contra la entidad Mascota ANTES de escribir esto
-- (leccion de V3/V4):
--   VARCHAR(n)    <-> String / enum con @JdbcTypeCode(VARCHAR)
--   DECIMAL(5,2)  <-> BigDecimal con precision = 5, scale = 2
--   DATE          <-> LocalDate
--   TIMESTAMP     <-> Instant
-- ---------------------------------------------------------------------------

CREATE TABLE mascota (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    cliente_id       BIGINT        NOT NULL,
    nombre           VARCHAR(60)   NOT NULL,

    -- Los enumerados se guardan como TEXTO ('PERRO'), nunca como numero.
    -- Si se guardara el ordinal (0, 1, 2), reordenar el enum en Java
    -- cambiaria en silencio el significado de todas las filas existentes.
    especie          VARCHAR(10)   NOT NULL,
    raza             VARCHAR(60)   NULL,
    sexo             VARCHAR(10)   NOT NULL,

    -- DATE y no TIMESTAMP: una fecha de nacimiento es una fecha civil, sin
    -- hora ni zona horaria. Guardarla como instante UTC haria que un perro
    -- nacido el 15 de marzo apareciera como nacido el 14 segun la zona.
    fecha_nacimiento DATE          NOT NULL,

    -- DECIMAL y no DOUBLE: DOUBLE es binario y no representa exactamente
    -- valores como 28.1. Para un peso no es grave, pero el habito importa:
    -- cuando lleguemos a precios, un DOUBLE seria un error real.
    peso_kg          DECIMAL(5,2)  NOT NULL,

    observaciones    VARCHAR(500)  NULL,

    -- Borrado logico (HU-017): una mascota con historial de reservas no se
    -- borra de verdad, se desactiva.
    activo           BOOLEAN       NOT NULL DEFAULT TRUE,

    creado_en        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_mascota_cliente (cliente_id),

    -- Sin ON DELETE CASCADE, a proposito: borrar un usuario no debe arrastrar
    -- en silencio mascotas que pueden tener reservas y pagos asociados.
    CONSTRAINT fk_mascota_cliente
        FOREIGN KEY (cliente_id) REFERENCES usuario (id),

    -- Defensa en profundidad: la validacion vive en el DTO, pero si algun dia
    -- un script o un endpoint olvidado escribe directo, la base lo rechaza.
    CONSTRAINT ck_mascota_peso CHECK (peso_kg > 0 AND peso_kg <= 200)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
