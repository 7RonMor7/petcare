-- ---------------------------------------------------------------------------
-- V14 — Correccion del tipo de orden_pago.moneda              (arregla V13)
--
-- V13 declaro la columna como CHAR(3). Un String de JPA se mapea a VARCHAR, y
-- ddl-auto: validate rechaza la diferencia al arrancar.
--
-- V13 ya estaba aplicada, asi que no se edita: se corrige con una migracion
-- nueva. Misma regla que en V3/V4 con refresh_token.
--
-- (CHAR habria sido defendible por eficiencia: los codigos ISO 4217 miden
-- siempre 3. Pero la coherencia con la entidad vale mas que esa micro-optimizacion.)
-- ---------------------------------------------------------------------------

ALTER TABLE orden_pago
    MODIFY COLUMN moneda VARCHAR(3) NOT NULL DEFAULT 'COP';
