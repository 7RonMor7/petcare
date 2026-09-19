-- ---------------------------------------------------------------------------
-- V4 — Corregir tipos de columna en refresh_token
--
-- V3 declaro token_hash y familia_id como CHAR. La entidad RefreshToken las
-- declara como String con @Column(length = ...), que en JPA significa VARCHAR.
-- Hibernate detecto el desajuste al validar el esquema y se nego a arrancar.
--
-- No se edita V3 porque ya fue aplicada y publicada: su checksum esta
-- registrado en flyway_schema_history y otras copias del repositorio la
-- tienen aplicada tal cual. Un esquema se corrige hacia delante.
--
-- Nota sobre MODIFY COLUMN: reescribe la definicion completa de la columna,
-- asi que hay que repetir NOT NULL o se perderia. Los indices (incluido el
-- UNIQUE sobre token_hash) son objetos aparte y no se ven afectados.
--
-- VARCHAR es ademas el tipo correcto por merito propio: con utf8mb4 un
-- CHAR(64) reserva 4 bytes por caracter (256 por fila) aunque el valor sea
-- mas corto, mientras que VARCHAR guarda solo lo que ocupa.
-- ---------------------------------------------------------------------------

ALTER TABLE refresh_token
    MODIFY COLUMN token_hash VARCHAR(64) NOT NULL,
    MODIFY COLUMN familia_id VARCHAR(36) NOT NULL;
