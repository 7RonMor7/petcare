-- ---------------------------------------------------------------------------
-- V5 — Permisos y su asignacion a roles                    (HU-009, IRR-02)
--
-- El sistema autoriza por PERMISO, no por rol (ADR-006). Un endpoint exige
-- "MASCOTA_CREAR", no "ser CLIENTE". El rol es solo un paquete de permisos.
--
-- Por que importa: el dia que el cliente pida "que el empleado tambien pueda
-- ver el catalogo de promociones", eso es un INSERT en rol_permiso. Si la
-- autorizacion estuviera escrita como hasRole('ADMIN') por todo el codigo,
-- seria buscar y recompilar.
--
-- Se cargan ya los permisos de historias futuras. Cuestan una fila cada uno
-- y evitan tener que migrar el catalogo en cada sprint.
-- ---------------------------------------------------------------------------

CREATE TABLE permiso (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(60)  NOT NULL,
    descripcion VARCHAR(200) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_permiso_nombre (nombre)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


CREATE TABLE rol_permiso (
    rol_id     BIGINT NOT NULL,
    permiso_id BIGINT NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    CONSTRAINT fk_rol_permiso_rol
        FOREIGN KEY (rol_id) REFERENCES rol (id) ON DELETE CASCADE,
    CONSTRAINT fk_rol_permiso_permiso
        FOREIGN KEY (permiso_id) REFERENCES permiso (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


INSERT INTO permiso (nombre, descripcion) VALUES
    ('MASCOTA_CREAR',                    'Registrar una mascota'),
    ('MASCOTA_LEER_PROPIA',              'Consultar las mascotas propias'),
    ('MASCOTA_EDITAR_PROPIA',            'Editar las mascotas propias'),
    ('MASCOTA_ELIMINAR_PROPIA',          'Eliminar las mascotas propias'),
    ('MASCOTA_LEER_DE_RESERVA',          'Ver la ficha de la mascota de una reserva asignada'),
    ('SERVICIO_LEER',                    'Consultar el catalogo de servicios'),
    ('SERVICIO_GESTIONAR',               'Crear, editar y desactivar servicios'),
    ('EMPLEADO_GESTIONAR',               'Crear, editar y desactivar empleados'),
    ('AGENDA_LEER_PROPIA',               'Consultar la agenda propia'),
    ('AGENDA_BLOQUEAR_PROPIA',           'Bloquear franjas de la agenda propia'),
    ('DISPONIBILIDAD_CONSULTAR',         'Consultar franjas disponibles'),
    ('RESERVA_CREAR',                    'Crear una reserva'),
    ('RESERVA_LEER_PROPIA',              'Consultar las reservas propias'),
    ('RESERVA_LEER_ASIGNADA',            'Consultar las reservas asignadas'),
    ('RESERVA_LEER_TODAS',               'Consultar todas las reservas'),
    ('RESERVA_CANCELAR_PROPIA',          'Cancelar una reserva propia'),
    ('RESERVA_CAMBIAR_ESTADO_ASIGNADA',  'Cambiar el estado de una reserva asignada'),
    ('RESERVA_CAMBIAR_ESTADO_CUALQUIERA','Cambiar el estado de cualquier reserva'),
    ('PAGO_LEER_PROPIO',                 'Consultar los pagos propios'),
    ('PAGO_LEER_TODOS',                  'Consultar todos los pagos'),
    ('REEMBOLSO_GESTIONAR',              'Procesar solicitudes de reembolso'),
    ('PROMOCION_APLICAR',                'Aplicar un codigo de promocion'),
    ('PROMOCION_GESTIONAR',              'Crear y editar promociones'),
    ('AUDITORIA_LEER',                   'Consultar la bitacora de auditoria'),
    ('DASHBOARD_LEER',                   'Consultar los indicadores del panel');


-- Asignacion por nombre y no por id: el INSERT ... SELECT resuelve los ids
-- en el momento, asi que no dependemos de que AUTO_INCREMENT haya asignado
-- los numeros que suponemos.

INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'CLIENTE' AND p.nombre IN (
    'MASCOTA_CREAR', 'MASCOTA_LEER_PROPIA', 'MASCOTA_EDITAR_PROPIA',
    'MASCOTA_ELIMINAR_PROPIA', 'SERVICIO_LEER', 'DISPONIBILIDAD_CONSULTAR',
    'RESERVA_CREAR', 'RESERVA_LEER_PROPIA', 'RESERVA_CANCELAR_PROPIA',
    'PAGO_LEER_PROPIO', 'PROMOCION_APLICAR'
);

INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'EMPLEADO' AND p.nombre IN (
    'MASCOTA_LEER_DE_RESERVA', 'SERVICIO_LEER', 'AGENDA_LEER_PROPIA',
    'AGENDA_BLOQUEAR_PROPIA', 'DISPONIBILIDAD_CONSULTAR',
    'RESERVA_LEER_ASIGNADA', 'RESERVA_CAMBIAR_ESTADO_ASIGNADA'
);

INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id FROM rol r, permiso p
WHERE r.nombre = 'ADMINISTRADOR' AND p.nombre IN (
    'MASCOTA_CREAR', 'MASCOTA_LEER_DE_RESERVA', 'SERVICIO_LEER',
    'SERVICIO_GESTIONAR', 'EMPLEADO_GESTIONAR', 'DISPONIBILIDAD_CONSULTAR',
    'RESERVA_LEER_TODAS', 'RESERVA_CAMBIAR_ESTADO_CUALQUIERA',
    'PAGO_LEER_TODOS', 'REEMBOLSO_GESTIONAR', 'PROMOCION_GESTIONAR',
    'AUDITORIA_LEER', 'DASHBOARD_LEER'
);
