# Product Backlog v2 — Alcance comprometido en tres niveles

**Plataforma PetCare · Fase 2b — Compromiso de entrega**

| Campo | Valor |
|---|---|
| Proyecto | Plataforma web de reservas y pagos — PetCare |
| Documento | BKL-PETCARE-002 |
| Versión | 2.0 |
| Fecha | 2026-09-16 |
| Autor | Ronald Moreno |
| Sustituye a | BKL-PETCARE-001 v1.0 (sección 6 y 8) |
| Estado | **Propuesta de compromiso — pendiente de aprobación del cliente** |
| Equipo | 1 desarrollador · ~15 h/semana |
| Plazo | 8 semanas · sprints de 1 semana |

---

## Resumen ejecutivo

El cliente pidió tres cosas: cerrar las preguntas pendientes, partir el backlog en tres niveles y quitar la sobrecarga del Sprint 7. Las tres están hechas. El resultado:

| Nivel | Historias | SP | Qué significa |
|---|:--:|:--:|---|
| **1 — MVP comprometido** | 32 | **102** | Lo que se entrega en 8 semanas |
| **2 — MVP diferido** | 37 | **119** | Estaba en el MVP declarado; no cabe. Segunda entrega |
| **3 — V2** | 7 | **24** | Acordado fuera desde el principio |
| | **76** | **245** | |

**El MVP comprometido cierra el ciclo completo:** un cliente se registra, registra su mascota, consulta el catálogo, ve disponibilidad real, reserva eligiendo empleado o sin preferencia, paga por la pasarela (simulada), su reserva se confirma por webhook, la ve en su historial y puede cancelarla bajo la regla de 24 horas. Las reservas no pagadas expiran a los 15 minutos. Todo desplegado y accesible por URL.

**Tres cosas que el cliente debe saber antes de aprobar:**

1. **La velocidad requerida es 12,75 SP/sprint**, frente a los 5,6 de la estimación conservadora. Este documento explica por qué creo que es alcanzable y qué pasa si no lo es (sección 5). No es un compromiso incondicional: es un compromiso con un punto de control en la semana 3 y una lista de degradación pre-acordada.

2. **Tres elementos de la lista de irrenunciables del cliente quedan en el nivel 2** porque no caben: el rol Empleado operativo, el correo transaccional (y con él la recuperación de contraseña) y la auditoría. La sección 6.4 pone el precio exacto de recuperarlos.

3. **Las respuestas de la ronda 2 aumentaron el alcance, no lo redujeron.** Hacer Must la verificación de correo y la recuperación de contraseña añadió 9 SP, y la ocupación multi-franja de PRG-62 encareció el motor de disponibilidad en 5 SP. Esto está detallado y justificado en la sección 3, porque el cliente pidió explícitamente que no se muevan estimaciones sin justificarlas.

---

## Tabla de contenido

