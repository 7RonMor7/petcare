-- ---------------------------------------------------------------------------
-- V10 — Bloqueos de agenda                            (base de HU-024, HU-031)
--
-- Bloqueos de agenda: tramos en los que un empleado NO esta disponible aunque
-- su jornada diga lo contrario (cita medica, capacitacion, permiso).
--
-- Se crea ahora, antes de HU-024, porque el motor de disponibilidad (HU-031)
-- tiene que descontarlos desde el primer dia: un motor que solo descuenta
-- reservas ofreceria horas en las que el empleado no esta.
--
-- Instantes y no hora+dia de la semana: un bloqueo es puntual ("el martes 14
-- de octubre de 9 a 11"), no una regla semanal como la jornada.
-- ---------------------------------------------------------------------------

CREATE TABLE bloqueo_agenda (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    empleado_id       BIGINT       NOT NULL,

    -- DATETIME en UTC, igual que el resto de instantes del sistema.
    fecha_hora_inicio DATETIME     NOT NULL,
    fecha_hora_fin    DATETIME     NOT NULL,

    motivo            VARCHAR(200) NULL,
    creado_en         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    -- La consulta del motor siempre es "bloqueos de ESTE empleado en ESTE
    -- rango", asi que el indice va por empleado y fecha, en ese orden.
    KEY idx_bloqueo_empleado_fecha (empleado_id, fecha_hora_inicio),

    CONSTRAINT fk_bloqueo_empleado
        FOREIGN KEY (empleado_id) REFERENCES usuario (id) ON DELETE CASCADE,

    CONSTRAINT ck_bloqueo_rango CHECK (fecha_hora_fin > fecha_hora_inicio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- La jornada se define en hora local ("los lunes de 8 a 12") y los instantes
-- se guardan en UTC. El motor necesita saber con que zona convertir.
INSERT INTO parametro_sistema (clave, valor, descripcion) VALUES
    ('zona_horaria', 'America/Bogota', 'Zona con la que se interpretan las horas de la jornada');
