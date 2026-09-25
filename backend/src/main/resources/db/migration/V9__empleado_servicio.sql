-- ---------------------------------------------------------------------------
-- V9 — Servicios que presta cada empleado                   (HU-021, IRR-15)
--
-- Relacion N–M entre empleados (usuario con rol EMPLEADO) y servicios.
--
-- Para que sirve: en el Sprint 4, al preguntar "quien puede atender un bano
-- el martes a las 10", el motor cruzara TRES cosas: esta tabla (quien sabe
-- hacerlo), jornada_laboral (quien trabaja a esa hora) y las reservas ya
-- creadas (quien esta libre). Sin esta tabla, la unica respuesta posible
-- seria "todos los empleados prestan todos los servicios", que es justo la
-- degradacion que el backlog dejaba como plan B.
-- ---------------------------------------------------------------------------

CREATE TABLE empleado_servicio (
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    empleado_id BIGINT    NOT NULL,
    servicio_id BIGINT    NOT NULL,
    creado_en   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    -- La pareja no puede repetirse: un empleado presta un servicio o no lo
    -- presta; no hay "lo presta dos veces".
    UNIQUE KEY uk_empleado_servicio (empleado_id, servicio_id),

    -- Indice para la consulta inversa del motor de disponibilidad:
    -- "que empleados prestan el servicio X".
    KEY idx_empleado_servicio_servicio (servicio_id),

    CONSTRAINT fk_empleado_servicio_empleado
        FOREIGN KEY (empleado_id) REFERENCES usuario (id) ON DELETE CASCADE,

    -- Sin cascada del lado del servicio: un servicio no se borra nunca, se
    -- desactiva (HU-029). Si algun dia alguien intentara borrarlo de verdad,
    -- que falle es la respuesta correcta.
    CONSTRAINT fk_empleado_servicio_servicio
        FOREIGN KEY (servicio_id) REFERENCES servicio (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
