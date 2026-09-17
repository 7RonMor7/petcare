# Product Backlog v3 — Alcance irrenunciable y compromiso de entrega

**Plataforma PetCare · Fase 2c — Versión para aprobación**

| Campo | Valor |
|---|---|
| Proyecto | Plataforma web de reservas y pagos — PetCare |
| Documento | BKL-PETCARE-003 |
| Versión | 3.0 |
| Fecha | 2026-09-16 |
| Autor | Ronald Moreno |
| Sustituye a | BKL-PETCARE-002 v2.0 |
| Estado | ✅ **APROBADO — línea base oficial del proyecto** (16/09/2026) |
| Alcance aprobado | Opción A — dos entregas: semana 8 (102 SP) y semana 12 (43 SP) |
| Compromiso firme | 90 SP · degradación automática 12 SP · RB05 y RB06 protegidas |
| Equipo | 1 desarrollador · ~15 h/semana |

---

## Estado de aprobación

El cliente aprobó este documento el 16/09/2026 como línea base oficial: los 23 irrenunciables, la opción A de dos entregas, el objetivo de 102 SP con compromiso firme de 90, la escalera de cinco escalones, la protección de RB05 y RB06, el punto de control del Sprint 2 y PRG-70 (rejilla de 30 min con veterinaria ocupando 60, parametrizada). PRG-68 y PRG-69 quedan diferidas junto con el hospedaje.

Cualquier modificación posterior del alcance requiere una nueva decisión formal del cliente.

---

## Nota de apertura

El cliente tiene razón en las dos objeciones de fondo, y conviene decirlo sin matizar:

**Primera.** Trasladé al nivel 2 elementos que el cliente había declarado irrenunciables, y lo presenté como un hecho consumado en lugar de como una decisión suya. Que no quepan en el plazo es un dato; decidir qué se hace con eso no me corresponde. La v3 corrige el método: la sección 3 pone el coste exacto de todos los irrenunciables y la sección 4 plantea cuatro opciones cuantificadas para que el cliente elija. No hay ningún irrenunciable movido de nivel en este documento sin su firma.

**Segunda.** La contradicción de "tres" contra "cuatro" es un error mío de redacción, y el correo estaba mal ubicado. La causa de fondo es que estaba trabajando con **dos listas de irrenunciables distintas** sin haberlas fusionado: la de 20 elementos de la ronda 1 y los Must que añadió DEC-C20 en la ronda 2. La sección 2 las une en una lista canónica única y numerada, que a partir de ahora es la única fuente de verdad.

Las otras cuatro correcciones están aplicadas: la posición del correo (sección 2, IRR-21 y IRR-22), la de pagos rechazados (IRR-12), la escalera sin degradación automática de RB05 y RB06 (sección 7) y la decisión sobre veterinaria replanteada con números (sección 8).

---

## Tabla de contenido

