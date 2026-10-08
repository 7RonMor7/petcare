# Guía — Sprints 5 y 6: reservas, concurrencia y pagos

**HU-032 · HU-033 · HU-035 · HU-039 · HU-003 · HU-004 · HU-040 · HU-041 · HU-042**

De "el sistema sabe qué horarios hay libres" a "el cliente reserva, paga y su reserva se confirma
sola cuando el pago se aprueba".

---

## 1. La concurrencia (HU-033, RNF07-R)

"Dos reservas no pueden solaparse" no se puede escribir como restricción de MySQL: `UNIQUE` compara
valores iguales, no intervalos que se cruzan, y un `CHECK` solo ve su propia fila. Validarlo en Java
tampoco basta: dos peticiones simultáneas leen la agenda libre a la vez y las dos insertan.

**La idea: convertir el solapamiento en una igualdad.** Como la agenda es una rejilla fija, una
reserva de 09:00 a 10:00 ocupa las franjas 09:00 y 09:30, y se escribe **una fila por franja**
(`ocupacion_franja`, V12). Entonces el solapamiento sí es un duplicado:

| Restricción | Regla |
|---|---|
| `uk_ocupacion_empleado (empleado_id, inicio_franja)` | RB02 — un empleado, una reserva a la vez |
| `uk_ocupacion_mascota (mascota_id, inicio_franja)` | RB10 — una mascota no está en dos sitios |

La garantía la da el motor de la base, que serializa los `INSERT`. La prueba con 20 hilos
simultáneos lo demuestra: exactamente uno gana.

**El `flush()` dentro del `try` no es decorativo:** sin él los `INSERT` salen al cerrar la
transacción, fuera del bloque, y el `Duplicate entry` se vuelve un `500` en vez de un `409`.

## 2. La máquina de estados (HU-035)

Las transiciones viven en el enum `EstadoReserva`, no repartidas en `if`. `PENDIENTE_PAGO →
COMPLETADA` simplemente no existe: RB03 (no hay servicio sin pago) expresado como estructura de
datos. Cuatro estados son finales. Una prueba recorre todos los valores para que añadir uno nuevo y
olvidarlo en el mapa falle en `mvn test` y no en producción.

## 3. El dinero (HU-039, CN-01)

```
servicio.precio ──copia al reservar──▶ reserva.total ──copia al cobrar──▶ orden.monto
```

Dos copias deliberadas: cambiar el catálogo no altera lo vendido ni lo cobrado. Una reserva puede
tener **varias** órdenes (un rechazo se conserva como historial, IRR-12), por eso es tabla aparte.

## 4. Puertos y adaptadores (HU-003, HU-004)

El spike eligió **Wompi** (`docs/15`). Pero el código no depende de Wompi: define el puerto
`PaymentGateway` con tres métodos, y hoy lo implementa `PasarelaSimulada`, que permite construir y
probar todo el flujo sin credenciales. `@ConditionalOnProperty` elige el adaptador; con
`PAGOS_PROVEEDOR=wompi` el arranque falla porque no existe aún — y eso es lo correcto: mejor fallar
al arrancar que ante el primer cliente que intente pagar.

La firma del simulador es `SHA-256(cuerpo + secreto)` y se compara con `MessageDigest.isEqual`,
en tiempo constante: un `equals` normal revela, por el tiempo de respuesta, cuántos caracteres
acertó un atacante.

## 5. El checkout (HU-040)

`POST /reservas/{id}/pago/checkout` es idempotente: si ya hay sesión abierta devuelve la misma URL.
Dos sesiones vivas para una orden significan dos webhooks posibles. Es `POST` y no `GET` porque la
primera llamada **crea** algo en la pasarela.

## 6. El webhook (HU-041) y la confirmación (HU-042)

Endpoint público, y por eso con tres defensas obligatorias:

| Defensa | Qué evita |
|---|---|
| Firma | Que cualquiera confirme reservas sin pagar |
| Idempotencia (`200 YA_PROCESADO`) | Que un reintento de la pasarela cobre dos veces |
| Contraste de monto y moneda | Que un evento manipulado apruebe 70.000 con 1.000 |

Detalles que importan:

- **`@RequestBody String`**, no un DTO: la firma se calcula sobre los bytes exactos recibidos.
- **Bloqueo pesimista** al leer la orden: dos webhooks simultáneos leerían `PENDIENTE` a la vez.
  Aquí el problema es *leer-modificar-escribir*, no insertar duplicados, así que la herramienta es
  distinta a la de HU-033.
- **Un rechazo no cancela la reserva:** sigue `PENDIENTE_PAGO` para que el cliente reintente.
- **La reserva se confirma solo aquí.** El retorno del navegador no confirma nada: una URL se
  escribe a mano.

## 7. Lecciones de la comprobación

- Una reserva insertada con hora local en una columna UTC hizo que dos pasos "pasaran" por la razón
  equivocada. Lo que entra por la API se convierte solo; lo que entra por SQL, no.
- `ocupacionRepository.count()` en una prueba contaba filas de pruebas manuales ajenas, y el
  `deleteAll()` de limpieza borraba datos que no eran suyos. Una prueba no debe depender del estado
  global ni dejarlo peor de como lo encontró.
- `CHAR(3)` contra un `String` de JPA rompió el arranque (V13 → V14). Un archivo aplicado no se
  edita: se corrige con otro nuevo, aunque el error sea reciente.

## Pruebas automatizadas del bloque

| Clase | Qué cubre |
|---|---|
| `EstadoReservaTest` | Transiciones válidas, estados finales, ningún estado fuera del mapa |
| `ReservaConcurrenciaTest` | 20 hilos, una sola reserva, dos franjas ocupadas |
| `PasarelaSimuladaTest` | Firma válida, ajena, nula y cuerpo alterado |
| `WebhookPagoServiceTest` | Aprobación, rechazo, monto y moneda manipulados, idempotencia, firma |

## Deuda anotada

- Las pruebas escriben en la base de desarrollo. Pendiente: Testcontainers (acordado para el final).
- Reintentar un pago rechazado necesita emitir una orden nueva (HU-043).
- Las reservas sin pagar no expiran todavía (HU-044); la franja queda tomada hasta que alguien actúe.
- El webhook real necesita URL pública: depende de HU-005 (despliegue).

## Cerrar

```bash
git add .
git commit -m "Sprints 5 y 6: reservas con concurrencia garantizada, ordenes de pago, checkout y webhook"
```
