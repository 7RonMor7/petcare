# HU-003 — Decisión de pasarela de pagos

**Sprint 0 (ejecutada en el Sprint 5) · 2 SP · timebox 4 h · IRR-10, RSG-01**

> Como equipo, quiero comparar Wompi, Mercado Pago y PayU en soporte de PSE/Nequi, calidad de
> sandbox, documentación e integración con Spring Boot, para elegir la pasarela con evidencia.

**Decidido por:** Ronald Moreno · **Fecha:** 2026-10-06

---

## Comparación

| Criterio | Wompi | Mercado Pago | PayU |
|---|---|---|---|
| **PSE** | Soportado directamente | Soportado | Soportado |
| **Nequi** | Soportado directamente | Disponible según integración/medio | No se toma como ventaja |
| **Sandbox** | Ambiente separado con llaves `pub_test_` / `prv_test_` | Muy completo; simulador bancario para PSE | Ambiente sandbox con guía específica de PSE |
| **Documentación** | Clara y orientada a API | Muy completa | Buena, más extensa y con partes legacy |
| **Spring Boot / Java** | API REST sencilla de consumir | SDK oficial para Java | API REST |
| **Webhooks** | Eventos, firma y reintentos | Notificaciones | Soportados |
| **Complejidad para este proyecto** | Baja | Media | Media/Alta |
| **Elección** | **Seleccionada** | Alternativa | Alternativa |

## Decisión

**Proveedor seleccionado: Wompi Colombia.**

El punto decisivo es la combinación de PSE + Nequi con la integración más simple:

- Documenta explícitamente ambos medios. Para PSE se consultan las instituciones financieras por API
  y luego se inicia la transacción; para Nequi basta el tipo `NEQUI` y el número de celular.
- El sandbox está separado de producción, con URLs y llaves distintas
  (`https://sandbox.wompi.co/v1` frente a `https://production.wompi.co/v1`, llaves `pub_test_` y
  `prv_test_`). Se desarrolla PetCare completo sin mover dinero real.
- Tiene webhooks para los cambios de estado de la transacción, incluidos aprobación y rechazo, con
  validación por checksum y reintentos si el servidor no responde `200`. Es justamente lo que
  necesita HU-041.
- Su API REST se consume desde Spring Boot sin añadir una dependencia tecnológica innecesaria.

## Consecuencias para la arquitectura

1. **El código no dependerá de Wompi.** HU-004 define el puerto `PaymentGateway`; Wompi será un
   adaptador más, junto al simulado. Si el cliente cambiara de proveedor, se escribe otro adaptador
   y no se toca el motor de reservas.
2. **La validación por checksum de HU-041 no es opcional.** Un webhook sin verificar es un endpoint
   público que cualquiera puede usar para confirmar reservas sin pagar.
3. **Los reintentos del proveedor obligan a idempotencia.** El mismo evento puede llegar dos veces;
   por eso `orden_pago.referencia_externa` es `UNIQUE` desde V13.
4. **El webhook necesita una URL pública.** `localhost` no recibe notificaciones: esto conecta con
   HU-005 (despliegue), que sigue pendiente y bloquea la prueba de extremo a extremo.
5. **Las credenciales van en variables de entorno**, nunca en el repositorio, igual que `JWT_SECRETO`.

## Pendiente al integrar

Los detalles concretos de la API (nombres de campos, formato exacto del checksum, catálogo de
estados) se contrastan contra la documentación oficial vigente en el momento de escribir el
adaptador real, no contra esta nota.

**Siguiente:** HU-004 — el puerto `PaymentGateway` y el adaptador simulado.
