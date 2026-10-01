-- ---------------------------------------------------------------------------
-- V12 — Ocupacion de franjas                                 (HU-033, RNF07-R)
--
-- EL PROBLEMA: "dos reservas no pueden solaparse" no se puede expresar con una
-- restriccion de MySQL. UNIQUE compara valores iguales, no intervalos que se
-- cruzan; y un CHECK solo ve la fila que se esta escribiendo, nunca las demas.
-- Validarlo en Java tampoco basta: dos peticiones simultaneas leen la agenda
-- libre al mismo tiempo, las dos pasan la validacion y las dos insertan.
--
-- LA SOLUCION: convertir el solapamiento en una igualdad. Como la agenda es
-- una rejilla de franjas fijas, una reserva de 09:00 a 10:00 ocupa las franjas
-- 09:00 y 09:30. Se escribe una fila por franja ocupada, y entonces el
-- solapamiento SI es un duplicado, que es lo que UNIQUE sabe impedir.
--
--   Reserva 09:00-10:00  ->  filas 09:00, 09:30
--   Reserva 09:30-10:30  ->  filas 09:30, 10:00   <- 09:30 ya existe: rechazada
--
-- Dos restricciones sobre la misma fila cubren dos reglas distintas:
--   uk_ocupacion_empleado  = RB02  (un empleado, una reserva a la vez)
--   uk_ocupacion_mascota   = RB10  (una mascota no puede estar en dos sitios)
--
-- La garantia es de la base de datos, no del servicio: bajo concurrencia, el
-- motor serializa los INSERT y exactamente uno gana. Es lo que exige RNF07-R.
-- ---------------------------------------------------------------------------

CREATE TABLE ocupacion_franja (
    id            BIGINT   NOT NULL AUTO_INCREMENT,
    reserva_id    BIGINT   NOT NULL,
    empleado_id   BIGINT   NOT NULL,
    mascota_id    BIGINT   NOT NULL,

    -- Inicio de la franja, en UTC y alineado a la rejilla.
    inicio_franja DATETIME NOT NULL,

    PRIMARY KEY (id),

    UNIQUE KEY uk_ocupacion_empleado (empleado_id, inicio_franja),
    UNIQUE KEY uk_ocupacion_mascota  (mascota_id, inicio_franja),
    KEY idx_ocupacion_reserva (reserva_id),

    -- Si algun dia se borrara una reserva, sus franjas se van con ella.
    -- En la practica no se borran: al cancelar o expirar, el servicio elimina
    -- estas filas para liberar el horario, y la reserva permanece como
    -- historial con su nuevo estado.
    CONSTRAINT fk_ocupacion_reserva
        FOREIGN KEY (reserva_id) REFERENCES reserva (id) ON DELETE CASCADE,
    CONSTRAINT fk_ocupacion_empleado
        FOREIGN KEY (empleado_id) REFERENCES usuario (id),
    CONSTRAINT fk_ocupacion_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascota (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
