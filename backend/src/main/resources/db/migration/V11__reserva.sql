-- ---------------------------------------------------------------------------
-- V11 — Reservas                                (base de HU-031; HU-032/033)
--
-- La tabla se crea en el Sprint 4 aunque el endpoint para CREAR reservas
-- llegue en el Sprint 5: el motor de disponibilidad no puede descontar lo que
-- no existe. Aqui solo se define la estructura; las reglas de creacion,
-- la maquina de estados (HU-035) y la restriccion de concurrencia (HU-033)
-- llegan despues.
-- ---------------------------------------------------------------------------

CREATE TABLE reserva (
    id                BIGINT        NOT NULL AUTO_INCREMENT,
    cliente_id        BIGINT        NOT NULL,
    mascota_id        BIGINT        NOT NULL,
    servicio_id       BIGINT        NOT NULL,
    empleado_id       BIGINT        NOT NULL,

    -- Instantes en UTC. El fin se GUARDA, no se calcula al vuelo: la duracion
    -- del servicio puede cambiar manana, y la reserva de hoy debe conservar
    -- la franja que realmente ocupo.
    fecha_hora_inicio DATETIME      NOT NULL,
    fecha_hora_fin    DATETIME      NOT NULL,

    -- PENDIENTE_PAGO | CONFIRMADA | EN_PROCESO | COMPLETADA
    -- CANCELADA | EXPIRADA | NO_ASISTIO
    estado            VARCHAR(20)   NOT NULL,

    -- Total congelado al crear la reserva (CN-01): cambiar el precio del
    -- catalogo no altera lo ya vendido.
    total             DECIMAL(10,2) NOT NULL,

    creado_en         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
                                    ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),

    -- Los tres indices corresponden a las tres preguntas frecuentes:
    -- "que ocupa este empleado", "que reservo este cliente", "que hay de
    -- esta mascota".
    KEY idx_reserva_empleado_fecha (empleado_id, fecha_hora_inicio),
    KEY idx_reserva_cliente (cliente_id),
    KEY idx_reserva_mascota (mascota_id),

    CONSTRAINT fk_reserva_cliente  FOREIGN KEY (cliente_id)  REFERENCES usuario (id),
    CONSTRAINT fk_reserva_mascota  FOREIGN KEY (mascota_id)  REFERENCES mascota (id),
    CONSTRAINT fk_reserva_servicio FOREIGN KEY (servicio_id) REFERENCES servicio (id),
    CONSTRAINT fk_reserva_empleado FOREIGN KEY (empleado_id) REFERENCES usuario (id),

    CONSTRAINT ck_reserva_rango CHECK (fecha_hora_fin > fecha_hora_inicio),
    CONSTRAINT ck_reserva_total CHECK (total >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- Sin ON DELETE CASCADE en ninguna clave foranea, a proposito: una reserva es
-- un hecho contable. Que el borrado de un usuario o una mascota falle es la
-- respuesta correcta; por eso ambos se desactivan y no se borran.