1. [Qué corrige esta versión](#1-qué-corrige-esta-versión)
2. [Lista canónica de irrenunciables](#2-lista-canónica-de-irrenunciables)
3. [El coste de los irrenunciables](#3-el-coste-de-los-irrenunciables)
4. [La decisión que corresponde al cliente](#4-la-decisión-que-corresponde-al-cliente)
5. [Alcance propuesto](#5-alcance-propuesto)
6. [Product Backlog v3](#6-product-backlog-v3)
7. [Escalera de degradación corregida](#7-escalera-de-degradación-corregida)
8. [Decisión sobre la ocupación de veterinaria](#8-decisión-sobre-la-ocupación-de-veterinaria)
9. [Plan de sprints](#9-plan-de-sprints)
10. [Declaración de entrega](#10-declaración-de-entrega)
11. [Qué necesito que el cliente apruebe](#11-qué-necesito-que-el-cliente-apruebe)

---

## 1. Qué corrige esta versión

| # | Objeción del cliente | Cómo se resuelve |
|:--:|---|---|
| 1 | Se trasladaron irrenunciables al nivel 2 de forma unilateral | **Ninguno se mueve en este documento.** La sección 3 cuantifica el conjunto completo (145 SP) y la sección 4 plantea cuatro opciones para que el cliente decida qué significa "irrenunciable" en la primera entrega |
| 2 | Contradicción entre "tres elementos" y los cuatro de la tabla | **Error de redacción corregido.** Se elimina la afirmación de "tres" y se sustituye por la lista canónica de la sección 2, con su cobertura exacta en la sección 5.2 |
| 3 | El correo no figuraba entre los 20 irrenunciables | **Aclarado.** El correo no estaba en la lista de la ronda 1; entró como irrenunciable por DEC-C20 en la ronda 2. Ahora figura como **IRR-21** y **IRR-22** en la lista unificada |
| 4 | Posición de "manejo de pagos rechazados" poco clara | **IRR-12**, mapeado a HU-043 (3 SP). Su posición depende de la opción que se elija en la sección 4, no de mi criterio |
| 5 | La cancelación de 24 h y la expiración de 15 min no deben degradarse automáticamente | **Corregido.** Ambas salen de la escalera y pasan a una *zona protegida* (sección 7). La degradación automática baja de 20 a 12 SP y el compromiso firme sube de 82 a **90 SP** |
| 6 | La opción A de veterinaria no está aprobada | **Replanteada con números** en la sección 8, más una propuesta que hace la decisión reversible sin tocar código |

**Corrección adicional de coherencia:** el cliente declaró irrenunciable la "gestión administrativa básica", pero HU-054 y HU-055 estaban marcadas como *Should*. Se promueven a **Must**. Esto cambia el reparto de prioridades del backlog: Must pasa de 157 a 165 SP y Should de 60 a 52.

---

## 2. Lista canónica de irrenunciables

Única fuente de verdad. Fusiona los 20 elementos de la ronda 1 con los añadidos por DEC-C20 en la ronda 2. Cualquier afirmación anterior sobre qué es irrenunciable queda derogada por esta tabla.

| ID | Irrenunciable | Origen | Historias | SP |
|---|---|---|---|---:|
| **IRR-01** | Registro e inicio de sesión | Ronda 1 | HU-007, HU-008 | 6 |
| **IRR-02** | Roles y autorización | Ronda 1 | HU-009, HU-010 | 5 |
| **IRR-03** | Gestión de mascotas | Ronda 1 | HU-016, HU-017, HU-018 | 8 |
| **IRR-04** | Catálogo de servicios | Ronda 1 | HU-028 | 2 |
| **IRR-05** | Consulta de disponibilidad | Ronda 1 | HU-030, HU-031 | 11 |
| **IRR-06** | Creación de reservas | Ronda 1 | HU-032, HU-034, HU-038 | 13 |
| **IRR-07** | Validación de reservas duplicadas o conflictivas | Ronda 1 | HU-033 | 5 |
| **IRR-08** | Estados de reserva | Ronda 1 | HU-035 | 3 |
| **IRR-09** | Cancelación bajo la regla de 24 horas | Ronda 1 | HU-037 | 3 |
| **IRR-10** | Pago electrónico | Ronda 1 | HU-003, HU-004, HU-039, HU-040 | 11 |
| **IRR-11** | Confirmación del pago mediante webhook | Ronda 1 | HU-041, HU-042 | 7 |
| **IRR-12** | **Manejo de pagos rechazados** | Ronda 1 | HU-043 | 3 |
| **IRR-13** | Expiración de pagos pendientes a los 15 minutos | Ronda 1 | HU-044 | 5 |
| **IRR-14** | Gestión administrativa básica | Ronda 1 | HU-054, HU-055 | 8 |
| **IRR-15** | Gestión de empleados | Ronda 1 | HU-020, HU-021, HU-022 | 8 |
| **IRR-16** | Gestión de servicios | Ronda 1 | HU-027, HU-029 | 4 |
| **IRR-17** | Historial de reservas y pagos | Ronda 1 | HU-036, HU-045 | 5 |
| **IRR-18** | Seguridad de contraseñas | Ronda 1 | *(BCrypt, dentro de HU-007)* | 0 |
| **IRR-19** | Auditoría de operaciones importantes | Ronda 1 | HU-056, HU-057 | 7 |
| **IRR-20** | Despliegue funcional | Ronda 1 | HU-001, HU-002, HU-005, HU-073 | 11 |
| **IRR-21** | **Verificación de correo** *(y la infraestructura que exige)* | **Ronda 2 — DEC-C20** | HU-061, HU-011 | 6 |
| **IRR-22** | **Recuperación de contraseña** | **Ronda 2 — DEC-C20** | HU-012 | 3 |
| **IRR-23** | Rol Empleado operativo | Ronda 1 (DEC-C01) | HU-023, HU-024, HU-025, HU-026 | 11 |
| | **Total** | | **46 historias** | **145** |

### 2.1 Aclaraciones sobre tres entradas que generaron la confusión

- **IRR-21 y IRR-22 (correo).** El cliente tiene razón: no estaban en la lista de 20. Aparecieron como irrenunciables en la ronda 2, cuando DEC-C20 subió a Must la verificación de correo y la recuperación de contraseña. Son irrenunciables por decisión posterior, no por la lista original, y eso es exactamente lo que la v2 no supo explicar.
- **IRR-12 (pagos rechazados).** Está en la lista original de la ronda 1. En la v2 acabó en el nivel 2 sin mención, que es el caso más claro de traslado unilateral. Aquí queda identificado con su historia y su coste.
- **IRR-18 (seguridad de contraseñas).** No tiene historias propias ni SP: el almacenamiento con BCrypt está dentro de HU-007 y en la *Definition of Done*. Aparece con 0 SP para que la lista esté completa, no porque se omita.

---

## 3. El coste de los irrenunciables

| Concepto | SP |
|---|---:|
| Conjunto completo de irrenunciables (sección 2) | **145** |
| Cubierto por el nivel 1 propuesto en la v2 | 102 |
| **Irrenunciables no cubiertos** | **43** |

Los 43 SP que faltan, con nombre y apellido:

| Irrenunciable | Historias | SP |
|---|---|---:|
| IRR-21 Verificación de correo + infraestructura | HU-061, HU-011 | 6 |
| IRR-22 Recuperación de contraseña | HU-012 | 3 |
| IRR-23 Rol Empleado operativo | HU-023, HU-024, HU-025, HU-026 | 11 |
| IRR-15 Gestión de empleados *(la parte de CRUD)* | HU-020 | 3 |
| IRR-12 Manejo de pagos rechazados | HU-043 | 3 |
| IRR-17 Historial de pagos *(la parte de pagos)* | HU-045 | 2 |
| IRR-14 Gestión administrativa básica | HU-054, HU-055 | 8 |
| IRR-19 Auditoría | HU-056, HU-057 | 7 |
| | **14 historias** | **43** |

### 3.1 Contraste con la capacidad

| Alcance | SP | Velocidad requerida en 8 sprints | h/SP implícita |
|---|---:|---:|---:|
| Todos los irrenunciables | 145 | 18,1 SP/sprint | 0,63 |
| Nivel 1 de la v2 | 102 | 12,75 SP/sprint | 0,89 |
| Compromiso firme v3 | 90 | 11,25 SP/sprint | 1,01 |
| Estimación conservadora | 45 | 5,6 SP/sprint | 2,00 |

Entregar los 145 SP en ocho semanas exige completar un story point cada 38 minutos, sostenido, incluyendo pruebas. Eso no es un plan optimista: es un plan que no existe.

---

## 4. La decisión que corresponde al cliente

El cliente lo formuló bien: *o modificamos qué significa irrenunciable, o modificamos el plazo, o reducimos otra parte del alcance.* Estas son las cuatro salidas, con su aritmética. **No elijo por el cliente; recomiendo y expongo por qué.**

### Opción A — Dos entregas, hito en la semana 8 · ⭐ recomendada

Se distingue entre **irrenunciable del producto** e **irrenunciable de la primera entrega**. Los 23 elementos siguen siendo irrenunciables: ninguno se cancela, todos tienen fecha.

| | |
|---|---|
| **Entrega 1 — semana 8** | 102 SP. El ciclo completo reservar → pagar → confirmar → cancelar, desplegado |
| **Entrega 2 — semana 12** | 43 SP. Los irrenunciables restantes: correo, rol Empleado, pagos rechazados, panel administrativo y auditoría |
| **Velocidad requerida** | 12,75 SP/sprint en la entrega 1; 10,75 en la entrega 2 |
| **Ventaja** | Conserva el hito de la semana 8 con algo demostrable de extremo a extremo, y compromete por escrito la fecha de todos los irrenunciables |
| **Coste** | El producto completo llega en la semana 12, no en la 8 |

### Opción B — Plazo único de 12 semanas

| | |
|---|---|
| **Entrega única — semana 12** | 145 SP |
| **Velocidad requerida** | 12,1 SP/sprint |
| **Ventaja** | La misma velocidad que ya asume el plan actual, sin dividir la entrega |
| **Coste** | No hay nada demostrable hasta la semana 12. Si algo va mal, se descubre tarde |

> B entrega lo mismo que A en la misma fecha, pero sin hito intermedio. **A es estrictamente mejor que B**, salvo que el cliente prefiera no gestionar dos entregas.

### Opción C — Ocho semanas con versión mínima de cada irrenunciable

Reducir dentro de cada irrenunciable en lugar de aplazarlo: auditoría sin pantalla de consulta (−2), rol Empleado sin bloqueo de agenda ni ficha de mascota (−4), gestión administrativa solo de reservas (−3), sin verificación de correo dejando solo la recuperación (−3), empleados cargados por migración sin CRUD (−3).

| | |
|---|---|
| **Alcance resultante** | 130 SP |
| **Velocidad requerida** | 16,3 SP/sprint → **0,70 h/SP** |
| **Veredicto** | **Descartada.** Recortar 15 SP no cambia el orden de magnitud del problema, y a cambio entrega ocho irrenunciables mutilados en vez de completos. Se documenta para que conste que se evaluó |

### Opción D — Ocho semanas con mayor dedicación

| | |
|---|---|
| **Alcance** | 145 SP en 8 semanas |
| **Dedicación requerida** | **≈ 22 h/semana** en lugar de 15 |
| **Ventaja** | Única opción que mantiene la fecha y el alcance completo |
| **Coste** | 7 horas semanales adicionales durante dos meses. Solo es viable si el desarrollador puede sostenerlo de verdad; comprometerlo y no cumplirlo es peor que no comprometerlo |

### 4.1 Recomendación

**Opción A.** Es la única que conserva el hito de la semana 8, entrega algo que se puede enseñar funcionando de extremo a extremo, y compromete por escrito la fecha de los 43 SP restantes en lugar de dejarlos en un "ya veremos".

Si la fecha de la semana 8 es inamovible **y** hay disponibilidad real para 22 h semanales, la **D** es preferible, porque entrega todo en el plazo original. Lo que no recomiendo es comprometer la D sin certeza sobre las horas: un plan que depende de un 47 % más de dedicación de la declarada es el mismo problema de la v1 con otro disfraz.

---

## 5. Alcance propuesto

Todo lo que sigue asume la **opción A**. Si el cliente elige otra, cambia el reparto entre entregas pero no el contenido del backlog ni las estimaciones.

### 5.1 Los tres niveles

| Nivel | Contenido | Historias | SP |
|---|---|:--:|---:|
| **1 — Entrega 1** (semana 8) | Ciclo completo reservar → pagar → confirmar → cancelar | 32 | **102** |
| **2 — Entrega 2** (semana 12) | Irrenunciables restantes: correo, rol Empleado, pagos rechazados, panel administrativo, auditoría | 14 | **43** |
| **3 — Diferido** | Estaba en el MVP declarado, no es irrenunciable: promociones, hospedaje, reembolsos, integración real, calidad adicional | 23 | **76** |
| **4 — V2** | Acordado fuera desde el principio | 7 | **24** |
| | | **76** | **245** |

> El nivel 2 de la v2 se parte en dos: lo que es irrenunciable (43 SP, entrega 2, con fecha) y lo que nunca lo fue (76 SP, diferido). Esa mezcla era parte de la confusión que el cliente detectó.

### 5.2 Cobertura de los irrenunciables

| ID | Irrenunciable | Entrega |
|---|---|:--:|
| IRR-01 Registro e inicio de sesión | | **1** |
| IRR-02 Roles y autorización | | **1** |
| IRR-03 Gestión de mascotas | | **1** |
| IRR-04 Catálogo de servicios | | **1** |
| IRR-05 Consulta de disponibilidad | | **1** |
| IRR-06 Creación de reservas | | **1** |
| IRR-07 Validación de duplicadas o conflictivas | | **1** |
| IRR-08 Estados de reserva | | **1** |
| IRR-09 Cancelación de 24 horas | | **1** |
| IRR-10 Pago electrónico | | **1** |
| IRR-11 Confirmación por webhook | | **1** |
| IRR-12 Manejo de pagos rechazados | | 2 |
| IRR-13 Expiración de 15 minutos | | **1** |
| IRR-14 Gestión administrativa básica | | 2 |
| IRR-15 Gestión de empleados | | **1** parcial · 2 el CRUD |
| IRR-16 Gestión de servicios | | **1** |
| IRR-17 Historial de reservas y pagos | | **1** reservas · 2 pagos |
| IRR-18 Seguridad de contraseñas | | **1** |
| IRR-19 Auditoría | | 2 |
| IRR-20 Despliegue funcional | | **1** |
| IRR-21 Verificación de correo | | 2 |
| IRR-22 Recuperación de contraseña | | 2 |
| IRR-23 Rol Empleado operativo | | 2 |

**16 irrenunciables completos en la entrega 1, 2 parciales, 7 en la entrega 2. Los 23 tienen fecha comprometida.**

---

## 6. Product Backlog v3

**Niveles:** 1 = entrega 1 (semana 8) · 2 = entrega 2 (semana 12) · 3 = diferido · 4 = V2.
🔒 = compromiso firme (no se degrada automáticamente). 🛡 = zona protegida (regla de negocio; no se toca sin nueva conversación con el cliente).

### E0 — Fundación y arquitectura

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-001 🔒 | Estructura modular de Spring Boot con Docker Compose, Flyway y perfiles | 1 | M | 3 | S0 | Pendiente |
| HU-002 🔒 | Proyecto React con Tailwind, router y cliente Axios con interceptor de JWT | 1 | M | 2 | S0 | Pendiente |
| HU-003 🔒 | Comparar Wompi, Mercado Pago y PayU y documentar la elección *(timebox 4 h)* | 1 | M | 2 | S0 | Pendiente |
| HU-004 🔒 | Puerto `PaymentGateway` con adaptador simulado | 1 | M | 3 | S3 | Pendiente |
| HU-005 🔒 | Despliegue público mínimo funcionando | 1 | M | 3 | S0 | Pendiente |
| HU-006 | Integración continua: build y pruebas en cada push | 3 | C | 2 | — | Pendiente |

### E1 — Autenticación y autorización

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-007 🔒 | Registrarme con mis datos y aceptar la política de tratamiento | 1 | M | 3 | S1 | Pendiente |
| HU-008 🔒 | Iniciar sesión y recibir token de acceso y de refresco | 1 | M | 3 | S1 | Pendiente |
| HU-009 🔒 | Autorizar cada endpoint según rol y permisos | 1 | M | 3 | S1 | Pendiente |
| HU-010 🔒 | Cerrar sesión con revocación del token de refresco | 1 | M | 2 | S1 | Pendiente |
| HU-011 | Verificar mi correo tras registrarme *(IRR-21)* | 2 | M | 3 | S8 | Pendiente |
| HU-012 | Recuperar mi contraseña por correo *(IRR-22)* | 2 | M | 3 | S8 | Pendiente |
| HU-013 | Cambiar mi contraseña estando autenticado | 3 | S | 1 | — | Pendiente |
| HU-014 | Bloqueo temporal tras 5 intentos fallidos | 3 | C | 2 | — | Pendiente |

### E2 — Cliente y mascotas

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-015 | Ver y editar los datos de mi perfil | 3 | S | 2 | — | Pendiente |
| HU-016 🔒 | Registrar una mascota con sus datos | 1 | M | 3 | S1 | Pendiente |
| HU-017 🔒 | Listar, editar y eliminar mis mascotas, con validación de propiedad | 1 | M | 2 | S2 | Pendiente |
| HU-018 🔒 | Interfaz de gestión de mascotas | 1 | M | 3 | S2 | Pendiente |
| HU-019 | Eliminar mi cuenta con anonimización de datos | 4 | W | 3 | — | Pendiente |

### E3 — Empleados y agenda

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-020 | Crear, editar y desactivar empleados desde el panel *(IRR-15)* | 2 | M | 3 | S9 | Pendiente |
| HU-021 | Asignar a cada empleado los servicios que puede prestar | 1 | M | 2 | S2 | Pendiente |
| HU-022 🔒 | Definir la jornada laboral semanal de cada empleado | 1 | M | 3 | S3 | Pendiente |
| HU-023 | Consultar mi agenda y las reservas que tengo asignadas *(IRR-23)* | 2 | M | 3 | S9 | Pendiente |
| HU-024 | Bloquear franjas de mi agenda, RB13 *(IRR-23)* | 2 | M | 2 | S9 | Pendiente |
| HU-025 | Consultar la información de la mascota que voy a atender *(IRR-23)* | 2 | S | 2 | S10 | Pendiente |
| HU-026 | Cambiar el estado de mis reservas, con la ventana de RB12 *(IRR-23)* | 2 | M | 4 | S9 | Pendiente |

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
| HU-031 🔒 | Consultar franjas disponibles con ocupación multi-franja, RB11 y RB14 | 1 | M | 8 | S4 | Pendiente |
| HU-032 🔒 | Crear una reserva validando RB01, RB02, RB10 y RB11 | 1 | M | 5 | S5 | Pendiente |
| HU-033 🔒 | Garantizar en base de datos que no haya solapamientos bajo concurrencia | 1 | M | 5 | S5 | Pendiente |
| HU-034 🔒 | Asistente de reserva guiado por servicio, mascota, fecha, hora, empleado y resumen | 1 | M | 5 | S6 | Pendiente |
| HU-035 🔒 | Máquina de estados con transiciones válidas | 1 | M | 3 | S3 | Pendiente |
| HU-036 | Listar mis reservas y ver el detalle de cada una | 1 | M | 3 | S6 | Pendiente |
| HU-037 🔒🛡 | Cancelar una reserva hasta 24 horas antes, RB06 | 1 | M | 3 | S6 | Pendiente |
| HU-038 | Elegir empleado o indicar "sin preferencia", DEC-C24 | 1 | M | 3 | S4 | Pendiente |

### E6 — Pagos

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-039 🔒 | Generar la orden de pago con el total congelado | 1 | M | 3 | S5 | Pendiente |
| HU-040 🔒 | Redirigir al checkout y volver a la plataforma | 1 | M | 3 | S6 | Pendiente |
| HU-041 🔒 | Recibir el webhook validando firma, idempotencia y monto | 1 | M | 5 | S7 | Pendiente |
| HU-042 🔒 | Confirmar la reserva al aprobarse el pago, RB03 | 1 | M | 2 | S7 | Pendiente |
| HU-043 | Reintentar el pago rechazado, RB04 *(IRR-12)* | 2 | M | 3 | S10 | Pendiente |
| HU-044 🔒🛡 | Expirar las reservas no pagadas a los 15 minutos, RB05 | 1 | M | 5 | S7 | Pendiente |
| HU-045 | Consultar el estado de mis pagos *(IRR-17)* | 2 | M | 2 | S8 | Pendiente |
| HU-046 | Generar `SOLICITUD_REEMBOLSO` al cancelar una reserva pagada, RB07 | 3 | M | 3 | — | Pendiente |
| HU-047 | Ver las solicitudes de reembolso y marcarlas como `REEMBOLSADO` | 3 | M | 3 | — | Pendiente |
| HU-048 | Configurar credenciales y entorno de la pasarela elegida | 3 | M | 3 | — | Pendiente |
| HU-049 | Integrar el checkout y el webhook reales del proveedor | 3 | M | 5 | — | Pendiente |

### E7 — Promociones

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-050 | Crear promociones con código, porcentaje, vigencia, máximo de usos y estado | 3 | S | 5 | — | Pendiente |
| HU-051 | Aplicar un código y ver el total recalculado antes de pagar | 3 | S | 5 | — | Pendiente |
| HU-052 | Impedir el uso repetido salvo configuración del administrador, RB09 | 3 | S | 3 | — | Pendiente |
| HU-053 | Recuperar el uso de la promoción si la reserva expira o se cancela, RB16 | 3 | S | 2 | — | Pendiente |

### E8 — Panel administrativo y auditoría

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-054 | Listar reservas filtrando por cliente, mascota, servicio, empleado, fecha y estado *(IRR-14)* | 2 | M | 5 | S11 | Pendiente |
| HU-055 | Listar pagos filtrando por cliente, fecha, método, estado y valor *(IRR-14)* | 2 | M | 3 | S11 | Pendiente |
| HU-056 | Registrar en bitácora, en la misma transacción, toda operación importante *(IRR-19)* | 2 | M | 5 | S10 | Pendiente |
| HU-057 | Consultar la bitácora filtrando por actor, entidad y rango de fechas *(IRR-19)* | 2 | S | 2 | S11 | Pendiente |
| HU-058 | Ver los KPIs de reservas de hoy, ingresos del mes, clientes y servicios realizados | 3 | S | 3 | — | Pendiente |
| HU-059 | Ver gráficas de reservas, ingresos y servicios más vendidos | 4 | W | 5 | — | Pendiente |
| HU-060 | Crear una reserva a nombre de un cliente que llamó por teléfono | 4 | W | 5 | — | Pendiente |

### E9 — Notificaciones

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-061 | Infraestructura de correo con plantillas y registro del estado de envío *(IRR-21)* | 2 | M | 3 | S8 | Pendiente |
| HU-062 | Recibir correo al confirmarse la reserva y al aprobarse el pago | 3 | S | 2 | — | Pendiente |
| HU-063 | Recibir correo al rechazarse el pago, expirar o cancelar la reserva | 3 | S | 2 | — | Pendiente |
| HU-064 | Recibir un recordatorio el día anterior a la reserva | 4 | W | 3 | — | Pendiente |
| HU-065 | Recibir aviso cuando me asignen o me cancelen una reserva | 4 | W | 2 | — | Pendiente |

### E10 — Hospedaje

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-066 | Definir la capacidad diaria de cupos de hospedaje, RB15 | 3 | S | 5 | — | Pendiente |
| HU-067 | Reservar hospedaje por rango de fechas con total por día, RB15 | 3 | S | 6 | — | Pendiente |
| HU-068 | Validar cupo en todo el rango y retenerlo durante los 15 min del pago | 3 | S | 5 | — | Pendiente |
| HU-069 | Ver la ocupación del hospedaje por fecha | 4 | W | 3 | — | Pendiente |

### E11 — Calidad y despliegue

| ID | Historia | Niv | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|:--:|---|
| HU-070 | Pruebas de controlador y de endpoint sobre los flujos críticos | 3 | M | 5 | — | Pendiente |
| HU-071 | Pruebas de los componentes críticos del frontend | 3 | S | 3 | — | Pendiente |
| HU-072 | Responsive completo en móvil, tablet y escritorio, RNF04-R | 3 | M | 5 | — | Pendiente |
| HU-073 🔒 | Despliegue final con variables de entorno y HTTPS | 1 | M | 3 | S4 | Pendiente |
| HU-074 | Documentación de la API con OpenAPI | 3 | S | 2 | — | Pendiente |
| HU-075 | Prueba de carga que valide RNF02-R | 4 | W | 3 | — | Pendiente |
| HU-076 | Manual de usuario y documentación de entrega | 3 | S | 2 | — | Pendiente |

### 6.1 Resumen

| Nivel | Historias | SP | Must | Should | Could | Won't |
|---|:--:|:--:|:--:|:--:|:--:|:--:|
| 1 — Entrega 1 | 32 | 102 | 102 | 0 | 0 | 0 |
| 2 — Entrega 2 | 14 | 43 | 39 | 4 | 0 | 0 |
| 3 — Diferido | 23 | 76 | 24 | 48 | 4 | 0 |
| 4 — V2 | 7 | 24 | 0 | 0 | 0 | 24 |
| **Total** | **76** | **245** | **165** | **52** | **4** | **24** |

> Las pruebas unitarias y de servicio, la protección de endpoints y el responsive básico no aparecen como historias del nivel 1: están en la *Definition of Done* de cada historia y ya están dentro de sus estimaciones (BKL-PETCARE-001 §10). HU-070 y HU-072 son el refuerzo adicional, no la cobertura mínima.

---

## 7. Escalera de degradación corregida

El cliente tiene razón: una regla de negocio no puede caerse por un mecanismo automático. La escalera se parte en dos zonas.

### 7.1 Degradación automática — 12 SP

Se aplica sin consulta si el punto de control de la semana 3 lo exige. Ninguna toca una regla de negocio: todas reducen comodidad o alcance administrativo.

| # | Historia | SP | Sprint | Qué se pierde |
|:--:|---|:--:|:--:|---|
| 1 | HU-038 — Elegir empleado | 3 | S4 | El sistema asigna siempre automáticamente |
| 2 | HU-029 — Desactivar servicio sin afectar confirmadas | 1 | S2 | La desactivación queda como borrado lógico simple |
| 3 | HU-036 — Listado de reservas del cliente | 3 | S6 | Lista mínima sin pantalla de detalle |
| 4 | HU-021 — Servicios por empleado | 2 | S2 | Todos los empleados prestan todos los servicios |
| 5 | HU-027 — CRUD de servicios | 3 | S2 | Los cinco servicios se cargan por migración de datos |
| | **Total** | **12** | | Compromiso firme resultante: **90 SP** |

### 7.2 Zona protegida — 8 SP

| Historia | SP | Regla | Condición |
|---|:--:|---|---|
| HU-037 🛡 | 3 | **RB06** — cancelación hasta 24 h antes | No se degrada por ningún mecanismo automático |
| HU-044 🛡 | 5 | **RB05** — expiración a los 15 minutos | No se degrada por ningún mecanismo automático |

Si el punto de control indica que el compromiso firme de 90 SP tampoco cabe, **no se toca ninguna de las dos**: se convoca al cliente y se decide en conjunto, con las cifras del momento sobre la mesa. Renunciar a RB05 o RB06 es cambiar el producto, no ajustar un plan, y esa decisión es del cliente.

### 7.3 Punto de control revisado

Al cerrar el **Sprint 2** se calcula `velocidad real = SP completados en S0+S1+S2 ÷ 3`:

| Velocidad real | Acción |
|---|---|
| **≥ 12,75** | Se mantiene el objetivo de 102 SP |
| **11,25 – 12,75** | Se aplican los cinco escalones automáticos (−12 SP). Objetivo: **90 SP** |
| **< 11,25** | **Se convoca al cliente.** No hay más degradación automática disponible. Se presentan las opciones con los datos reales de velocidad |

---

## 8. Decisión sobre la ocupación de veterinaria

El cliente no aprueba la opción A porque modifica la disponibilidad que ve el usuario. Es una objeción correcta, así que aquí están los números que faltaban.

**El caso:** rejilla de 30 minutos, jornada de 08:00 a 18:00 (600 minutos), veterinaria de 45 minutos. Paseo (60), baño (90) y peluquería (120) son múltiplos exactos de 30; veterinaria no.

### 8.1 Consultas por día y por veterinario

| Opción | Cómo funciona | Inicios posibles | Consultas/día | Minutos desaprovechados |
|---|---|---|:--:|:--:|
| **A — Redondear a 60 min** | La consulta ocupa 60 min de agenda aunque dure 45 | 08:00, 09:00, 10:00… | **10** | 150 (como margen entre pacientes) |
| **B — Rejilla de 15 min** | Ocupación exacta sobre una rejilla más fina | 08:00, 08:45, 09:30… | **13** | 15 |
| **C — Ocupación exacta sobre rejilla de 30** | La consulta ocupa 45 min exactos | 08:00, 09:00, 10:00… | **10** | 150 (en huecos de 15 min invendibles) |

**El hallazgo que cambia la discusión: A y C dan exactamente el mismo resultado.** Con una rejilla de 30 minutos, una consulta que empieza a las 08:00 termina a las 08:45, y el siguiente inicio disponible en la rejilla es a las 09:00 de todos modos. Los 15 minutos sobrantes no son vendibles en ninguna de las dos opciones; la diferencia es que en A quedan declarados como ocupados y en C quedan declarados como libres sin serlo, lo que produce una disponibilidad que miente al usuario.

**Por tanto, la disponibilidad que ve el cliente final es idéntica en A y en C.** La preocupación del cliente sobre A aplica en realidad a la comparación con B, que es la única que aumenta la capacidad: **tres consultas más por día y por veterinario, un 30 %.**

### 8.2 El coste de B

Rejilla de 15 minutos para **todos** los servicios: el doble de franjas que generar, calcular, transmitir y mostrar. Una jornada pasa de 20 a 40 franjas por empleado y día. Afecta al rendimiento de HU-031 (la historia más cara del plan, 8 SP) y densifica la interfaz de selección de hora en móvil, donde el espacio ya es escaso.

### 8.3 Propuesta: no decidir ahora

El tamaño de la franja se implementa como **parámetro de sistema**, no como constante en el código:

```
ParametroSistema.tamanoFranjaMinutos = 30
Servicio.duracionAgendaMinutos = ceil(duracionMinutos / tamanoFranja) × tamanoFranja
```

Con esto:

- Se arranca con la **opción A** (rejilla de 30, veterinaria ocupando 60), que es la más simple y no pierde ni una consulta frente a C.
- Cambiar a la opción B más adelante es modificar un valor de configuración y regenerar las franjas: **sin tocar código y sin migración**.
- La decisión deja de ser irreversible y puede tomarse con datos reales de ocupación, en lugar de ahora y a ciegas.

El coste de esta reversibilidad es cero: parametrizar el tamaño de franja no añade SP a HU-030 ni a HU-031, porque el cálculo es el mismo con una constante o con un parámetro.

**PRG-70 revisada:** ¿acepta el cliente arrancar con rejilla de 30 y veterinaria ocupando 60 minutos, sabiendo que es reversible por configuración y que la alternativa C no aporta ninguna consulta adicional?

---

## 9. Plan de sprints

### 9.1 Entrega 1 — semanas 1 a 8 · 102 SP

Sin cambios respecto a la v2, que el cliente aceptó. Media 12,75 SP, máximo 14, Sprint 7 como colchón.

| Sprint | Objetivo demostrable | Historias | SP |
|:--:|---|---|---:|
| **S0** | La aplicación existe, está desplegada en una URL pública y hay pasarela elegida | HU-001, HU-002, HU-003, HU-005 | 10 |
| **S1** | Un cliente se registra, inicia sesión y registra su mascota | HU-007, HU-008, HU-009, HU-010, HU-016 | 14 |
| **S2** | El cliente gestiona sus mascotas y ve el catálogo con precios reales | HU-017, HU-018, HU-021, HU-027, HU-028, HU-029 | 13 |
| **S3** | El sistema conoce la jornada de cada empleado y genera su rejilla | HU-004, HU-022, HU-030, HU-035 | 12 |
| **S4** | El sistema muestra disponibilidad real con ocupación multi-franja | HU-031, HU-038, HU-073 | 14 |
| **S5** | Se crea una reserva válida con orden de pago, a prueba de concurrencia | HU-032, HU-033, HU-039 | 13 |
| **S6** | El cliente reserva, ve su historial, cancela y llega al checkout | HU-034, HU-036, HU-037, HU-040 | 14 |
| **S7** | El webhook confirma y las reservas no pagadas expiran solas | HU-041, HU-042, HU-044 | 12 |
| | | **Total** | **102** |

### 9.2 Entrega 2 — semanas 9 a 12 · 43 SP

| Sprint | Objetivo demostrable | Historias | SP |
|:--:|---|---|---:|
| **S8** | El sistema envía correo: verificación al registrarse y recuperación de contraseña. El cliente consulta sus pagos | HU-061, HU-011, HU-012, HU-045 | 11 |
| **S9** | El empleado entra, ve su agenda, bloquea franjas y cambia el estado de sus reservas | HU-020, HU-023, HU-024, HU-026 | 12 |
| **S10** | El empleado consulta la ficha de la mascota, el cliente reintenta pagos rechazados y el sistema registra auditoría | HU-025, HU-043, HU-056 | 10 |
| **S11** | El administrador consulta reservas, pagos y la bitácora con filtros | HU-054, HU-055, HU-057 | 10 |
| | | **Total** | **43** |

Velocidad requerida en la entrega 2: **10,75 SP/sprint**, inferior a la de la entrega 1. Es deliberado: al llegar a la semana 9 el proyecto ya tiene arquitectura, patrones y despliegue resueltos, y el trabajo restante es mayoritariamente CRUD y pantallas — de la categoría "conocido" del modelo de dos tasas.

---

## 10. Declaración de entrega

### 10.1 Al final de la semana 8

Una plataforma desplegada en una URL pública con HTTPS, donde un cliente crea su cuenta con sesión protegida por JWT y autorización por rol; registra, edita y elimina sus mascotas sin poder tocar las de otro; consulta el catálogo de los cinco servicios con sus precios; consulta disponibilidad real calculada sobre la jornada de cada empleado, en franjas de 30 minutos y con ocupación multi-franja; reserva eligiendo empleado o sin preferencia; paga en el checkout y su reserva se confirma por webhook validado con firma, idempotencia y contraste de monto; ve su historial y cancela hasta 24 horas antes; y si no paga en 15 minutos, la reserva expira y la franja se libera.

El sistema impide reservar para una mascota ajena, sobre un empleado ocupado, sobre una mascota con otra reserva solapada o fuera del rango de antelación, y lo garantiza en la base de datos, con una prueba automatizada de 20 peticiones concurrentes.

**No incluye, y el cliente lo sabe de antemano:** la integración con una pasarela real — el ciclo funciona contra un adaptador simulado con toda la lógica implementada y probada, y **no debe presentarse como integración productiva** (DEC-C13).

### 10.2 Al final de la semana 12

Se añade: verificación de correo y recuperación de contraseña; el rol Empleado operativo completo — agenda propia, bloqueo de franjas, ficha de la mascota y cambio de estados con la ventana de RB12; reintento de pagos rechazados; consulta del estado de pagos; panel administrativo con listados filtrables de reservas y pagos; y auditoría con registro transaccional y consulta.

**Con esto quedan entregados los 23 irrenunciables.**

### 10.3 Lo que queda fuera de ambas entregas

Promociones, hospedaje, reembolsos, integración real de la pasarela, KPIs del dashboard, notificaciones por correo de reservas y pagos, perfil del cliente, refuerzo de pruebas y responsive completo (76 SP), más lo acordado como V2 desde el principio (24 SP). Ninguno de estos figura en la lista de irrenunciables.

---

## 11. Qué necesito que el cliente apruebe

| # | Punto | Estado |
|:--:|---|---|
| 1 | **La lista canónica de 23 irrenunciables** de la sección 2, que deroga cualquier versión anterior | Pendiente |
| 2 | **La opción de la sección 4.** Recomiendo la A: dos entregas, hito en la semana 8 y todos los irrenunciables cerrados en la semana 12 | **Decisión bloqueante** |
| 3 | El compromiso firme de **90 SP** y el objetivo de **102 SP** para la entrega 1 | Pendiente |
| 4 | La **escalera de cinco escalones automáticos** (12 SP) y la **zona protegida** de RB05 y RB06 | Pendiente |
| 5 | El **punto de control al cerrar el Sprint 2**, con convocatoria al cliente si la velocidad baja de 11,25 | Pendiente |
| 6 | **PRG-70 revisada:** arrancar con rejilla de 30 min y veterinaria ocupando 60, parametrizado para poder cambiar a 15 min sin tocar código | Pendiente |
| 7 | **PRG-68 y PRG-69** sobre el cobro y la liberación del cupo de hospedaje | No bloquean: hospedaje está en el nivel 3 |

Con los puntos 1 a 6 aprobados, este documento pasa a ser el compromiso oficial y el siguiente entregable son las historias de usuario detalladas del Sprint 0 y el Sprint 1.

---

## Registro de cambios

| Versión | Fecha | Cambios |
|---|---|---|
| 1.0 | 2026-09-15 | Backlog inicial de 243 SP en un solo nivel |
| 2.0 | 2026-09-16 | Tres niveles, seis estimaciones revisadas, modelo de capacidad de dos tasas, sprints rebalanceados |
| 3.0 | 2026-09-16 | Lista canónica de irrenunciables (145 SP). Corrección de la contradicción "tres vs. cuatro" y de la posición del correo y de pagos rechazados. Cuatro opciones cuantificadas para que el cliente decida el alcance. Nivel 2 partido en entrega 2 (irrenunciables, con fecha) y diferido. RB05 y RB06 sacadas de la degradación automática; compromiso firme 82 → 90 SP. HU-054 y HU-055 promovidas a Must. Decisión de veterinaria replanteada con cifras y hecha reversible por configuración |

---

**Fin del documento BKL-PETCARE-003 v3.0**
