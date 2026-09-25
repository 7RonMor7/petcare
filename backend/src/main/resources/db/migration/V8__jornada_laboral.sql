-- ---------------------------------------------------------------------------
-- V8 — Jornada laboral semanal del empleado                (HU-022, IRR-23)
--
-- Una fila = un tramo de trabajo de un empleado en un dia de la semana.
-- Varios tramos por dia son normales: 08:00-12:00 y 14:00-18:00 (almuerzo).
--
-- Esta tabla es la MATERIA PRIMA del motor de disponibilidad (HU-030): sobre
-- ella se generan las franjas de 30 minutos, y de ahi se descuentan reservas
-- y bloqueos. Por eso se modela como tramos y no como "hora de entrada y hora
-- de salida" en columnas del empleado: sin tramos no hay forma de expresar el
-- almuerzo, y el motor ofreceria horas en las que nadie atiende.
--
-- No hay tabla "empleado": un empleado es un usuario con rol EMPLEADO (HU-020).
-- ---------------------------------------------------------------------------

CREATE TABLE jornada_laboral (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    empleado_id BIGINT      NOT NULL,

    -- Nombres de java.time.DayOfWeek: MONDAY .. SUNDAY.
    dia_semana  VARCHAR(10) NOT NULL,

    -- TIME y no DATETIME: es una hora del reloj que se repite cada semana,
    -- no un instante. "Los lunes a las 8" no es una fecha concreta.
    hora_inicio TIME        NOT NULL,
    hora_fin    TIME        NOT NULL,

    creado_en   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    KEY idx_jornada_empleado (empleado_id),

    -- Aqui SI hay cascada, al reves que en mascota: la jornada es
    -- configuracion que solo existe mientras exista el empleado, y ninguna
    -- reserva apunta a ella. No hay historial que preservar.
    CONSTRAINT fk_jornada_empleado
        FOREIGN KEY (empleado_id) REFERENCES usuario (id) ON DELETE CASCADE,

    -- Evita el duplicado exacto. El solapamiento parcial (08:00-12:00 contra
    -- 11:00-15:00) NO se puede expresar con una restriccion de tabla: se
    -- valida en el servicio, porque depende de comparar filas entre si.
    UNIQUE KEY uk_jornada_tramo (empleado_id, dia_semana, hora_inicio),

    CONSTRAINT ck_jornada_horas CHECK (hora_fin > hora_inicio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
