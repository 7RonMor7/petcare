-- ---------------------------------------------------------------------------
-- V15 — URL de checkout en la orden de pago                        (HU-040)
--
-- Al abrir el cobro, la pasarela devuelve DOS datos: la referencia externa
-- (que ya se guardaba) y la URL a la que hay que enviar al cliente.
--
-- Guardar la URL hace idempotente el inicio del checkout: si el cliente pulsa
-- "Pagar" dos veces, o recarga la pestana, se le devuelve la MISMA sesion de
-- cobro en lugar de abrir una nueva. Dos sesiones vivas para una sola orden
-- significan dos webhooks posibles y un cobro duplicado.
-- ---------------------------------------------------------------------------

ALTER TABLE orden_pago
    ADD COLUMN url_checkout VARCHAR(500) NULL AFTER referencia_externa;
