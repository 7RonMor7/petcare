-- ---------------------------------------------------------------------------
-- V13 — Ordenes de pago                                 (HU-039; RB03, RB05)
--
-- Una orden de pago es la INTENCION de cobrar un monto por una reserva. No es
-- el pago: es lo que se le presenta a la pasarela y lo que el webhook vendra a
-- aprobar o rechazar (HU-041).
--
-- Por que una tabla aparte y no columnas en reserva:
--   1. Una reserva puede tener VARIAS ordenes. Si el pago se rechaza, se emite
--      una nueva para reintentar (IRR-12); la rechazada se conserva como
--      historial, porque un intento fallido tambien es informacion contable.
--   2. El ciclo de vida es distinto: la reserva vive dias; la orden expira en
--      15 minutos (RB05).
--
-- El monto se copia de reserva.total, que a su vez se copio del catalogo al
-- crear la reserva (CN-01). Entre el catalogo y el cobro hay dos copias
-- deliberadas: cambiar un precio no toca ni reservas ni ordenes existentes.
-- ---------------------------------------------------------------------------

CREATE TABLE orden_pago (
    id                 BIGINT        NOT NULL AUTO_INCREMENT,
    reserva_id         BIGINT        NOT NULL,

    monto              DECIMAL(10,2) NOT NULL,

    -- ISO 4217. Hoy siempre COP, pero el dia que exista otra moneda, un monto
    -- sin moneda es un numero sin significado.
    moneda             CHAR(3)       NOT NULL DEFAULT 'COP',

    -- PENDIENTE | APROBADO | RECHAZADO | EXPIRADO | REEMBOLSADO
    estado             VARCHAR(20)   NOT NULL,

    -- Identificador que devuelve la pasarela. Es NULL hasta que exista el
    -- checkout (HU-040). UNIQUE: el webhook puede llegar dos veces con el
    -- mismo id, y la base garantiza que solo se procese una (idempotencia).
    referencia_externa VARCHAR(100)  NULL,
    proveedor          VARCHAR(30)   NULL,

    -- RB05: la reserva sin pagar expira a los 15 minutos (parametrizable).
    expira_en          DATETIME      NOT NULL,

    creado_en          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_orden_referencia (referencia_externa),
    KEY idx_orden_reserva (reserva_id),
    KEY idx_orden_estado_expira (estado, expira_en),

    CONSTRAINT fk_orden_reserva
        FOREIGN KEY (reserva_id) REFERENCES reserva (id),

    CONSTRAINT ck_orden_monto CHECK (monto >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
