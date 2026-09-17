-- ---------------------------------------------------------------------------
-- V1 — Esquema base
--
-- Esta primera migracion no crea tablas de negocio todavia: solo deja el
-- esquema listo y una tabla de parametros configurables.
--
-- Por que parametro_sistema desde el principio: hay reglas del negocio que el
-- cliente puede querer cambiar sin que toquemos codigo (el tamano de la franja
-- horaria, los minutos que dura una reserva sin pagar, la antelacion minima
-- para reservar). Si eso vive en constantes de Java, cada cambio es un
-- despliegue. Si vive en una tabla, es un UPDATE.
-- ---------------------------------------------------------------------------

CREATE TABLE parametro_sistema (
    clave         VARCHAR(60)   NOT NULL,
    valor         VARCHAR(255)  NOT NULL,
    descripcion   VARCHAR(255)  NULL,
    actualizado_en TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (clave)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

INSERT INTO parametro_sistema (clave, valor, descripcion) VALUES
    ('tamano_franja_minutos',     '30',     'Tamano de la rejilla de agenda en minutos'),
    ('antelacion_minima_horas',   '2',      'Horas minimas de antelacion para reservar un servicio por franja'),
    ('antelacion_maxima_dias',    '30',     'Dias maximos de antelacion para reservar un servicio por franja'),
    ('expiracion_pago_minutos',   '15',     'Minutos que una reserva puede permanecer en PENDIENTE_PAGO'),
    ('cancelacion_minima_horas',  '24',     'Horas minimas antes del servicio para poder cancelar'),
    ('jornada_hora_apertura',     '08:00',  'Hora de apertura de PetCare'),
    ('jornada_hora_cierre',       '18:00',  'Hora de cierre de PetCare');