1. [Decisiones de la ronda 2](#1-decisiones-de-la-ronda-2)
2. [Reglas de negocio nuevas](#2-reglas-de-negocio-nuevas)
3. [Re-estimación: qué cambia y por qué](#3-re-estimación-qué-cambia-y-por-qué)
4. [Capacidad recalculada](#4-capacidad-recalculada)
5. [El compromiso y su condición](#5-el-compromiso-y-su-condición)
6. [Los tres niveles de alcance](#6-los-tres-niveles-de-alcance)
7. [Product Backlog v2](#7-product-backlog-v2)
8. [Plan de sprints rebalanceado](#8-plan-de-sprints-rebalanceado)
9. [Escalera de degradación pre-acordada](#9-escalera-de-degradación-pre-acordada)
10. [Criterios de aceptación nuevos](#10-criterios-de-aceptación-nuevos)
11. [Problema de diseño detectado: la rejilla de 30 minutos](#11-problema-de-diseño-detectado-la-rejilla-de-30-minutos)
12. [Preguntas abiertas de tercera ronda](#12-preguntas-abiertas-de-tercera-ronda)
13. [Modelo de datos actualizado](#13-modelo-de-datos-actualizado)
14. [Riesgos v2](#14-riesgos-v2)
15. [Declaración de entrega](#15-declaración-de-entrega)

---

## 1. Decisiones de la ronda 2

| ID | Tema | Decisión | Cierra |
|---|---|---|---|
| **DEC-C13** | Pasarela simulada | Se acepta para la primera entrega: checkout y webhook simulados, con validación de firma, idempotencia, validación de monto y cambio de estado reales. **Debe documentarse que la integración real queda pendiente y no se presenta como productiva.** | PRG-52, CN-09 |
| **DEC-C14** | Hospedaje — capacidad | 10 mascotas por día. Check-in y check-out entre 08:00 y 16:00. Se cobran todos los días del rango seleccionado. | PRG-53 |
| **DEC-C15** | Hospedaje — recurso | Sin empleado asignado. Solo se controlan cupos por día. | PRG-54, CN-02 |
| **DEC-C16** | Bloqueo de agenda | Si existe una reserva activa en el intervalo, la franja **no es bloqueable**. El empleado no puede provocar cancelaciones ni reasignaciones. | PRG-55, CN-03 |
| **DEC-C17** | `NO_ASISTIO` — efecto | Sin reembolso. El pago permanece `APROBADO`. Sin penalización adicional. | PRG-56, GAP-13 |
| **DEC-C18** | `NO_ASISTIO` — ventana | Solo desde `CONFIRMADA`, desde la hora de inicio y hasta 2 horas después. Pasado ese plazo, solo el administrador. | PRG-57 |
| **DEC-C19** | Veterinaria | Únicamente agendamiento. Sin historia clínica, diagnóstico, tratamiento, recetas ni seguimiento. | PRG-58, CN-07, DIS-02 |
| **DEC-C20** | Correo y contraseñas | Verificación de correo → **Must**. Recuperación de contraseña → **Must**. Cambio de contraseña → Should. La infraestructura de correo entra al MVP. | PRG-59, CN-06 |
| **DEC-C21** | Auditoría | Mínimo funcional: tabla, endpoint de consulta y filtros por actor, entidad y rango de fechas. Tabla sencilla en el panel, sin visualización sofisticada. | PRG-60, CN-08 |
| **DEC-C22** | Duraciones | Paseo 60 min · Baño 90 min · Peluquería 120 min · Veterinaria 45 min. Hospedaje por días, sin duración en minutos. | PRG-61, GAP-05 |
| **DEC-C23** | Franjas | Rejilla de 30 minutos. **Un servicio ocupa varias franjas**: peluquería a las 10:00 ocupa hasta las 12:00 y ninguna otra reserva puede solaparse con ese intervalo. | PRG-62, AMB-01 |
| **DEC-C24** | Elección de empleado | Ambas opciones: el cliente elige un empleado disponible, o elige "sin preferencia" y el sistema asigna automáticamente. | PRG-63, AMB-05 |
| **DEC-C25** | Paseos grupales | No. Un empleado solo puede tener una reserva activa por intervalo. | PRG-64, GAP-02 |
| **DEC-C26** | Promoción liberada | Si la reserva expira o se cancela, el uso de la promoción se libera y el código vuelve a estar disponible. | PRG-65, GAP-22 |
| **DEC-C27** | Antelación | Servicios por franja: mínimo 2 h, máximo 30 días. Hospedaje: mínimo 24 h, máximo 60 días. | PRG-66, SUP-04 |
| **DEC-C28** | Alcance vs. plazo | Se mantienen las 8 semanas y se **reduce el alcance comprometido**. El backlog conserva los tres niveles para poder explicar qué se entrega y qué queda pendiente. | PRG-67, RSG-02 |
| **DEC-C29** | Sprint 7 | No se acepta un sprint sobrecargado. Se prefiere sacrificar historias *Should* antes que mantener 26 SP en el último sprint. | — |

> Con DEC-C13 a DEC-C27 quedan cerradas **todas** las contradicciones CN-01 a CN-10 y los vacíos GAP-02, GAP-05, GAP-13 y GAP-22 que seguían abiertos desde la Fase 1. La única salvedad es CN-01, cuya resolución técnica (`unidadCobro`) se mantiene y ahora queda confirmada por DEC-C14.

---

## 2. Reglas de negocio nuevas

Las decisiones de la ronda 2 crean reglas que no existían en el enunciado original. Se numeran a continuación de las diez originales para que el backlog pueda referenciarlas.

| ID | Regla |
|---|---|
| **RB11 — Ocupación multi-franja** | Una reserva por franja ocupa el intervalo `[inicio, inicio + duración del servicio]`. Ninguna otra reserva activa del mismo empleado puede solaparse con ese intervalo, aunque empiece en una franja distinta. |
| **RB12 — Inasistencia** | `NO_ASISTIO` solo puede marcarse sobre una reserva `CONFIRMADA`, desde su hora de inicio y hasta 2 horas después. Fuera de esa ventana, solo el administrador. No genera reembolso: el pago permanece `APROBADO`. |
| **RB13 — Bloqueo de agenda** | Un empleado no puede crear un bloqueo sobre un intervalo que contenga reservas en estado `PENDIENTE_PAGO`, `CONFIRMADA` o `EN_PROCESO`. |
| **RB14 — Antelación de reserva** | Servicios por franja: entre 2 horas y 30 días de antelación. Hospedaje: entre 24 horas y 60 días. |
| **RB15 — Hospedaje** | Capacidad de 10 mascotas por día. Check-in y check-out entre 08:00 y 16:00. Se cobran todos los días del rango seleccionado, incluido el día de salida. Sin empleado asignado; el recurso es el cupo diario. |
| **RB16 — Liberación de promoción** | Si una reserva que consumió una promoción pasa a `EXPIRADA` o `CANCELADA`, el uso se libera y el cliente puede volver a aplicar el código si sigue cumpliendo las demás condiciones. |

---

## 3. Re-estimación: qué cambia y por qué

El cliente pidió explícitamente que no se muevan story points para cuadrar números. **Se modifican seis estimaciones, tres al alza y tres a la baja, con un efecto neto de +2 SP.** Que el saldo sea positivo es la prueba de que el ajuste responde al contenido y no a la conveniencia: si el objetivo fuera cuadrar, todas habrían bajado.

| ID | Historia | Antes | Ahora | Δ | Justificación |
|---|---|:--:|:--:|:--:|---|
| **HU-031** | Consultar disponibilidad | 5 | **8** | **+3** | **Mi estimación original era incorrecta.** Asumía que una reserva ocupaba una franja, de modo que la disponibilidad se resolvía comparando franjas. DEC-C23 establece que un servicio ocupa varias franjas: peluquería (120 min) ocupa cuatro. Eso convierte el cálculo en aritmética de intervalos solapados sobre una rejilla, con el caso adicional de que el intervalo no exceda el cierre de jornada. Es un algoritmo distinto y más costoso, no el mismo con un parámetro. |
| **HU-033** | Unicidad bajo concurrencia | 3 | **5** | **+2** | **Consecuencia directa del cambio anterior.** Con una reserva por franja, una restricción `UNIQUE (empleado_id, inicio)` bastaba. Con ocupación por intervalos, la unicidad no es expresable como restricción de columna: exige detección de solapamiento con bloqueo transaccional sobre el rango del empleado, más la prueba de concurrencia correspondiente. |
| **HU-026** | Cambiar estado de la reserva | 3 | **4** | **+1** | DEC-C18 añade una ventana temporal (desde la hora de inicio hasta +2 h) y el escalado al administrador fuera de ella. Es lógica de negocio adicional con sus propios casos límite y pruebas. |
| **HU-024** | Bloquear franjas de la agenda | 3 | **2** | **−1** | DEC-C16 simplifica la historia a una validación de rechazo. Desaparecen las ramas de cancelación en cascada y reasignación que la estimación original contemplaba como posibles. |
| **HU-057** | Consultar la bitácora de auditoría | 3 | **2** | **−1** | DEC-C21 la reduce a una tabla simple con tres filtros. La estimación original contemplaba una pantalla con filtros combinables y paginación avanzada. |
| **HU-067** | Reservar hospedaje por rango | 8 | **6** | **−2** | DEC-C15 elimina la asignación de empleado y DEC-C14 fija la capacidad en un valor único. Desaparece la intersección entre disponibilidad de empleado y disponibilidad de cupo, que era la parte cara de la estimación original. |
| | **Efecto neto** | | | **+2** | Backlog total: 243 → **245 SP** |

**Cambios de prioridad** (sin efecto sobre los puntos):

| ID | Historia | Antes | Ahora | Motivo |
|---|---|:--:|:--:|---|
| HU-011 | Verificación de correo | Should | **Must** | DEC-C20 |
| HU-012 | Recuperación de contraseña | Should | **Must** | DEC-C20 |
| HU-061 | Infraestructura de correo | Must | **Must** | Confirmado por DEC-C20 |
| HU-013 | Cambio de contraseña | Should | **Should** | Confirmado |
| HU-053 | Liberar uso de promoción | Won't (V2) | **Should** (diferido) | DEC-C26 lo incorpora al alcance deseado |

---

## 4. Capacidad recalculada

### 4.1 El error del modelo anterior

La estimación de la v1 usaba una tasa única de 2 h/SP para todo el trabajo. Eso trata igual a "CRUD de mascotas" y a "webhook idempotente con validación de firma", lo cual es falso en ambas direcciones: sobreestima el trabajo rutinario e infraestima el novedoso.

El modelo revisado separa dos tipos de trabajo:

| Tipo | Qué incluye | Tasa | Justificación |
|---|---|:--:|---|
| **Conocido** | CRUD, pantallas, formularios, validaciones, autenticación estándar con Spring Security, consultas JPA | **1,25 h/SP** | Es trabajo que el desarrollador ya ha hecho en este mismo stack (Spring Boot + React + MySQL). La tasa de 2 h/SP correspondía a un desarrollador arrancando en frío |
| **Nuevo** | Motor de disponibilidad, concurrencia, integración de pasarela, webhook, planificador de tareas, cupos de hospedaje | **2,50 h/SP** | Superior a la estimación original, porque es donde de verdad se pierde el tiempo: depuración, casos límite, documentación de terceros |

### 4.2 Capacidad del MVP comprometido

| Concepto | Cálculo | Valor |
|---|---|---:|
| Tiempo bruto | 8 semanas × 15 h | 120 h |
| − Ceremonias (45 min/sprint) | | −6 h |
| − Contingencia (20 %) | | −23 h |
| **Horas efectivas** | | **91 h** |
| SP de trabajo conocido en el nivel 1 | | 68 SP |
| SP de trabajo nuevo en el nivel 1 | | 34 SP |
| **Horas requeridas** | (68 × 1,25) + (34 × 2,50) | **170 h** |
| **Déficit** | 170 − 91 | **−79 h** |

**El modelo revisado no cierra la brecha.** Con las tasas mejor calibradas, el MVP comprometido de 102 SP sigue requiriendo casi el doble de horas de las disponibles. Esto es lo que hay, y conviene decirlo antes de que el cliente apruebe: el plan no se sostiene sobre una estimación optimista sino sobre una apuesta explícita, que es lo que desarrolla la sección siguiente.

---

## 5. El compromiso y su condición

### 5.1 El número honesto

| Métrica | Valor |
|---|---:|
| MVP comprometido | 102 SP |
| Sprints disponibles | 8 |
| **Velocidad requerida** | **12,75 SP/sprint** |
| Velocidad según la estimación conservadora | 5,6 SP/sprint |
| **Relación** | **2,3×** |
| Horas por SP que implica | **0,89 h/SP** |

Dicho sin rodeos: **entregar este alcance exige completar un story point cada 53 minutos, sostenido durante ocho semanas.**

### 5.2 Por qué creo que es alcanzable — y por qué no lo garantizo

**A favor:**

- La estimación de 2 h/SP se calibró para un desarrollador sin experiencia previa en el stack. No es el caso: hay experiencia real construyendo y desplegando aplicaciones Spring Boot + React + MySQL.
- El 67 % del nivel 1 (68 de 102 SP) es trabajo conocido: autenticación, CRUD, formularios, listados. Ahí una hora por punto es realista.
- Todas las decisiones funcionales están cerradas. Buena parte del tiempo que se pierde en proyectos de este tamaño se va en esperar respuestas o rehacer lo que se construyó sobre un supuesto equivocado. Eso ya está resuelto, y es precisamente lo que compraron las dos rondas de preguntas.

**En contra:**

- Los 34 SP de trabajo nuevo (disponibilidad, concurrencia, webhook, planificador) son exactamente los que no admiten atajos, y son el 33 % del alcance.
- No hay ninguna medición real de velocidad. Todo lo anterior es argumento, no evidencia.

### 5.3 La forma del compromiso

Un compromiso honesto sobre una velocidad no medida no puede ser una cifra única. Toma esta forma:

| Elemento | Contenido |
|---|---|
| **Compromiso firme** | El ciclo reservar → pagar → confirmar, desplegado y accesible por URL. Son las 25 historias marcadas 🔒 en la sección 7 (**82 SP**) |
| **Objetivo** | Las 32 historias del nivel 1 (**102 SP**) |
| **Diferencia** | 7 historias / 20 SP que se degradan al nivel 2 en el orden pre-acordado de la sección 9, sin renegociación |
| **Punto de control** | Al cerrar el **Sprint 2** (semana 3), con la velocidad real de S0–S2 |

### 5.4 El punto de control

Al terminar el Sprint 2 se calcula `velocidad real = SP completados en S0+S1+S2 ÷ 3` y se actúa **sin volver a negociar**:

| Velocidad real | Acción |
|---|---|
| **≥ 12,75** | El objetivo se mantiene. Se sigue el plan de la sección 8 |
| **10,5 – 12,75** | Se aplican los escalones 1 a 3 de la degradación (−7 SP). Objetivo revisado: 95 SP |
| **8 – 10,5** | Se aplican los siete escalones (−20 SP). Se entrega el compromiso firme: 82 SP |
| **< 8** | El compromiso firme tampoco cabe. Se escala al cliente con dos opciones cuantificadas: 2 semanas adicionales, o reducir el nivel 1 al ciclo sin cancelación ni expiración |

Medir en tres sprints y no en uno evita tomar una decisión sobre el ruido del arranque, que siempre es el sprint menos representativo.

### 5.5 Lo que pido al cliente

Tres elementos de su lista de irrenunciables quedan en el nivel 2. Este es su precio exacto, por si el cliente prefiere pagarlo antes que aplazarlos:

| Recuperar | SP | Coste |
|---|:--:|---|
| Rol Empleado operativo (agenda, estados, bloqueo, ficha de mascota) | 14 | +1 sprint, o +7 h/semana |
| Correo transaccional + verificación + recuperación de contraseña | 9 | +1 sprint |
| Auditoría (registro transaccional + consulta) | 7 | +1 sprint |
| **Los tres** | **30** | **+2,5 sprints → 11 semanas**, o mantener 8 semanas con **+23 h/semana** |

Mi recomendación, si hay margen: **ampliar a 10 semanas.** Con dos sprints más, el nivel 1 pasa de 12,75 a 10,2 SP/sprint de velocidad requerida y entran el rol Empleado y el correo. Es la diferencia entre un plan que necesita que todo salga bien y uno que tolera una semana mala.

---

## 6. Los tres niveles de alcance

### 6.1 Nivel 1 — MVP comprometido · 32 historias · 102 SP

Lo que el cliente recibe al final de la semana 8.

| Área | Qué incluye |
|---|---|
| **Cuentas** | Registro de cliente con autorización de tratamiento de datos, login con JWT (access + refresh), autorización por rol y permiso en todos los endpoints, cierre de sesión con revocación |
| **Mascotas** | Registrar, listar, editar y eliminar mascotas propias, con validación de propiedad |
| **Servicios** | CRUD administrativo, catálogo público con precios, desactivación sin afectar reservas confirmadas |
| **Empleados** | Jornada laboral semanal y servicios que presta cada uno — lo mínimo que el motor de disponibilidad necesita |
| **Disponibilidad** | Rejilla de 30 minutos, ocupación multi-franja (RB11), descuento de reservas y bloqueos, validación de antelación (RB14) |
| **Reservas** | Creación con validaciones RB01, RB02, RB10, RB11; unicidad garantizada en base de datos bajo concurrencia; elección de empleado o "sin preferencia" (DEC-C24); máquina de estados; historial del cliente; cancelación bajo RB06 |
| **Pagos** | `PaymentGateway` con adaptador simulado, orden de pago con total congelado, checkout, webhook con firma, idempotencia y validación de monto, confirmación RB03, expiración a 15 min RB05 |
| **Despliegue** | Público, con HTTPS y variables de entorno, desde el Sprint 0 |
| **Calidad** | Pruebas unitarias y de servicio en la *Definition of Done* de cada historia; responsive básico (360 px y 1280 px) en la DoD de cada sprint |

### 6.2 Nivel 2 — MVP diferido · 37 historias · 119 SP

Estaba en el MVP declarado. No cabe en 8 semanas. Segunda entrega.

| Bloque | Historias | SP |
|---|---|:--:|
| Rol Empleado operativo (agenda, estados, bloqueo, ficha de mascota, CRUD) | HU-020, HU-023, HU-024, HU-025, HU-026 | 14 |
| Correo transaccional, verificación y recuperación de contraseña | HU-011, HU-012, HU-013, HU-061, HU-062, HU-063 | 14 |
| Pagos: reintento, estado, reembolsos, integración real | HU-043, HU-045, HU-046, HU-047, HU-048, HU-049 | 19 |
| Promociones completas | HU-050, HU-051, HU-052, HU-053 | 15 |
| Panel administrativo y auditoría | HU-054, HU-055, HU-056, HU-057, HU-058 | 18 |
| Hospedaje | HU-066, HU-067, HU-068 | 16 |
| Perfil, integración continua, bloqueo por intentos | HU-006, HU-014, HU-015 | 6 |
| Calidad adicional y documentación | HU-070, HU-071, HU-072, HU-074, HU-076 | 17 |
| **Total** | | **119** |

### 6.3 Nivel 3 — V2 · 7 historias · 24 SP

Acordado fuera desde el principio: reprogramación, reservas recurrentes, reembolso automático, precios dinámicos, promociones complejas, gráficas del dashboard, recordatorios programados, avisos al empleado, ocupación del hospedaje, creación de reservas por el administrador, eliminación de cuenta con anonimización y prueba de carga.

### 6.4 Trazabilidad contra la lista de irrenunciables del cliente

| Irrenunciable declarado | Nivel | Nota |
|---|:--:|---|
| Registro e inicio de sesión | 1 ✅ | |
| Roles y autorización | 1 ✅ | |
| Gestión de mascotas | 1 ✅ | |
| Catálogo de servicios | 1 ✅ | |
| Consulta de disponibilidad | 1 ✅ | |
| Creación de reservas | 1 ✅ | |
| Validación de reservas duplicadas/conflictivas | 1 ✅ | |
| Estados de reserva | 1 ✅ | Las transiciones que dispara el empleado quedan en nivel 2 |
| Cancelación bajo la regla de 24 horas | 1 ✅ | |
| Pago electrónico | 1 ✅ | Con adaptador simulado (DEC-C13) |
| Confirmación del pago por webhook | 1 ✅ | |
| Manejo de pagos rechazados | **2** ⚠️ | El estado se registra; el reintento guiado es nivel 2 |
| Expiración a los 15 minutos | 1 ✅ | |
| Gestión administrativa básica | **2** ⚠️ | |
| Gestión de empleados | **2** ⚠️ | Jornada y servicios sí están en nivel 1; el CRUD y la agenda no |
| Gestión de servicios | 1 ✅ | |
| Historial de reservas/pagos | 1 ✅ | Historial de reservas. El de pagos es nivel 2 |
| Seguridad de contraseñas | 1 ✅ | BCrypt. La recuperación es nivel 2 |
| Auditoría de operaciones importantes | **2** ⚠️ | |
| Despliegue funcional | 1 ✅ | |

**16 de 20 irrenunciables en el nivel 1. Cuatro en el nivel 2**, con su precio en la sección 5.5.

---

## 7. Product Backlog v2

**Columnas:** ID · Historia · Prioridad MoSCoW · Story Points · Sprint · Estado.
**Nivel:** 1 = comprometido · 2 = diferido · 3 = V2.
🔒 marca las historias del **compromiso firme** (se entregan aunque la velocidad sea menor de la esperada).

### E0 — Fundación y arquitectura

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-001 🔒 | Estructura modular de Spring Boot con Docker Compose, Flyway y perfiles de entorno | 1 | M | 3 | S0 | Pendiente |
| HU-002 🔒 | Proyecto React con Tailwind, router y cliente Axios con interceptor de JWT | 1 | M | 2 | S0 | Pendiente |
| HU-003 🔒 | Comparar Wompi, Mercado Pago y PayU y documentar la elección *(timebox 4 h)* | 1 | M | 2 | S0 | Pendiente |
| HU-004 🔒 | Puerto `PaymentGateway` con adaptador simulado | 1 | M | 3 | S3 | Pendiente |
| HU-005 🔒 | Despliegue público mínimo funcionando | 1 | M | 3 | S0 | Pendiente |
| HU-006 | Integración continua: build y pruebas en cada push | 2 | C | 2 | — | Pendiente |

### E1 — Autenticación y autorización

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-007 🔒 | Registrarme con mis datos y aceptar la política de tratamiento | 1 | M | 3 | S1 | Pendiente |
| HU-008 🔒 | Iniciar sesión y recibir token de acceso y de refresco | 1 | M | 3 | S1 | Pendiente |
| HU-009 🔒 | Autorizar cada endpoint según rol y permisos | 1 | M | 3 | S1 | Pendiente |
| HU-010 🔒 | Cerrar sesión con revocación del token de refresco | 1 | M | 2 | S1 | Pendiente |
| HU-011 | Verificar mi correo tras registrarme | 2 | M | 3 | — | Pendiente |
| HU-012 | Recuperar mi contraseña por correo | 2 | M | 3 | — | Pendiente |
| HU-013 | Cambiar mi contraseña estando autenticado | 2 | S | 1 | — | Pendiente |
| HU-014 | Bloqueo temporal tras 5 intentos fallidos | 2 | C | 2 | — | Pendiente |

### E2 — Cliente y mascotas

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-015 | Ver y editar los datos de mi perfil | 2 | S | 2 | — | Pendiente |
| HU-016 🔒 | Registrar una mascota con sus datos | 1 | M | 3 | S1 | Pendiente |
| HU-017 🔒 | Listar, editar y eliminar mis mascotas, con validación de propiedad | 1 | M | 2 | S2 | Pendiente |
| HU-018 🔒 | Interfaz de gestión de mascotas | 1 | M | 3 | S2 | Pendiente |
| HU-019 | Eliminar mi cuenta con anonimización de datos | 3 | W | 3 | — | Pendiente |

### E3 — Empleados y agenda

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-020 | Crear, editar y desactivar empleados desde el panel | 2 | M | 3 | — | Pendiente |
| HU-021 | Asignar a cada empleado los servicios que puede prestar | 1 | M | 2 | S2 | Pendiente |
| HU-022 🔒 | Definir la jornada laboral semanal de cada empleado | 1 | M | 3 | S3 | Pendiente |
| HU-023 | Consultar mi agenda y las reservas que tengo asignadas | 2 | M | 3 | — | Pendiente |
| HU-024 | Bloquear franjas de mi agenda (RB13) | 2 | M | 2 | — | Pendiente |
| HU-025 | Consultar la información de la mascota que voy a atender | 2 | S | 2 | — | Pendiente |
| HU-026 | Cambiar el estado de mis reservas, con la ventana de RB12 | 2 | M | 4 | — | Pendiente |

### E4 — Servicios

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-027 | Crear y editar servicios con precio, duración, unidad de cobro y estado | 1 | M | 3 | S2 | Pendiente |
| HU-028 🔒 | Consultar el catálogo de servicios con sus precios | 1 | M | 2 | S2 | Pendiente |
| HU-029 | Desactivar un servicio sin afectar las reservas confirmadas | 1 | M | 1 | S2 | Pendiente |

### E5 — Disponibilidad y reservas

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-030 🔒 | Generar las franjas de 30 min a partir de la jornada laboral | 1 | M | 3 | S3 | Pendiente |
| HU-031 🔒 | Consultar franjas disponibles con ocupación multi-franja (RB11, RB14) | 1 | M | 8 | S4 | Pendiente |
| HU-032 🔒 | Crear una reserva validando RB01, RB02, RB10 y RB11 | 1 | M | 5 | S5 | Pendiente |
| HU-033 🔒 | Garantizar en base de datos que no haya solapamientos bajo concurrencia | 1 | M | 5 | S5 | Pendiente |
| HU-034 🔒 | Asistente de reserva guiado por servicio, mascota, fecha, hora, empleado y resumen | 1 | M | 5 | S6 | Pendiente |
| HU-035 🔒 | Máquina de estados con transiciones válidas | 1 | M | 3 | S3 | Pendiente |
| HU-036 | Listar mis reservas y ver el detalle de cada una | 1 | M | 3 | S6 | Pendiente |
| HU-037 | Cancelar una reserva hasta 24 horas antes (RB06) | 1 | M | 3 | S6 | Pendiente |
| HU-038 | Elegir empleado o indicar "sin preferencia" (DEC-C24) | 1 | M | 3 | S4 | Pendiente |

### E6 — Pagos

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-039 🔒 | Generar la orden de pago con el total congelado | 1 | M | 3 | S5 | Pendiente |
| HU-040 🔒 | Redirigir al checkout y volver a la plataforma | 1 | M | 3 | S6 | Pendiente |
| HU-041 🔒 | Recibir el webhook validando firma, idempotencia y monto | 1 | M | 5 | S7 | Pendiente |
| HU-042 🔒 | Confirmar la reserva al aprobarse el pago (RB03) | 1 | M | 2 | S7 | Pendiente |
| HU-043 | Reintentar el pago rechazado (RB04) | 2 | M | 3 | — | Pendiente |
| HU-044 | Expirar las reservas no pagadas a los 15 minutos (RB05) | 1 | M | 5 | S7 | Pendiente |
| HU-045 | Consultar el estado de mis pagos | 2 | M | 2 | — | Pendiente |
| HU-046 | Generar `SOLICITUD_REEMBOLSO` al cancelar una reserva pagada (RB07) | 2 | M | 3 | — | Pendiente |
| HU-047 | Ver las solicitudes de reembolso y marcarlas como `REEMBOLSADO` | 2 | M | 3 | — | Pendiente |
| HU-048 | Configurar credenciales y entorno de la pasarela elegida | 2 | M | 3 | — | Pendiente |
| HU-049 | Integrar el checkout y el webhook reales del proveedor | 2 | M | 5 | — | Pendiente |

### E7 — Promociones

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-050 | Crear promociones con código, porcentaje, vigencia, máximo de usos y estado | 2 | S | 5 | — | Pendiente |
| HU-051 | Aplicar un código y ver el total recalculado antes de pagar | 2 | S | 5 | — | Pendiente |
| HU-052 | Impedir el uso repetido salvo configuración del administrador (RB09) | 2 | S | 3 | — | Pendiente |
| HU-053 | Recuperar el uso de la promoción si la reserva expira o se cancela (RB16) | 2 | S | 2 | — | Pendiente |

### E8 — Panel administrativo y auditoría

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-054 | Listar reservas filtrando por cliente, mascota, servicio, empleado, fecha y estado | 2 | S | 5 | — | Pendiente |
| HU-055 | Listar pagos filtrando por cliente, fecha, método, estado y valor | 2 | S | 3 | — | Pendiente |
| HU-056 | Registrar en bitácora, en la misma transacción, toda operación importante (RNF08) | 2 | M | 5 | — | Pendiente |
| HU-057 | Consultar la bitácora filtrando por actor, entidad y rango de fechas | 2 | S | 2 | — | Pendiente |
| HU-058 | Ver los KPIs de reservas de hoy, ingresos del mes, clientes y servicios realizados | 2 | S | 3 | — | Pendiente |
| HU-059 | Ver gráficas de reservas, ingresos y servicios más vendidos | 3 | W | 5 | — | Pendiente |
| HU-060 | Crear una reserva a nombre de un cliente que llamó por teléfono | 3 | W | 5 | — | Pendiente |

### E9 — Notificaciones

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-061 | Infraestructura de correo con plantillas y registro del estado de envío | 2 | M | 3 | — | Pendiente |
| HU-062 | Recibir correo al confirmarse la reserva y al aprobarse el pago | 2 | S | 2 | — | Pendiente |
| HU-063 | Recibir correo al rechazarse el pago, expirar o cancelar la reserva | 2 | S | 2 | — | Pendiente |
| HU-064 | Recibir un recordatorio el día anterior a la reserva | 3 | W | 3 | — | Pendiente |
| HU-065 | Recibir aviso cuando me asignen o me cancelen una reserva | 3 | W | 2 | — | Pendiente |

### E10 — Hospedaje

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-066 | Definir la capacidad diaria de cupos de hospedaje (RB15) | 2 | S | 5 | — | Pendiente |
| HU-067 | Reservar hospedaje por rango de fechas con total por día (RB15) | 2 | S | 6 | — | Pendiente |
| HU-068 | Validar cupo en todo el rango y retenerlo durante los 15 min del pago | 2 | S | 5 | — | Pendiente |
| HU-069 | Ver la ocupación del hospedaje por fecha | 3 | W | 3 | — | Pendiente |

### E11 — Calidad y despliegue

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-070 | Pruebas de controlador y de endpoint sobre los flujos críticos | 2 | M | 5 | — | Pendiente |
| HU-071 | Pruebas de los componentes críticos del frontend | 2 | S | 3 | — | Pendiente |
| HU-072 | Responsive completo en móvil, tablet y escritorio (RNF04-R) | 2 | M | 5 | — | Pendiente |
| HU-073 🔒 | Despliegue final con variables de entorno y HTTPS | 1 | M | 3 | S4 | Pendiente |
| HU-074 | Documentación de la API con OpenAPI | 2 | S | 2 | — | Pendiente |
| HU-075 | Prueba de carga que valide RNF02-R | 3 | W | 3 | — | Pendiente |
| HU-076 | Manual de usuario y documentación de entrega | 2 | S | 2 | — | Pendiente |

### 7.1 Resumen

| Nivel | Historias | SP | Must | Should | Could | Won't |
|---|:--:|:--:|:--:|:--:|:--:|:--:|
| 1 — Comprometido | 32 | 102 | 102 | 0 | 0 | 0 |
| 2 — Diferido | 37 | 119 | 55 | 60 | 4 | 0 |
| 3 — V2 | 7 | 24 | 0 | 0 | 0 | 24 |
| **Total** | **76** | **245** | **157** | **60** | **4** | **24** |

---

## 8. Plan de sprints rebalanceado

**102 SP en 8 sprints. Media 12,75. Máximo 14. Ningún sprint por encima del 10 % de la media.** El Sprint 7 queda en 12 SP como colchón deliberado, porque es donde históricamente se acumula el retraso de todo lo anterior.

| Sprint | Objetivo demostrable | Historias | SP |
|:--:|---|---|---:|
| **S0** | *"La aplicación existe, está desplegada en una URL pública y sé con qué pasarela voy a trabajar."* | HU-001, HU-002, HU-003, HU-005 | 10 |
| **S1** | *"Un cliente se registra, inicia sesión y registra su mascota. Los endpoints están protegidos por rol."* | HU-007, HU-008, HU-009, HU-010, HU-016 | 14 |
| **S2** | *"El cliente gestiona sus mascotas y ve el catálogo de servicios con precios reales."* | HU-017, HU-018, HU-021, HU-027, HU-028, HU-029 | 13 |
| **S3** | *"El sistema conoce la jornada de cada empleado y genera su rejilla de franjas. El puerto de pagos está listo."* | HU-004, HU-022, HU-030, HU-035 | 12 |
| **S4** | *"El sistema muestra la disponibilidad real de cada servicio, con ocupación multi-franja, y el despliegue está endurecido."* | HU-031, HU-038, HU-073 | 14 |
| **S5** | *"Se crea una reserva válida con su orden de pago, y dos usuarios simultáneos no pueden ocupar la misma franja."* | HU-032, HU-033, HU-039 | 13 |
| **S6** | *"El cliente reserva desde la interfaz, ve su historial, cancela bajo la regla de 24 h y llega al checkout."* | HU-034, HU-036, HU-037, HU-040 | 14 |
| **S7** | *"El webhook confirma la reserva y las no pagadas expiran solas. El ciclo está cerrado."* | HU-041, HU-042, HU-044 | 12 |
| | | **Total** | **102** |

### 8.1 Decisiones de planificación que conviene explicar

- **HU-073 (despliegue final) se adelanta al Sprint 4.** En la v1 estaba en el último sprint. Es un error: el webhook del Sprint 7 necesita un endpoint público con HTTPS funcionando, y descubrir en la semana 8 que el certificado o las variables de entorno no están bien es perder el proyecto. Endurecer el despliegue a mitad de camino es mejor práctica, y además libera 3 SP del sprint final.
- **HU-035 (máquina de estados) se adelanta al Sprint 3.** No depende de que existan reservas: es el modelo de transiciones. Construirlo antes hace que el Sprint 5 solo tenga que usarlo.
- **HU-037 (cancelación) sube al Sprint 6.** Solo necesita reservas y máquina de estados, ambas disponibles desde el Sprint 5. Sacarla del sprint final reduce el riesgo de que se caiga.
- **El Sprint 4 es el de mayor riesgo técnico**, con HU-031 a 8 SP. Conviene empezarlo con el algoritmo escrito en pseudocódigo y los casos límite enumerados antes de tocar el teclado. Si un sprint se va a desbordar, es este.
- **El Sprint 7 tiene 12 SP y no 26.** Eso responde a DEC-C29: el colchón está en el último sprint, que es donde hace falta.

---

## 9. Escalera de degradación pre-acordada

Si el punto de control de la semana 3 muestra una velocidad inferior a la requerida, estas historias pasan del nivel 1 al nivel 2 **en este orden y sin renegociación**. Aprobar este documento es aprobar también esta escalera.

| # | Historia | SP | Sprint | Qué se pierde |
|:--:|---|:--:|:--:|---|
| 1 | HU-038 — Elegir empleado | 3 | S4 | El sistema asigna siempre automáticamente. El cliente no elige profesional |
| 2 | HU-029 — Desactivar servicio sin afectar confirmadas | 1 | S2 | La desactivación queda como borrado lógico simple |
| 3 | HU-036 — Listado de reservas del cliente | 3 | S6 | Se reduce a una lista mínima sin pantalla de detalle |
| 4 | HU-021 — Servicios por empleado | 2 | S2 | Todos los empleados prestan todos los servicios |
| 5 | HU-027 — CRUD de servicios | 3 | S2 | Los cinco servicios se cargan por migración de datos |
| 6 | HU-037 — Cancelación bajo RB06 | 3 | S6 | **Se incumple RB06.** Requiere aprobación expresa del cliente |
| 7 | HU-044 — Expiración a los 15 minutos | 5 | S7 | **Se incumple RB05.** Las franjas quedan bloqueadas indefinidamente. Requiere aprobación expresa |
| | **Total degradable** | **20** | | Compromiso firme resultante: **82 SP** |

Los escalones **6 y 7 rompen reglas de negocio** y por eso están al final: solo se aplican con aprobación expresa del cliente en el momento. Los escalones 1 a 5 son degradaciones de experiencia de usuario, no de reglas, y se aplican automáticamente.

**Las 25 historias marcadas 🔒 en la sección 7 no se degradan nunca**, porque sin ellas no hay producto que enseñar: son la fundación, la autenticación, las mascotas, el catálogo, el motor de disponibilidad, la creación de reservas con su garantía de concurrencia, el asistente de reserva, la cadena completa de pago y el despliegue.

---

## 10. Criterios de aceptación nuevos

Los de HU-031, HU-032, HU-033, HU-037, HU-041 y HU-044 están en BKL-PETCARE-001 §11 y siguen vigentes, con una corrección y tres añadidos derivados de la ronda 2.

### HU-031 — Disponibilidad con ocupación multi-franja *(corrige la versión anterior)*

- La rejilla es de 30 minutos, generada desde la jornada del empleado (RB11, DEC-C23).
- Una franja de inicio `t` es ofrecible solo si el intervalo completo `[t, t + duración del servicio]` está libre. Una reserva de peluquería a las 10:00 inhabilita como inicio las franjas 10:00, 10:30, 11:00 y 11:30, y también las anteriores cuyo intervalo alcanzaría las 10:00.
- Una franja no es ofrecible si `t + duración` supera el cierre de la jornada. Con peluquería (120 min) y cierre a las 18:00, el último inicio ofrecible es 16:00.
- Se descartan las franjas que caigan dentro de un bloqueo activo (RB13).
- Se descartan las franjas anteriores a `ahora + 2 h` o posteriores a `hoy + 30 días` (RB14).
- Domingos y días sin jornada devuelven lista vacía, no error.

### HU-026 — Cambiar estado de la reserva *(nuevo)*

- El empleado solo opera sobre reservas asignadas a él.
- `CONFIRMADA → EN_PROCESO` y `EN_PROCESO → COMPLETADA` están permitidas para el empleado asignado.
- `CONFIRMADA → NO_ASISTIO` solo es posible entre la hora de inicio de la reserva y dos horas después (RB12). Antes de la hora de inicio, el sistema responde `409`. Pasadas las dos horas, responde `403` indicando que debe intervenir un administrador.
- Marcar `NO_ASISTIO` **no** modifica el pago: permanece `APROBADO` y no se genera solicitud de reembolso (DEC-C17).

### HU-024 — Bloquear franjas de la agenda *(nuevo)*

- El empleado solo bloquea su propia agenda.
- **Dado** que el intervalo contiene una reserva en `PENDIENTE_PAGO`, `CONFIRMADA` o `EN_PROCESO`, **cuando** intenta bloquearlo, **entonces** el sistema responde `409`, no crea el bloqueo e informa cuántas reservas lo impiden y en qué horas (RB13).
- Un bloqueo creado excluye el intervalo de la disponibilidad desde ese momento.

### HU-067 — Reservar hospedaje *(nuevo, nivel 2)*

- El cliente indica fecha de entrada y de salida; ambas dentro del rango de antelación de RB14 (24 h a 60 días).
- El total es `número de días del rango × precio diario`, contando entrada y salida. Entrada lunes y salida jueves = 4 días (DEC-C14).
- La reserva se crea solo si hay cupo disponible **en todos** los días del rango.
- Durante los 15 minutos del pago, los cupos quedan **retenidos**, no ocupados, y el job de expiración los libera.
- No se asigna empleado (DEC-C15).

---

## 11. Problema de diseño detectado: la rejilla de 30 minutos

DEC-C22 y DEC-C23 juntas producen un desajuste que conviene resolver antes del Sprint 4, no durante.

**Las duraciones no son múltiplos de la franja.** Paseo (60), baño (90) y peluquería (120) sí lo son. **Veterinaria, con 45 minutos, no.** Una consulta a las 10:00 termina a las 10:45 y deja quince minutos huérfanos en la franja de 10:30, que no sirven para nada porque ningún servicio dura menos de 45 minutos.

Tres formas de resolverlo:

| Opción | Cómo funciona | Consecuencia |
|---|---|---|
| **A — Redondear la ocupación al alza** *(recomendada)* | La consulta ocupa 60 min de agenda aunque dure 45. El intervalo bloqueado es `[10:00, 11:00]` | Simple, predecible, sin huecos inutilizables. Se pierden 15 min de agenda por consulta, que en la práctica funcionan como margen entre pacientes — algo que cualquier veterinaria necesita de todos modos |
| **B — Rejilla de 15 minutos** | Todas las duraciones son múltiplos de 15 | Duplica el número de franjas a calcular y mostrar. Más carga y una interfaz más densa, a cambio de un aprovechamiento marginalmente mejor |
| **C — Ocupación exacta** | El intervalo es exactamente `[10:00, 10:45]` | Genera huecos de 15 min imposibles de vender y complica la interfaz, que debe mostrar inicios fuera de rejilla |

**Recomendación: opción A**, con `duracionAgendaMinutos` calculado como la duración redondeada al alza al múltiplo de franja. Requiere confirmación (PRG-70).

---

## 12. Preguntas abiertas de tercera ronda

Solo quedan tres, y ninguna bloquea el Sprint 0.

| ID | Pregunta | Prio | Bloquea |
|---|---|:--:|---|
| **PRG-68** | Hospedaje: entrada lunes y salida jueves se cobra como 4 días, pero la mascota pasa 3 noches. La convención hotelera cobra noches. ¿Confirma que quiere cobrar también el día de salida? Afecta a lo que el cliente final espera pagar y conviene que el texto de la reserva lo diga con claridad | P1 | Nivel 2 |
| **PRG-69** | Hospedaje: si el check-out es a las 16:00 del jueves, ¿el cupo del jueves queda libre ese mismo día para otra mascota, o se considera ocupado hasta el viernes? | P1 | Nivel 2 |
| **PRG-70** | ¿Acepta la opción A de la sección 11 (redondear al alza la ocupación de agenda, de modo que veterinaria ocupe 60 min)? | P0 | Sprint 4 |

---

## 13. Modelo de datos actualizado

Cambios sobre BKL-PETCARE-001 §12, derivados de la ronda 2:

| Entidad | Cambio | Motivo |
|---|---|---|
| `Servicio` | + `duracionAgendaMinutos` (duración redondeada al múltiplo de franja) | Sección 11, opción A |
| `Servicio` | `unidadCobro ∈ {POR_SERVICIO, POR_DIA}` — confirmado | DEC-C14, CN-01 |
| `Servicio` | `modalidadAgenda ∈ {POR_FRANJA, POR_DIAS}` — confirmado | DEC-C15, CN-02 |
| `Reserva` | `empleado` es **nulo** cuando `modalidadAgenda = POR_DIAS` | DEC-C15 |
| `Reserva` | + `inicio`, `fin` (instantes UTC) — la unicidad se evalúa por solapamiento de intervalos, no por igualdad de franja | RB11, HU-033 |
| `CupoHospedaje` | `capacidad` por defecto 10; `retenidos` distinto de `ocupados` | DEC-C14, CN-05 |
| `BloqueoAgenda` | Restricción: no puede crearse solapando reservas activas | RB13 |
| `Reserva` | + `marcadoNoAsistioPor`, `marcadoNoAsistioEn` | RB12, auditoría |
| `UsoPromocion` | + `liberadoEn` (nulo mientras el uso siga vigente) | RB16 |
| `ParametroSistema` | **Nueva.** Tamaño de franja, antelación mínima y máxima, capacidad de hospedaje, minutos de expiración | Evita recompilar para ajustar reglas |

---

## 14. Riesgos v2

| ID | Riesgo | Estado |
|---|---|---|
| **RSG-02** Alcance excesivo | 🟡 **Gestionado, no eliminado.** DEC-C28 reduce el compromiso de 217 a 102 SP. Sigue exigiendo 2,3× la velocidad conservadora, pero ahora tiene punto de control y escalera de degradación acordados |
| **RSG-20** Velocidad real desconocida | 🟠 **Es ahora el riesgo principal.** Todo el plan depende de una velocidad no medida. Mitigación: punto de control al cerrar el Sprint 2 y degradación automática |
| **RSG-01** Pasarela | 🟢 **Cerrado por DEC-C13.** El desarrollo no depende de credenciales externas. Queda la obligación de documentar que la integración real está pendiente |
| **RSG-03** Concurrencia | 🟠 **Agravado.** La ocupación multi-franja impide resolverlo con una restricción `UNIQUE` simple. Requiere detección de solapamiento con bloqueo transaccional. Por eso HU-033 subió de 3 a 5 SP |
| **RSG-04** Carrera entre expiración y confirmación | 🟠 Sin cambios. Sigue siendo el caso límite más delicado del Sprint 7 |
| **RSG-05** Correo en spam | 🟢 **Fuera del nivel 1.** El correo pasa al nivel 2; el riesgo se aplaza con él |
| **RSG-07** Hospedaje | 🟢 **Neutralizado en esta entrega.** Pasa íntegro al nivel 2 |
| **RSG-09** Despliegue en capa gratuita | 🟠 Sin cambios, pero mitigado antes: HU-073 se adelanta al Sprint 4 |
| **RSG-21** Bloqueo sobre reservas confirmadas | 🟢 **Cerrado por DEC-C16** |
| **RSG-22** *(nuevo)* **Entrega sin integración real de pagos** | 🟡 El MVP demuestra el ciclo con un adaptador simulado. Si el evaluador esperaba una integración productiva, hay un desajuste de expectativas. Mitigación: DEC-C13 exige documentarlo de forma explícita y visible en la entrega, no en una nota al pie |
| **RSG-23** *(nuevo)* **Cuatro irrenunciables en el nivel 2** | 🟠 El cliente declaró irrenunciables el rol Empleado, la auditoría, la gestión administrativa y el manejo de pagos rechazados, y los cuatro quedan diferidos. Mitigación: sección 5.5 pone el precio exacto de recuperarlos; la decisión es del cliente, no del equipo |

---

## 15. Declaración de entrega

Al término de la semana 8, el cliente recibe:

**Una plataforma web desplegada en una URL pública, con HTTPS, donde:**

- Un cliente crea su cuenta, inicia sesión y su sesión está protegida con JWT y autorización por rol.
- Registra, edita y elimina sus mascotas, y no puede operar sobre las de otro cliente.
- Consulta el catálogo de los cinco servicios con sus precios reales.
- Consulta la disponibilidad real de un servicio en una fecha, calculada sobre la jornada de cada empleado, en franjas de 30 minutos y respetando que un servicio ocupa varias franjas.
- Reserva eligiendo un empleado disponible o indicando "sin preferencia".
- El sistema impide reservar para una mascota ajena, sobre un empleado ocupado, sobre una mascota con otra reserva solapada, o fuera del rango de antelación — y lo garantiza en la base de datos, con una prueba automatizada de 20 peticiones concurrentes que demuestra que exactamente una tiene éxito.
- Paga a través del checkout de la pasarela y su reserva se confirma cuando llega el webhook, validado por firma, idempotente y con el monto contrastado contra la orden.
- Si no paga en 15 minutos, la reserva expira y la franja vuelve a estar disponible.
- Consulta su historial de reservas y cancela cualquiera de ellas hasta 24 horas antes.
- Todo funciona en móvil y en escritorio.

**Lo que el cliente NO recibe en esta entrega**, y sabe de antemano que no recibe:

- La integración con una pasarela real. El ciclo funciona de extremo a extremo contra un adaptador simulado, con toda la lógica de firma, idempotencia y validación de monto implementada y probada. **Esto no debe presentarse como una integración productiva** (DEC-C13).
- El rol Empleado operativo: agenda propia, cambio de estados y bloqueo de franjas.
- Correo transaccional, verificación de correo y recuperación de contraseña.
- Hospedaje.
- Promociones.
- Panel administrativo, KPIs y auditoría.
- Reembolsos.

**Y sabe cuánto cuesta cada una de esas cosas**, en la sección 5.5.

---

## Registro de cambios

| Versión | Fecha | Autor | Cambios |
|---|---|---|---|
| 1.0 | 2026-09-15 | Ronald Moreno | Backlog inicial de 243 SP en un solo nivel |
| 2.0 | 2026-09-16 | Ronald Moreno | Ronda 2 de decisiones integrada. Backlog partido en tres niveles. Seis estimaciones revisadas con justificación. Modelo de capacidad de dos tasas. Plan de sprints rebalanceado con máximo de 14 SP y colchón en S7. Escalera de degradación y punto de control de velocidad. Reglas RB11–RB16 |

---

**Fin del documento BKL-PETCARE-002 v2.0**
