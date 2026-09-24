-- ---------------------------------------------------------------------------
-- V7 — Catalogo de servicios                    (HU-027, HU-028, HU-029; IRR-04)
--
-- CN-01: el precio es fijo POR SERVICIO, salvo el hospedaje, que se cobra POR
-- DIA. Por eso la unidad de cobro es una columna y no una suposicion escrita
-- en el codigo: el total de una reserva se calculara con ella y se congelara
-- al crearla (el precio puede cambiar despues; el total cobrado no).
-- ---------------------------------------------------------------------------

CREATE TABLE servicio (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(80)   NOT NULL,
    descripcion      VARCHAR(400)  NULL,

    -- DECIMAL y nunca DOUBLE: esto es dinero.
    precio           DECIMAL(10,2) NOT NULL,

    -- POR_SERVICIO | POR_DIA
    unidad_cobro     VARCHAR(20)   NOT NULL,

    -- Minutos que ocupa en la agenda. NULL para los servicios POR_DIA, que no
    -- consumen franjas horarias sino cupos por noche (DIS-01, diferido).
    duracion_minutos INT           NULL,

    -- HU-029: desactivar no borra. Las reservas ya confirmadas siguen
    -- apuntando a la fila y conservan su historial.
    activo           BOOLEAN       NOT NULL DEFAULT TRUE,

    creado_en        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_servicio_nombre (nombre),

    CONSTRAINT ck_servicio_precio CHECK (precio >= 0),

    -- Coherencia entre unidad y duracion, garantizada por la base:
    -- POR_SERVICIO exige duracion; POR_DIA exige que no la tenga.
    CONSTRAINT ck_servicio_duracion CHECK (
        (unidad_cobro = 'POR_SERVICIO' AND duracion_minutos IS NOT NULL AND duracion_minutos > 0)
     OR (unidad_cobro = 'POR_DIA'      AND duracion_minutos IS NULL)
    )
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


-- Los cinco servicios del negocio, cargados por migracion (escalon 5 de la
-- escalera: el CRUD de HU-027 existe, pero el catalogo no nace vacio).
-- Duraciones alineadas con PRG-70: rejilla de 30 min; la consulta veterinaria
-- dura 45 pero ocupara 60 (dos franjas).

INSERT INTO servicio (nombre, descripcion, precio, unidad_cobro, duracion_minutos, activo) VALUES
    ('Paseo',
     'Paseo individual con recogida y entrega en la direccion del cliente.',
     25000.00, 'POR_SERVICIO', 60, TRUE),

    ('Cuidado a domicilio',
     'Acompanamiento en casa del cliente: alimentacion, juego y paseo corto.',
     45000.00, 'POR_SERVICIO', 120, TRUE),

    ('Bano y peluqueria',
     'Bano, secado, corte de unas y peluqueria segun la raza.',
     55000.00, 'POR_SERVICIO', 90, TRUE),

    ('Consulta veterinaria',
     'Consulta general con medico veterinario. Ocupa una hora de agenda.',
     70000.00, 'POR_SERVICIO', 45, TRUE),

    ('Hospedaje temporal',
     'Alojamiento por noche con alimentacion y paseos incluidos.',
     70000.00, 'POR_DIA', NULL, FALSE);
