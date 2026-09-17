# Documento de Especificación de Requisitos — Plataforma PetCare

**Fase 1 — Levantamiento inicial de requisitos**

| Campo | Valor |
|---|---|
| Proyecto | Plataforma web de reservas y pagos — PetCare |
| Documento | DER-PETCARE-001 |
| Versión | 1.0 (borrador para validación) |
| Fecha | 2026-09-10 |
| Autor | Ronald Moreno |
| Estado | **En revisión — requiere validación del cliente** |
| Fase | 1 de N — Levantamiento inicial |
| Contexto | Proyecto académico. El rol de "cliente" lo asume el docente / stakeholder simulado |

---

## Nota de lectura

Este documento **no es** una especificación cerrada. Es deliberadamente un documento de *incertidumbre*: su trabajo es dejar por escrito qué se sabe, qué no se sabe, qué está mal escrito, qué puede salir mal y qué no se debe decidir todavía.

Un error frecuente en proyectos académicos es pasar directamente del enunciado al modelo de datos. El enunciado de PetCare es inusualmente detallado (10 objetivos, 10 reglas de negocio, 13 estados, 8 RNF), y eso genera una **falsa sensación de completitud**. El análisis que sigue muestra que varias de esas reglas son incompletas, dos de los cinco servicios del negocio no encajan en el flujo de reserva propuesto, y hay al menos tres funcionalidades imprescindibles que el enunciado no menciona en absoluto.

**Convención de identificadores usada en este documento:**

| Prefijo | Significado |
|---|---|
| `GAP-xx` | Vacío de información |
| `AMB-xx` | Requisito ambiguo |
| `CON-xx` | Contradicción o inconsistencia interna |
| `PRG-xx` | Pregunta para el cliente |
| `SUP-xx` | Supuesto por defecto (aplica si no hay respuesta) |
| `RSG-xx` | Riesgo |
| `DIS-xx` | Funcionalidad discutible antes de aprobar alcance |
| `DEC-xx` | Decisión técnica diferida |
| `RB-xx` / `RNF-xx` | Regla de negocio / requisito no funcional (del enunciado) |

---

## Tabla de contenido

1. [Introducción](#1-introducción)
2. [Entendimiento actual del sistema](#2-entendimiento-actual-del-sistema)
3. [Análisis de vacíos de información](#3-análisis-de-vacíos-de-información)
4. [Requisitos ambiguos](#4-requisitos-ambiguos)
5. [Contradicciones e inconsistencias](#5-contradicciones-e-inconsistencias)
6. [Funcionalidades ausentes del enunciado](#6-funcionalidades-ausentes-del-enunciado)
7. [Banco de preguntas para el cliente](#7-banco-de-preguntas-para-el-cliente)
8. [Preguntas bloqueantes para construir el Product Backlog](#8-preguntas-bloqueantes-para-construir-el-product-backlog)
9. [Supuestos por defecto](#9-supuestos-por-defecto)
10. [Riesgos iniciales](#10-riesgos-iniciales)
11. [Funcionalidades discutibles antes de aprobar el alcance](#11-funcionalidades-discutibles-antes-de-aprobar-el-alcance)
12. [Decisiones técnicas que aún no deben tomarse](#12-decisiones-técnicas-que-aún-no-deben-tomarse)
13. [Análisis crítico de la arquitectura propuesta](#13-análisis-crítico-de-la-arquitectura-propuesta)
14. [Requisitos no funcionales reformulados](#14-requisitos-no-funcionales-reformulados)
15. [Criterios de salida de la Fase 1](#15-criterios-de-salida-de-la-fase-1)
16. [Anexo A — Impacto en la planificación de sprints](#anexo-a--impacto-en-la-planificación-de-sprints)
17. [Anexo B — Plantilla de matriz de permisos](#anexo-b--plantilla-de-matriz-de-permisos)
18. [Anexo C — Glosario](#anexo-c--glosario)

---

## 1. Introducción

### 1.1 Propósito

Documentar el estado del levantamiento de requisitos de la plataforma PetCare al cierre de la Fase 1, identificando de forma explícita la información faltante, ambigua o contradictoria, los riesgos iniciales y las decisiones que deben posponerse, de modo que el Product Backlog se construya sobre una base validada y no sobre suposiciones del equipo de desarrollo.

### 1.2 Alcance del documento

Este documento cubre:

- El entendimiento actual del dominio, tal como fue comunicado.
- El análisis de completitud, consistencia y verificabilidad de ese entendimiento.
- El conjunto de preguntas que debe responder el cliente antes de aprobar el alcance.

Este documento **no** cubre: diseño de la base de datos, diagramas UML detallados, historias de usuario con criterios de aceptación, ni decisiones de implementación. Todo eso corresponde a fases posteriores y depende de las respuestas obtenidas aquí.

### 1.3 Audiencia

| Audiencia | Qué debe leer |
|---|---|
| Cliente / Product Owner | Secciones 3, 5, 7, 8, 9, 11 |
| Docente / evaluador | Documento completo |
| Equipo de desarrollo | Documento completo, con foco en 10, 12, 13, 14 |

### 1.4 Documentos de referencia

- Enunciado del proyecto PetCare (contexto entregado al equipo).
- ISO/IEC/IEEE 29148:2018 — *Requirements engineering* (guía de estructura y de criterios de calidad de requisitos).
- Ley 1581 de 2012 y Decreto 1377 de 2013 — Protección de datos personales (Colombia).
- PCI-DSS v4.0 — alcance aplicable a comercios que **no** almacenan datos de tarjeta (SAQ-A).

---

## 2. Entendimiento actual del sistema

Esta sección resume lo que el enunciado **sí** deja establecido. Sirve como línea base: si algo aquí está mal, el cliente debe corregirlo antes de continuar.

### 2.1 Dominio del negocio

PetCare presta cinco servicios: paseos, cuidado a domicilio, baño y peluquería, consulta veterinaria y hospedaje temporal. Hoy la reserva es un proceso manual, telefónico o presencial: el cliente pregunta disponibilidad, reserva y paga por fuera del sistema.

### 2.2 Capacidades solicitadas

| # | Capacidad | Actor |
|---|---|---|
| 1 | Crear cuenta | Cliente |
| 2 | Registrar mascotas | Cliente |
| 3 | Consultar catálogo de servicios | Cliente |
| 4 | Consultar precios y disponibilidad | Cliente |
| 5 | Reservar un servicio | Cliente |
| 6 | Pagar en línea | Cliente |
| 7 | Consultar sus reservas | Cliente |
| 8 | Cancelar reservas según condiciones | Cliente |
| 9 | Consultar estado de pagos | Cliente |
| 10 | Recibir confirmaciones | Cliente |
| 11 | Gestionar el negocio (dashboard, búsquedas, filtros) | Administrador |
| — | *Sin definir* | **Empleado** |

### 2.3 Entidades identificadas

| Entidad | Atributos declarados | Completitud |
|---|---|---|
| Usuario | (no detallados) | ⚠️ Incompleta |
| Rol | Cliente, Empleado, Administrador | ⚠️ Sin permisos |
| Mascota | ID, Nombre, Especie, Raza, Sexo, Fecha de nacimiento, Peso, Observaciones, Cliente | ✅ Razonable |
| Servicio | ID, Nombre, Descripción, Precio, Duración, Estado, Tipo de servicio | ⚠️ Ver AMB-03, GAP-06 |
| Reserva | (no detallados; solo estados) | ⚠️ Incompleta |
| Pago | (no detallados; solo estados) | ⚠️ Incompleta |
| Promoción | Código, % descuento, fecha inicial, fecha final, máx. usos, estado | ⚠️ Ver GAP-14 |

### 2.4 Máquinas de estado declaradas

**Reserva:** `PENDIENTE_PAGO` → `CONFIRMADA` → `EN_PROCESO` → `COMPLETADA`; transiciones alternas a `CANCELADA`, `EXPIRADA`, `NO_ASISTIO`.

**Pago:** `PENDIENTE` → `APROBADO` | `RECHAZADO` | `CANCELADO` → `REEMBOLSADO`.

> Ninguna de las dos máquinas de estado está completa: no se declara **quién** dispara cada transición, ni cuáles transiciones son inválidas. Ver GAP-09 y CON-03.

---

## 3. Análisis de vacíos de información

> **Pregunta respondida: ¿Qué información consideras insuficiente?**

Se identificaron **30 vacíos** en total: los 24 de esta sección (GAP-01 a GAP-24) más 6 funcionalidades directamente ausentes del enunciado, tratadas aparte en la [sección 6](#6-funcionalidades-ausentes-del-enunciado) (GAP-25 a GAP-30). Se clasifican por criticidad:
🔴 **Bloqueante** (impide diseñar el modelo o el flujo) · 🟠 **Alto** (genera retrabajo si se resuelve tarde) · 🟡 **Medio** (puede resolverse durante el sprint correspondiente).

### 3.1 Modelo operativo del negocio

| ID | Vacío | Criticidad | Impacto si no se resuelve |
|---|---|---|---|
| **GAP-01** | **Horarios de atención.** No se indica en qué días ni franjas opera PetCare, ni cómo se tratan domingos y los 18 festivos del calendario colombiano. | 🔴 | Sin esto no existe el concepto de "horario válido" que exige el flujo de reserva. El motor de disponibilidad no se puede construir. |
| **GAP-02** | **Capacidad y modelo de recursos.** ¿Un empleado atiende una sola mascota a la vez? Un paseo grupal con 4 perros es práctica estándar del sector y rompe el supuesto 1 empleado = 1 reserva de RB02. | 🔴 | Define si el recurso escaso es el empleado, el cupo, o ambos. Cambia por completo la validación de disponibilidad. |
| **GAP-03** | **Sedes.** ¿PetCare tiene un local, varios, o solo opera a domicilio? Baño/peluquería y hospedaje sugieren local físico; paseos y cuidado sugieren domicilio. | 🟠 | Determina si `Sede` es una entidad y si el cliente debe elegirla en el flujo de reserva. |
| **GAP-04** | **Servicios a domicilio y cobertura geográfica.** Ni el cliente ni la reserva tienen dirección. No hay zonas de cobertura, ni tiempo de desplazamiento entre reservas consecutivas. | 🔴 | Sin buffer de desplazamiento el sistema agendará al mismo paseador en dos barrios distintos con 0 minutos de diferencia. Es un defecto funcional garantizado. |
| **GAP-05** | **Duración: ¿fija o variable?** El servicio tiene un campo `Duración` único. Un baño de un Chihuahua y uno de un San Bernardo no duran lo mismo. | 🟠 | Afecta el cálculo del bloque horario y la precisión de la agenda. |
| **GAP-06** | **Modelo de precios.** `Servicio.Precio` es un valor único. En el sector, baño/peluquería y hospedaje se tarifan por tamaño o peso de la mascota; el paseo por duración; la consulta veterinaria puede ser fija. | 🔴 | Si el precio es variable, `Servicio` necesita una tabla de tarifas y el cálculo del total cambia. Rehacer esto después del Sprint 3 es caro. |
| **GAP-07** | **Disponibilidad y turnos del empleado.** RB02 solo prohíbe el solapamiento con otra reserva. No existe el concepto de jornada laboral, descanso, incapacidad, vacaciones ni bloqueo manual de agenda. | 🔴 | RB02 tal como está permite reservar a un empleado a las 3:00 a.m. de un domingo si no tiene otra reserva. |
| **GAP-08** | **Canal telefónico.** El negocio actual atiende por teléfono. No se dice si ese canal desaparece o convive con la plataforma. | 🟠 | Si convive, el administrador necesita crear reservas manualmente y existe riesgo de doble agenda (RSG-13). |

### 3.2 Reservas y ciclo de vida

| ID | Vacío | Criticidad | Impacto si no se resuelve |
|---|---|---|---|
| **GAP-09** | **Responsables de las transiciones de estado.** No se dice quién marca `EN_PROCESO`, `COMPLETADA` ni `NO_ASISTIO`, ni desde qué interfaz. El empleado no tiene funcionalidades definidas. | 🔴 | Tres de los siete estados de reserva no tienen forma de alcanzarse. |
| **GAP-10** | **Reprogramación.** El enunciado contempla cancelar, no reprogramar. Es la operación más pedida en agendamiento. | 🟠 | Si se pide después, obliga a rehacer la lógica de disponibilidad y de pagos (¿se conserva el pago?). |
| **GAP-11** | **Reservas recurrentes.** El paseo diario o 3 veces por semana es el modelo comercial habitual del sector. No se menciona. | 🟠 | Impacta el modelo de datos (serie vs. ocurrencia) y el pago (¿se paga cada paseo o el paquete?). |
| **GAP-12** | **Cancelación originada por PetCare.** Empleado enfermo, mascota agresiva, clima. No hay regla ni estado. | 🟠 | Ocurrirá en producción y no hay respuesta definida (¿reembolso total?, ¿reasignación automática?). |
| **GAP-13** | **Consecuencia de `NO_ASISTIO`.** ¿Se reembolsa? ¿Hay penalización? ¿Afecta futuras reservas del cliente? | 🟡 | El estado existe pero no tiene efecto definido. |

### 3.3 Pagos

| ID | Vacío | Criticidad | Impacto si no se resuelve |
|---|---|---|---|
| **GAP-14** | **Métodos de pago aceptados.** El filtro del panel administrativo menciona "Método de pago" en plural, pero nunca se enumeran. En Colombia lo esperable es tarjeta, PSE, Nequi/Daviplata y posiblemente efectivo. | 🔴 | Si se acepta efectivo o pago en sitio, **RB03 deja de ser cierta** y el flujo completo cambia. |
| **GAP-15** | **Pago total vs. anticipo.** RB03 implica pago total anticipado. En hospedaje y servicios de alto valor es común un abono del 30–50 %. | 🟠 | Afecta el modelo de `Pago` (uno a uno vs. varios pagos por reserva). |
| **GAP-16** | **Mecánica del reembolso (RB07).** "Se genera una solicitud" no dice si es automática vía API de la pasarela o un trámite manual del administrador, ni el plazo, ni si se retiene un porcentaje. | 🟠 | Define si hay integración de salida adicional con la pasarela y si se requiere una bandeja de reembolsos en el panel. |
| **GAP-17** | **Contrato de integración de la pasarela.** No se especifica si la confirmación llega por *webhook*, por *redirect* de retorno, o ambos; ni cómo se firma; ni política de reintentos. | 🔴 | Es la integración de mayor riesgo del proyecto (RSG-01) y define si la reserva puede confirmarse sin que el cliente regrese al sitio. |
| **GAP-18** | **Conciliación.** Qué hacer si el webhook nunca llega y el dinero sí fue debitado. | 🟠 | Sin un proceso de consulta de estado (*polling*) quedan reservas expiradas con pago aprobado: el peor escenario posible para el cliente final. |
| **GAP-19** | **Facturación e impuestos.** ¿Los precios incluyen IVA? ¿Se emite factura electrónica DIAN? ¿Se requiere número de documento del cliente? | 🟡 | Puede quedar fuera del MVP, pero si entra afecta el modelo de `Usuario` y de `Pago`. |

### 3.4 Promociones

| ID | Vacío | Criticidad | Impacto si no se resuelve |
|---|---|---|---|
| **GAP-20** | **Ámbito de aplicación.** RB08 no dice si la promoción aplica a todos los servicios, a algunos, a un tipo de servicio, o a clientes nuevos. | 🟠 | Cambia el modelo de `Promocion` de una tabla plana a una relación con `Servicio`. |
| **GAP-21** | **Tipo de descuento y topes.** Solo se contempla porcentaje. No hay monto fijo, ni tope máximo de descuento, ni monto mínimo de compra, ni acumulabilidad entre promociones. | 🟡 | Riesgo de un 90 % de descuento aplicado sobre un hospedaje de 15 días. |
| **GAP-22** | **Devolución del uso.** Si una reserva con promoción **expira** (RB05) o se **cancela** (RB06), ¿el uso de la promoción se libera? RB09 no lo contempla. | 🟠 | Sin esto, un cliente pierde su cupón por un fallo de la pasarela. Genera reclamos reales. |

### 3.5 Notificaciones, cuenta y datos

| ID | Vacío | Criticidad | Impacto si no se resuelve |
|---|---|---|---|
| **GAP-23** | **Catálogo completo de eventos notificables y sus destinatarios.** Solo se dan 3 ejemplos, todos al cliente. No se define si el empleado recibe su agenda ni si el administrador recibe alertas. | 🟠 | Ver sección 6: faltan al menos 6 eventos evidentes. |
| **GAP-24** | **Tratamiento de datos personales.** No hay política de privacidad, ni autorización de tratamiento (Ley 1581/2012), ni política de retención, ni procedimiento de eliminación de cuenta. | 🟠 | Obligación legal en Colombia. Además, la consulta veterinaria puede generar información clínica que exige cuidado adicional. |

---

## 4. Requisitos ambiguos

> **Pregunta respondida: ¿Qué requisitos se ven ambiguos?**

Un requisito es ambiguo cuando dos personas razonables pueden leerlo y construir cosas distintas. Para cada uno se propone una **redacción alternativa verificable**.

| ID | Texto original | Por qué es ambiguo | Redacción propuesta |
|---|---|---|---|
| **AMB-01** | *"El horario sea válido"* | No define validez. ¿Dentro del horario de atención? ¿En el futuro? ¿Con antelación mínima? ¿Alineado a bloques de 30 min? | "Un horario es válido si: (a) está dentro de la jornada de atención de la sede/servicio; (b) inicia con al menos *N* horas de antelación respecto al momento actual; (c) coincide con el inicio de un bloque de *M* minutos; (d) el intervalo `[inicio, inicio + duración]` no cruza el cierre de la jornada." |
| **AMB-02** | *"La reserva cumpla las reglas del negocio"* | Es circular: la lista de validaciones incluye "cumplir las reglas del negocio", que es el conjunto que la contiene. No es verificable. | Eliminar la viñeta y sustituirla por la enumeración explícita de las validaciones (RB01, RB02, RB10, AMB-01, estado del servicio, vigencia de promoción). |
| **AMB-03** | `Servicio.Estado` | No se enumeran los valores. ¿`ACTIVO`/`INACTIVO`? ¿Existe `BORRADOR` o `DESCONTINUADO`? ¿Qué pasa con las reservas futuras de un servicio que se desactiva? | "`Servicio.Estado ∈ {ACTIVO, INACTIVO}`. Un servicio `INACTIVO` no aparece en el catálogo ni admite nuevas reservas, pero las reservas ya `CONFIRMADA` se mantienen y se ejecutan." |
| **AMB-04** | `Servicio.TipoServicio` | No se sabe si es una categoría de catálogo (`PASEO`, `VETERINARIA`…) o una discriminante de comportamiento (a domicilio vs. en sede, por hora vs. por día). | Separar en dos atributos: `Categoria` (taxonomía comercial) y `Modalidad ∈ {EN_SEDE, A_DOMICILIO}` + `UnidadAgenda ∈ {BLOQUE_HORARIO, DIA_COMPLETO}`. |
| **AMB-05** | *"Empleado disponible"* en el flujo de reserva | No se sabe si el cliente **elige** al empleado de una lista, si el sistema **asigna** automáticamente, o si hay una opción "cualquiera disponible". | Definir explícitamente una de las tres. Recomendación: mostrar lista + opción "sin preferencia" con asignación automática por menor carga. |
| **AMB-06** | *"Confirmación"* (paso del flujo de reserva) | Colisiona con el estado `CONFIRMADA`. En el flujo, "Confirmación" ocurre **antes** del pago; el estado `CONFIRMADA` solo se alcanza **después** (RB03). | Renombrar el paso del flujo a **"Resumen de la reserva"** y reservar la palabra "confirmada" para el estado. |
| **AMB-07** | RB05 — *"tiempo máximo de 15 minutos"* | ¿15 minutos desde la creación de la reserva, desde la generación de la orden de pago, o desde el último intento? ¿Se recalcula en cada reintento de RB04? | "La reserva expira 15 minutos después de la creación de la **primera** orden de pago. Un reintento (RB04) no reinicia el contador." *(o la alternativa contraria, pero debe elegirse una)* |
| **AMB-08** | RB06 — *"hasta 24 horas antes del servicio"* | ¿24 h antes del **inicio** de la reserva? ¿Se calcula en hora local `America/Bogota` o en UTC? ¿Se cuentan horas calendario u horas hábiles? | "Una reserva puede cancelarse si `inicio_reserva − ahora ≥ 24 horas`, evaluado en zona horaria `America/Bogota`." |
| **AMB-09** | RB09 — *"salvo que el administrador configure lo contrario"* | No dice si la configuración es global, por promoción, o por cliente. Tampoco cuál es el valor por defecto. | "Cada promoción tiene `usosMaximosPorCliente` (entero, por defecto 1). El límite global sigue siendo `cantidadMaximaDeUsos`." |
| **AMB-10** | RNF02 — *"consultas principales"*, *"aproximadamente 2 segundos"*, *"condiciones normales"* | Ninguno de los tres términos es medible. No hay percentil, ni volumen de datos, ni definición de "consulta principal". | Ver sección 14, RNF02-R. |
| **AMB-11** | RNF03 — *"la mayor parte del tiempo"* | No es un SLA. Puede significar 90 % o 99,99 %; la diferencia es de tres órdenes de magnitud en costo. | Ver sección 14, RNF03-R. |
| **AMB-12** | RNF06 — *"agregar posteriormente nuevos servicios"* | Ambigüedad grave: puede significar (a) agregar filas a la tabla `Servicio` — trivial, o (b) agregar nuevos módulos/microservicios — arquitectónicamente costoso. | Ver sección 14, RNF06-R. Interpretación recomendada: (a) más "agregar nuevos módulos funcionales sin modificar los existentes". |
| **AMB-13** | RNF08 — *"operaciones importantes deberán poder rastrearse"* | No define qué operaciones, qué se registra, quién lo consulta, ni por cuánto tiempo se conserva. | Ver sección 14, RNF08-R. |
| **AMB-14** | RNF07 — *"reservas duplicadas o inconsistentes"* | "Duplicada" no está definida: ¿misma mascota + mismo horario? ¿mismo cliente + mismo servicio + mismo día? Lo segundo puede ser legítimo (dos mascotas). | "No pueden existir dos reservas activas que violen RB02 o RB10. La unicidad se garantiza a nivel de base de datos, no solo de aplicación." |
| **AMB-15** | *"Ingresos del mes"* (dashboard) | ¿Suma de pagos `APROBADO` del mes, o de servicios `COMPLETADA`? ¿Neto de reembolsos? ¿Por fecha de pago o de servicio? | Definir la fórmula exacta antes de implementar el dashboard. Ver PRG-31. |
| **AMB-16** | *"Servicios realizados: 91"* (dashboard) | Periodo no indicado (¿mes? ¿histórico?) y no se aclara si equivale a reservas en estado `COMPLETADA`. | Etiquetar cada KPI con su periodo y su definición operativa. |
| **AMB-17** | `Mascota.Peso` | ¿Es el peso actual (mutable) o el del registro? Si el precio depende del peso (GAP-06), un cambio de peso alteraría precios históricos. | "El peso es un dato mutable de la mascota. El precio de una reserva se congela al momento de crearla." |
| **AMB-18** | *"manejo adecuado de sesiones/autenticación"* | "Adecuado" no es un requisito. No dice si hay *refresh token*, revocación, cierre de sesión en servidor ni control de sesiones concurrentes. | Ver DEC-04 y sección 14, RNF01-R. |

---

## 5. Contradicciones e inconsistencias

Estas no son vacíos: son puntos donde el enunciado se contradice consigo mismo. Deben resolverse con el cliente, no con criterio propio.

| ID | Contradicción | Detalle | Resolución sugerida |
|---|---|---|---|
| **CON-01** | **Hospedaje temporal vs. el flujo de reserva** | El flujo es `Servicio → Mascota → Fecha → Hora → Empleado`. El hospedaje es un servicio **multi-día** con fecha de entrada y salida, cuyo recurso escaso es un **cupo/habitación**, no un empleado en una franja horaria. El modelo propuesto no lo soporta. | Sacar hospedaje del MVP (DIS-01) o modelarlo como un tipo de reserva distinto con `fechaInicio`/`fechaFin` y recurso `Cupo`. |
| **CON-02** | **RB04 vs. RB05** | RB04 dice que tras un rechazo el cliente "podrá intentar nuevamente". RB05 dice que la reserva expira a los 15 minutos. Si el rechazo ocurre en el minuto 14, el cliente tiene 60 segundos de reintento. Las dos reglas no fueron escritas pensando la una en la otra. | Definir si el contador se reinicia con cada intento (AMB-07) o si el mensaje de rechazo debe informar el tiempo restante. |
| **CON-03** | **Estados de pago sin transición definida** | `Pago.CANCELADO` no aparece en ninguna regla: no se sabe quién cancela un pago ni desde qué estado. Y RB07 genera una "solicitud de reembolso", pero el único estado disponible es `REEMBOLSADO`, que representa el reembolso ya ejecutado. Falta el estado intermedio. | Añadir `REEMBOLSO_SOLICITADO` (o `EN_REEMBOLSO`), o documentar la solicitud como una entidad aparte con su propio ciclo de vida. |
| **CON-04** | **Consulta veterinaria vs. el modelo de datos** | La consulta veterinaria implica un acto médico. `Mascota` solo tiene `Observaciones` (texto libre). No hay historia clínica, diagnóstico, tratamiento ni vacunas. O el servicio es solo agendamiento, o el modelo está muy incompleto. | Acotar explícitamente a **agendamiento** en el MVP (DIS-02). |
| **CON-05** | **RB02 sin jornada laboral** | RB02 define disponibilidad únicamente como ausencia de otra reserva. Combinado con AMB-01 sin resolver, el sistema permitiría agendar a un empleado a las 03:00 de un 25 de diciembre. | Extender RB02: disponibilidad = dentro de jornada **y** sin solapamiento **y** sin bloqueo/ausencia. |
| **CON-06** | **Diagrama de arquitectura y pasarela** | En el diagrama la pasarela cuelga del nivel de datos, por debajo de MySQL/MongoDB. La pasarela es una integración externa del backend, y además tiene un canal **entrante** (webhook) que el diagrama no representa. | Corregir el diagrama. Ver sección 13. |
| **CON-07** | **Panel administrativo vs. rol Empleado** | Se dice que "el personal de PetCare" tendrá un panel administrativo, pero luego el dashboard y los filtros se atribuyen solo al administrador, y las funciones del empleado quedan pendientes. | Definir la matriz de permisos (Anexo B) antes del Sprint 1. |
| **CON-08** | **RB10 (mascota) sin equivalente para el cliente** | Se prohíbe que una mascota tenga reservas solapadas, pero no se dice nada del cliente. Si el cliente debe estar presente (llevar la mascota al local), dos reservas simultáneas de dos mascotas suyas son físicamente imposibles. | Definir si existe una RB11 análoga para el cliente, y si depende de la modalidad del servicio. |

---

## 6. Funcionalidades ausentes del enunciado

El enunciado es detallado, pero omite funcionalidades sin las cuales el sistema **no puede operar**. No son mejoras opcionales; son piezas faltantes.

| ID | Funcionalidad ausente | Por qué es imprescindible |
|---|---|---|
| **GAP-25** | **Recuperación de contraseña** | Un sistema con registro propio y sin recuperación de contraseña genera cuentas inutilizables desde el primer olvido. No aparece en ningún objetivo ni sprint. |
| **GAP-26** | **Verificación de correo electrónico** | Todo el sistema de notificaciones (objetivo 10) depende de que el correo sea real. Sin verificación, las confirmaciones se pierden y no hay forma de saberlo. |
| **GAP-27** | **Proceso automático de expiración (job programado)** | RB05 exige liberar horarios tras 15 minutos. Eso requiere un planificador que el enunciado no menciona en la arquitectura ni en los sprints. Lo mismo aplica al recordatorio "mañana tienes una reserva". |
| **GAP-28** | **Endpoint receptor de webhooks de la pasarela** | RB03 depende de que "PetCare recibe confirmación". Ese es un endpoint público, no autenticado por JWT, que debe validar firma y ser idempotente. Es un caso especial frente al requisito general de "protección de endpoints". |
| **GAP-29** | **Gestión de empleados (CRUD y agenda)** | El Sprint 2 se llama "Usuarios y mascotas", pero la creación de empleados, la asignación de servicios que puede prestar y su disponibilidad no aparecen explícitamente. Sin esto el Sprint 4 (reservas) no tiene contra qué validar. |
| **GAP-30** | **Perfil del cliente y edición de datos** | Objetivo 1 es "crear una cuenta"; no se menciona editar datos, cambiar contraseña ni eliminar la cuenta (esto último exigido por la Ley 1581). |

---

## 7. Banco de preguntas para el cliente

> **Pregunta respondida: ¿Qué preguntas le podría hacer al cliente?**

Prioridad: **P0** = bloquea el diseño · **P1** = bloquea un sprint específico · **P2** = puede resolverse durante el sprint.

### 7.1 Modelo de negocio y operación

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-01 | ¿Cuál es el horario de atención por día de la semana? ¿Se atiende domingos y festivos? | P0 | GAP-01 |
| PRG-02 | ¿PetCare tiene local físico, opera solo a domicilio, o ambos? ¿Cuántas sedes? | P0 | GAP-03 |
| PRG-03 | ¿Cuántos empleados hay hoy y qué servicios presta cada uno? ¿Un empleado puede prestar varios servicios distintos? | P0 | GAP-02, GAP-29 |
| PRG-04 | En un paseo, ¿el paseador lleva una sola mascota o varias a la vez? Si son varias, ¿cuál es el máximo? | P0 | GAP-02 |
| PRG-05 | ¿Qué zonas o barrios cubren los servicios a domicilio? ¿Se cobra distinto según la zona? | P1 | GAP-04 |
| PRG-06 | ¿Cuánto tiempo de desplazamiento hay que dejar entre dos servicios a domicilio consecutivos del mismo empleado? | P1 | GAP-04 |
| PRG-07 | ¿La duración de cada servicio es fija, o depende del tamaño/raza de la mascota? | P1 | GAP-05 |
| PRG-08 | ¿El precio de cada servicio es fijo, o varía por peso, tamaño, raza o duración? Un ejemplo concreto de cada servicio sería muy útil. | P0 | GAP-06 |
| PRG-09 | ¿Se seguirá atendiendo por teléfono/WhatsApp cuando exista la plataforma? Si sí, ¿el administrador debe poder crear reservas a nombre de un cliente? | P0 | GAP-08, RSG-13 |
| PRG-10 | ¿Con cuánta antelación mínima puede reservarse un servicio? ¿Y con cuánta anticipación máxima (ej. hasta 3 meses)? | P1 | AMB-01 |
| PRG-11 | ¿Los horarios se manejan en bloques fijos (ej. cada 30 min) o el cliente elige cualquier hora? | P1 | AMB-01 |

### 7.2 Roles y permisos

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-12 | ¿Qué puede hacer exactamente un **Empleado** en el sistema? ¿Ve solo su agenda o la de todos? ¿Puede cambiar el estado de sus reservas? ¿Puede cancelar? | P0 | GAP-09, CON-07 |
| PRG-13 | ¿El empleado puede bloquear su propia agenda (ausencia, almuerzo, vacaciones) o solo el administrador? | P0 | GAP-07 |
| PRG-14 | ¿Existe un cuarto rol implícito, como un recepcionista con permisos intermedios? | P1 | — |
| PRG-15 | ¿Quién crea las cuentas de empleado y administrador? ¿Se auto-registran o los crea el administrador? | P1 | GAP-29 |
| PRG-16 | ¿Un empleado puede además ser cliente (reservar para su propia mascota)? | P2 | — |

### 7.3 Reservas

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-17 | ¿El cliente elige a un empleado específico, o el sistema lo asigna? ¿Debe existir la opción "cualquiera disponible"? | P0 | AMB-05 |
| PRG-18 | ¿El cliente puede **reprogramar** una reserva, o solo cancelar y volver a reservar? | P0 | GAP-10 |
| PRG-19 | ¿Se necesitan reservas recurrentes (ej. paseo todos los martes y jueves)? ¿Entra en el MVP? | P0 | GAP-11 |
| PRG-20 | ¿Se pueden reservar dos servicios distintos para la misma mascota el mismo día (ej. baño y luego paseo)? | P1 | RB10 |
| PRG-21 | ¿Qué pasa si PetCare debe cancelar (empleado enfermo)? ¿Reembolso total, reasignación automática, reprogramación? | P1 | GAP-12 |
| PRG-22 | ¿Quién marca una reserva como `NO_ASISTIO` y qué consecuencia tiene para el cliente y para el pago? | P1 | GAP-13 |
| PRG-23 | ¿Quién y cómo cambia una reserva a `EN_PROCESO` y a `COMPLETADA`? ¿Desde el navegador, en el momento del servicio? | P0 | GAP-09 |
| PRG-24 | ¿Debe el cliente indicar una dirección de servicio, y puede ser distinta a la de su perfil? | P1 | GAP-04 |

### 7.4 Pagos

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-25 | ¿Qué métodos de pago deben aceptarse? (tarjeta crédito/débito, PSE, Nequi, Daviplata, efectivo en sitio) | P0 | GAP-14 |
| PRG-26 | ¿Se acepta pago en efectivo o contra-entrega? **Si la respuesta es sí, RB03 debe reescribirse.** | P0 | GAP-14, RB03 |
| PRG-27 | ¿El pago es siempre del 100 % por anticipado, o se admite un abono? | P0 | GAP-15 |
| PRG-28 | ¿Existe ya una cuenta de comercio en alguna pasarela? ¿Quién la provee para el proyecto? ¿Hay acceso a ambiente de pruebas (sandbox)? | P0 | DEC-01, RSG-01 |
| PRG-29 | El reembolso de RB07, ¿se ejecuta automáticamente contra la pasarela, o es un trámite que el administrador gestiona por fuera y solo registra en el sistema? ¿En cuántos días hábiles? ¿Se retiene algún porcentaje? | P0 | GAP-16 |
| PRG-30 | ¿Los precios mostrados incluyen IVA? ¿Se debe emitir factura electrónica? | P1 | GAP-19 |
| PRG-31 | ¿Qué cuenta como "ingreso del mes" en el dashboard: pagos aprobados en el mes, o servicios completados en el mes? ¿Se descuentan los reembolsos? | P1 | AMB-15 |
| PRG-32 | Si un pago se aprueba después de que la reserva expiró (minuto 16), ¿qué debe hacer el sistema: reembolsar automáticamente, o intentar revalidar el horario? | P0 | GAP-18, RSG-04 |

### 7.5 Promociones

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-33 | ¿Las promociones entran en el MVP o pueden quedar para una segunda entrega? | P0 | DIS-05 |
| PRG-34 | ¿Una promoción aplica a todos los servicios o puede limitarse a algunos? ¿Puede limitarse a clientes nuevos? | P1 | GAP-20 |
| PRG-35 | ¿Solo descuento porcentual, o también monto fijo? ¿Hay tope máximo de descuento o monto mínimo de compra? | P1 | GAP-21 |
| PRG-36 | ¿Se pueden acumular dos promociones en la misma reserva? | P2 | GAP-21 |
| PRG-37 | Si una reserva con promoción expira o se cancela, ¿el cliente recupera el derecho a usar ese código? | P1 | GAP-22 |

### 7.6 Notificaciones

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-38 | ¿Qué eventos exactamente deben notificarse, y a quién? (Propuesta de lista completa en la sección 9, SUP-10) | P1 | GAP-23 |
| PRG-39 | ¿A qué hora debe salir el recordatorio de "mañana tienes una reserva"? | P2 | GAP-23 |
| PRG-40 | ¿Los empleados deben recibir notificación de nuevas reservas asignadas o de cancelaciones? | P1 | GAP-23 |
| PRG-41 | ¿Existe un correo institucional y un dominio propio de PetCare para enviar los correos? | P1 | RSG-05 |
| PRG-42 | ¿Las notificaciones deben quedar registradas en el sistema (bandeja "mis notificaciones") o basta con el correo? | P2 | — |

### 7.7 Legal, datos y contenido

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-43 | ¿Existe una política de tratamiento de datos personales? El registro debe incluir la autorización (Ley 1581 de 2012). | P1 | GAP-24 |
| PRG-44 | ¿El cliente debe poder eliminar su cuenta y sus datos? ¿Qué pasa con su historial de reservas y pagos? | P1 | GAP-24, GAP-30 |
| PRG-45 | En consulta veterinaria, ¿el sistema debe guardar diagnóstico o historia clínica, o únicamente agendar la cita? | P0 | CON-04, DIS-02 |
| PRG-46 | ¿Se deben subir fotos de las mascotas? Si sí, define necesidad de almacenamiento de archivos. | P2 | — |

### 7.8 Alcance, entrega y evaluación

| ID | Pregunta | Prio | Relacionado |
|---|---|---|---|
| PRG-47 | ¿Cuál es la fecha límite de entrega y cuántas semanas dura cada sprint? | P0 | RSG-02 |
| PRG-48 | ¿El proyecto lo desarrolla una sola persona o un equipo? ¿Cuántas horas semanales están disponibles? | P0 | RSG-02 |
| PRG-49 | ¿Qué se considera "entregado y aceptado"? ¿Basta con funcionar en local, o debe estar desplegado y accesible por URL? | P0 | DEC-05 |
| PRG-50 | ¿Hay una cobertura mínima de pruebas exigida (porcentaje)? ¿Se evalúa la documentación además del código? | P1 | RSG-14 |
| PRG-51 | Si hubiera que recortar alcance por tiempo, ¿qué funcionalidades son irrenunciables y cuáles sacrificables? | P0 | Sección 11 |

---

## 8. Preguntas bloqueantes para construir el Product Backlog

> **Pregunta respondida: ¿Qué preguntas necesitaría hacerle al cliente antes de construir el Product Backlog?**

De las 51 preguntas anteriores, **estas 12 son las que impiden escribir historias de usuario**. Sin ellas, cualquier backlog que se escriba tendrá que reescribirse.

| # | Pregunta | ID | Por qué bloquea el backlog |
|---|---|---|---|
| 1 | ¿Qué puede hacer exactamente el rol **Empleado**? | PRG-12 | Es un tercio del sistema. Sin esto no existen las historias del Sprint 6 ni las transiciones de estado del Sprint 4. |
| 2 | ¿Qué servicios entran en el MVP? ¿Hospedaje y veterinaria incluidos? | PRG-45, DIS-01 | Hospedaje exige un motor de reservas diferente. Define si el Sprint 4 es uno o dos sprints. |
| 3 | ¿El precio es fijo o variable? | PRG-08 | Determina el modelo de datos de `Servicio` y el cálculo de la orden de pago. |
| 4 | ¿Cuál es el horario de atención y cómo se modela la disponibilidad del empleado? | PRG-01, PRG-13 | Sin esto no hay historia "consultar disponibilidad", que es el corazón del producto. |
| 5 | ¿Qué métodos de pago se aceptan, y se acepta efectivo? | PRG-25, PRG-26 | Si hay efectivo, RB03 cambia y con ella todo el ciclo de vida de la reserva. |
| 6 | ¿Hay cuenta de pasarela y sandbox disponible? | PRG-28 | Si no la hay, el requisito "obligatorio" de pasarela no es ejecutable y hay que negociar una simulación. |
| 7 | ¿El reembolso es automático o manual? | PRG-29 | Diferencia entre una historia de 2 puntos y una integración de 8. |
| 8 | ¿Se requiere reprogramación de reservas? | PRG-18 | Es una historia grande que hoy no está en ningún sprint. |
| 9 | ¿Se requieren reservas recurrentes? | PRG-19 | Cambia el modelo de datos de `Reserva` de raíz. |
| 10 | ¿Las promociones entran en el MVP? | PRG-33 | RB08/RB09 son ~4 historias que no tienen sprint asignado en la planificación actual. |
| 11 | ¿Cuál es la fecha de entrega y la capacidad del equipo? | PRG-47, PRG-48 | Sin capacidad no hay estimación en story points ni priorización realista. |
| 12 | ¿Qué es irrenunciable y qué es sacrificable? | PRG-51 | Es la pregunta que define la prioridad de todo el backlog. |

> **Recomendación:** llevar estas 12 preguntas a una sola sesión de trabajo con el cliente, con las respuestas por defecto de la sección 9 ya preparadas. Presentar un supuesto concreto y pedir confirmación es mucho más eficaz que hacer una pregunta abierta: el cliente corrige más rápido de lo que inventa.

---

## 9. Supuestos por defecto

Si el cliente no responde, el proyecto no puede detenerse. Estos son los supuestos que el equipo adoptará, **documentados para que puedan ser refutados**. Cada uno tiene fecha límite implícita: el inicio del sprint que lo consume.

| ID | Supuesto | Sprint que lo consume | Costo de cambiarlo después |
|---|---|---|---|
| **SUP-01** | Horario de atención: lunes a sábado, 08:00–18:00; domingos y festivos cerrado. Zona horaria única `America/Bogota`. | 4 | Bajo (configuración) |
| **SUP-02** | Una sola sede. Los servicios `EN_SEDE` no requieren dirección; los `A_DOMICILIO` usan la dirección del perfil del cliente. | 2 | Medio |
| **SUP-03** | Un empleado atiende **una** reserva a la vez. No hay paseos grupales en el MVP. | 4 | **Alto** — cambia RB02 |
| **SUP-04** | Los horarios se ofrecen en bloques de 30 minutos. Antelación mínima de reserva: 2 horas. Antelación máxima: 60 días. | 4 | Bajo |
| **SUP-05** | El precio es fijo por servicio en el MVP; el diseño de la tabla contempla desde ya una tarifa opcional por rango de peso, para no bloquear la extensión. | 3 | Medio si no se previó |
| **SUP-06** | El cliente elige empleado de una lista de disponibles, con opción "sin preferencia" (asignación automática al de menor carga ese día). | 4 | Medio |
| **SUP-07** | Pago 100 % anticipado, en línea, sin efectivo. Única moneda: COP, sin decimales. | 5 | **Alto** — cambia RB03 |
| **SUP-08** | Integración de pasarela por *checkout redirect* + *webhook* firmado e idempotente, más consulta de estado como respaldo. | 5 | Alto |
| **SUP-09** | El reembolso (RB07) se **registra** en el sistema como solicitud y lo tramita el administrador manualmente. No hay llamada automática de reembolso a la pasarela en el MVP. | 5 | Medio |
| **SUP-10** | Eventos notificados por correo: registro/verificación, recuperación de contraseña, reserva creada (pendiente de pago), pago aprobado, pago rechazado, reserva confirmada, reserva expirada, reserva cancelada, recordatorio 24 h antes, reembolso registrado. Destinatario: cliente. El empleado recibe aviso de asignación y cancelación. | 7 | Bajo |
| **SUP-11** | Reprogramación **no** incluida en el MVP: el cliente cancela y vuelve a reservar. | 4 | Medio |
| **SUP-12** | Reservas recurrentes **no** incluidas en el MVP. | 4 | **Alto** |
| **SUP-13** | El contador de 15 minutos (RB05) **no** se reinicia con los reintentos de pago (RB04). El mensaje de rechazo informa el tiempo restante. | 5 | Bajo |
| **SUP-14** | Consulta veterinaria: solo agendamiento. Sin historia clínica ni diagnóstico. | 3 | **Alto** |
| **SUP-15** | Hospedaje temporal: fuera del MVP, o modelado como reserva por rango de días contra un recurso `Cupo` en una fase posterior. | 4 | **Alto** |
| **SUP-16** | Autenticación con *access token* JWT de corta vida (15 min) + *refresh token* rotativo persistido, revocable al cerrar sesión. | 1 | Medio |
| **SUP-17** | Si una reserva con promoción expira o se cancela, el uso de la promoción se libera y vuelve a estar disponible para el cliente. | 5 | Bajo |
| **SUP-18** | Auditoría: se registran creación, cambio de estado y eliminación de reservas, pagos, servicios, promociones y usuarios, con actor, marca de tiempo, valores previo y nuevo, e IP. | 6 | Medio |

---

## 10. Riesgos iniciales

> **Pregunta respondida: ¿Qué riesgos iniciales se identifican?**

Escala: probabilidad y impacto en **Alta / Media / Baja**. Exposición = combinación de ambas.

### 10.1 Matriz de riesgos

| ID | Riesgo | Prob. | Impacto | Exposición | Mitigación | Disparador de alerta |
|---|---|---|---|---|---|---|
| **RSG-01** | **La integración con la pasarela no se logra a tiempo.** Es un requisito obligatorio y depende de un tercero: aprobación de cuenta de comercio, credenciales de sandbox, documentación cambiante. Además, los webhooks **no llegan a `localhost`**: hace falta un túnel público (ngrok/Cloudflare Tunnel) o desplegar temprano. | Alta | Alto | 🔴 Crítica | Aislar la pasarela tras un puerto `PaymentGateway` con dos implementaciones: la real y una simulada. Desarrollar todo el Sprint 5 contra la simulada; enchufar la real al final. Solicitar credenciales **en el Sprint 0**, no en el 5. | No hay credenciales de sandbox al terminar el Sprint 2. |
| **RSG-02** | **Alcance excesivo para el tiempo y la capacidad disponibles.** Cinco dominios funcionales, dos motores de datos, integración de pagos, correo, panel administrativo, pruebas y despliegue, en 9 sprints y (probablemente) con un desarrollador. | Alta | Alto | 🔴 Crítica | Acordar el MVP explícitamente (sección 11) y aplicar la técnica MoSCoW al backlog. Recortar hospedaje, promociones y MongoDB primero. | Al cerrar el Sprint 3 hay más de 20 % del alcance comprometido sin empezar. |
| **RSG-03** | **Condición de carrera en la reserva (doble agendamiento).** Dos clientes reservan el mismo empleado y horario simultáneamente. Validar con un `SELECT` previo a un `INSERT` **no** lo previene: entre ambas operaciones cabe otra transacción. RNF07 exige que esto no ocurra. | Media | Alto | 🟠 Alta | Restricción `UNIQUE` en base de datos sobre `(empleado_id, inicio)` para bloques fijos, y/o bloqueo pesimista sobre la franja. Pruebas de concurrencia explícitas. No confiar en la validación de la capa de servicio. | — |
| **RSG-04** | **Carrera entre la expiración (RB05) y la confirmación del pago.** El job expira la reserva en el minuto 15; el webhook de aprobación llega en el 15:03. Resultado: dinero cobrado y reserva liberada, posiblemente ya tomada por otro cliente. | Media | Alto | 🟠 Alta | Definir política explícita (PRG-32). Recomendado: al recibir un pago aprobado sobre reserva `EXPIRADA`, intentar revalidar el horario; si ya no está disponible, marcar el pago para reembolso y notificar. Manejar el estado con bloqueo transaccional. | — |
| **RSG-05** | **Los correos no llegan o caen en spam.** Sin dominio propio con SPF/DKIM, los proveedores marcan como spam. Los servicios gratuitos limitan a ~100 correos/día y exigen remitente verificado. El objetivo 10 depende por completo de esto. | Alta | Medio | 🟠 Alta | Elegir proveedor transaccional temprano, verificar remitente en el Sprint 0–1, y persistir cada notificación en base de datos con su estado de envío, para poder demostrar el requisito aunque el correo se pierda. | El primer correo de prueba llega a spam. |
| **RSG-06** | **Los permisos por rol no están definidos (GAP-12).** El modelo de autorización se construye en el Sprint 1, pero los permisos del Empleado se conocerán después. | Alta | Alto | 🔴 Crítica | Obtener la matriz de permisos (Anexo B) **antes** del Sprint 1. Si no llega, implementar autorización por permisos granulares en vez de por rol, para que agregar permisos no obligue a reescribir. | Inicio del Sprint 1 sin matriz aprobada. |
| **RSG-07** | **Hospedaje temporal no encaja en el motor de reservas (CON-01).** Si se descubre en el Sprint 4, obliga a rehacer el modelo de `Reserva`. | Alta | Alto | 🔴 Crítica | Decidirlo en Fase 1, no en el Sprint 4. Si entra, modelarlo como tipo de reserva separado desde el diseño inicial. | — |
| **RSG-08** | **Cumplimiento de datos personales (Ley 1581/2012).** Registro sin autorización de tratamiento, sin política de privacidad y sin mecanismo de eliminación. | Media | Medio | 🟡 Media | Incluir *checkbox* de autorización y política en el Sprint 1; historia de eliminación de cuenta (con anonimización, no borrado, para conservar la integridad contable). | — |
| **RSG-09** | **El despliegue en capa gratuita incumple RNF02 y RNF03.** Los planes gratuitos suspenden la instancia por inactividad; el primer acceso tarda decenas de segundos (*cold start*). Además, MySQL gestionado gratuito es escaso y suele tener caducidad. | Alta | Medio | 🟠 Alta | Reformular RNF02/RNF03 (sección 14) excluyendo el arranque en frío, o presupuestar un plan pago mínimo. Decidir el proveedor tarde pero **probar el despliegue temprano** (Sprint 1), no en el Sprint 8. | — |
| **RSG-10** | **MongoDB añade complejidad sin valor demostrado.** Segundo motor, segundo driver, segundo despliegue, segundo respaldo, y transacciones que no abarcan ambas bases. | Alta | Medio | 🟠 Alta | Diferir (DEC-02). Publicar eventos de auditoría a través de una interfaz `AuditEventPublisher`; implementar primero contra MySQL. Si el volumen lo justifica, cambiar la implementación sin tocar el dominio. | — |
| **RSG-11** | **Zona horaria y manejo de fechas.** Reglas de 15 minutos y de 24 horas mezcladas con `LocalDateTime` sin zona, servidores en UTC y navegadores en `America/Bogota`. Fuente clásica de defectos difíciles de reproducir. | Media | Medio | 🟡 Media | Almacenar todo en UTC (`TIMESTAMP`/`Instant`), convertir solo en la capa de presentación, fijar la zona de la JVM y de MySQL explícitamente, y probar con casos límite. | — |
| **RSG-12** | **Ausencia de proceso programado en la arquitectura (GAP-27).** RB05 y el recordatorio de 24 h exigen un planificador que no está previsto. En despliegues con más de una instancia, un `@Scheduled` simple se ejecuta duplicado. | Media | Medio | 🟡 Media | Incluir el planificador en el diseño de arquitectura. Si hay varias instancias, usar bloqueo distribuido (ShedLock) o marcar la ejecución en base de datos. | — |
| **RSG-13** | **Doble canal de agendamiento.** Si el teléfono sigue operando y las reservas telefónicas no entran al sistema, la disponibilidad que ve el cliente en la web es falsa. | Media | Alto | 🟠 Alta | Exigir que **toda** reserva pase por el sistema, incluidas las telefónicas, mediante creación manual por el administrador. Es tanto un requisito funcional como un cambio de proceso del negocio. | — |
| **RSG-14** | **Pruebas concentradas en el Sprint 8.** La planificación propuesta deja las pruebas para el final. Escribir pruebas de siete sprints en uno es inviable y arruina su propósito. | Alta | Medio | 🟠 Alta | Convertir las pruebas en criterio transversal de *Definition of Done* de cada sprint. Reservar el Sprint 8 solo para pruebas de integración de extremo a extremo y despliegue. | — |
| **RSG-15** | **Funcionalidades ausentes descubiertas tarde (sección 6).** Recuperación de contraseña, verificación de correo, CRUD de empleados y perfil del cliente no están en ningún sprint. | Alta | Medio | 🟠 Alta | Incorporarlas al backlog en Fase 1 (Anexo A). | — |
| **RSG-16** | **Seguridad del endpoint de webhook.** Es público y no autenticado por JWT. Sin validación de firma, un tercero podría confirmar reservas sin pagar. | Media | Alto | 🟠 Alta | Validar firma HMAC del proveedor, exigir idempotencia por identificador de transacción, y **nunca** confiar en el monto que llega en el cuerpo sin contrastarlo contra la orden. | — |
| **RSG-17** | **Datos de tarjeta y alcance PCI.** Si el frontend capturara datos de tarjeta, el proyecto entraría en un alcance de cumplimiento inviable para un trabajo académico. | Baja | Alto | 🟡 Media | Usar exclusivamente checkout alojado por la pasarela o widget tokenizado. Prohibir explícitamente almacenar PAN, CVV o fecha de expiración. Documentarlo como restricción de diseño. | — |
| **RSG-18** | **Dependencia de un solo desarrollador.** Enfermedad, cruce con otras materias o bloqueo técnico detienen el proyecto por completo. | Media | Alto | 🟠 Alta | Repositorio con historial limpio, documentación al día, entregas incrementales desplegables desde el Sprint 2. Priorizar por valor para que un recorte deje algo funcional. | — |

### 10.2 Los cinco riesgos a vigilar

Si solo se puede hacer seguimiento a cinco: **RSG-01** (pasarela), **RSG-02** (alcance), **RSG-06** (permisos sin definir), **RSG-07** (hospedaje) y **RSG-03** (concurrencia). Los cuatro primeros se mitigan con **decisiones del cliente en Fase 1**; el quinto es puramente técnico y se mitiga con diseño.

---

## 11. Funcionalidades discutibles antes de aprobar el alcance

> **Pregunta respondida: ¿Qué funcionalidades se consideran discutibles antes de aprobar el alcance?**

Recomendación: **MVP** (entra) · **v2** (después del MVP) · **Fuera** (no se construye) · **Negociar** (requiere decisión del cliente).

| ID | Funcionalidad | Recomendación | Justificación |
|---|---|---|---|
| **DIS-01** | **Hospedaje temporal** | 🔶 Negociar → preferible **v2** | No encaja en el flujo `fecha + hora + empleado` (CON-01). Requiere reservas por rango de días, gestión de cupos y precio por noche. Es prácticamente un segundo motor de reservas. Incluirlo puede añadir un sprint completo. |
| **DIS-02** | **Consulta veterinaria con historia clínica** | ✅ MVP solo como **agendamiento**; historia clínica **fuera** | Una historia clínica veterinaria (anamnesis, diagnóstico, tratamiento, vacunas, exámenes) es un producto en sí mismo y arrastra requisitos legales de datos. Fuera del objetivo declarado, que es automatizar reservas y pagos. |
| **DIS-03** | **Reembolso automático vía API** | 🔶 Negociar → preferible **v2**; en MVP, solicitud registrada + trámite manual | RB07 dice "se genera una solicitud", lo que ya sugiere un paso manual. La automatización duplica la superficie de integración con la pasarela y su manejo de errores. |
| **DIS-04** | **Reservas recurrentes** | 🔶 Negociar → preferible **v2** | Comercialmente muy relevante para paseos, pero cambia el modelo de datos de raíz (serie vs. ocurrencia) y complica pagos y cancelaciones. Si el cliente la considera esencial, debe decidirse **ahora**, no después. |
| **DIS-05** | **Promociones completas (RB08–RB09)** | 🔶 Negociar → **MVP reducido** | RB08/RB09 no tienen sprint asignado en la planificación propuesta. Alternativa reducida: código + porcentaje + vigencia + máximo de usos global + un uso por cliente. Dejar para v2 el ámbito por servicio, los topes y la acumulabilidad. |
| **DIS-06** | **MongoDB para logs/eventos** | ❌ **Fuera del MVP** | Ver RSG-10 y DEC-02. RNF08 se cumple con una tabla de auditoría en MySQL más registro estructurado a `stdout`. Añadir un segundo motor sin volumen que lo justifique es sobre-ingeniería. |
| **DIS-07** | **Gráficos del dashboard** | ✅ MVP reducido: KPIs numéricos; gráficos en **v2** | Los cuatro indicadores numéricos aportan la mayor parte del valor con una fracción del esfuerzo. Los gráficos son atractivos para la sustentación pero no cambian decisiones del negocio. |
| **DIS-08** | **Reprogramación de reservas** | 🔶 Negociar | Muy pedida por usuarios reales. Si entra, debe hacerlo en el Sprint 4 con el resto del motor de reservas; añadirla luego cuesta más. |
| **DIS-09** | **Multi-sede** | ❌ **Fuera** salvo que el negocio ya tenga varias sedes | Añade una dimensión a disponibilidad, empleados y precios. Si hay una sola sede, es complejidad sin beneficio. |
| **DIS-10** | **Calificaciones y reseñas de servicios** | ❌ **Fuera** | No está en los objetivos. Es la típica funcionalidad que se cuela y consume un sprint. |
| **DIS-11** | **Notificaciones por WhatsApp o SMS** | ❌ **Fuera** (el enunciado ya dice "por ahora" correo) | Confirmar explícitamente con el cliente para que no reaparezca a mitad del proyecto. |
| **DIS-12** | **Aplicación móvil para empleados** | ❌ **Fuera** | El requisito es una plataforma **web**. Con RNF04 (responsive) el empleado puede operar desde el navegador del teléfono. |
| **DIS-13** | **Chat o soporte en línea** | ❌ **Fuera** | No aparece en los objetivos. |
| **DIS-14** | **Fotos de mascotas** | 🟡 v2 | Requiere almacenamiento de archivos (S3/Cloudinary), validación de tipo y tamaño. Bajo valor frente al costo en el MVP. |
| **DIS-15** | **Paseos grupales (varias mascotas por paseador)** | 🔶 Negociar — **decisión P0** | Si el negocio los ofrece, RB02 y el modelo de disponibilidad cambian de "1 empleado = 1 reserva" a un modelo de capacidad. No es un añadido posterior: es un supuesto estructural. |
| **DIS-16** | **Creación de reservas por el administrador (canal telefónico)** | ✅ **MVP** | Aunque no está en el enunciado, sin esto la disponibilidad mostrada al cliente es inconsistente con la realidad (RSG-13). |
| **DIS-17** | **Recuperación de contraseña y verificación de correo** | ✅ **MVP, no negociable** | Ver GAP-25 y GAP-26. Sin ellas el sistema no es usable. |

---

## 12. Decisiones técnicas que aún no deben tomarse

> **Pregunta respondida: ¿Qué decisiones técnicas todavía no deberían tomarse?**

Se distingue entre decisiones **irreversibles** (tipo 1: costosas de deshacer, hay que pensarlas) y **reversibles** (tipo 2: se prueban y se cambian). El error es tratar las de tipo 2 como si fueran de tipo 1, y viceversa.

| ID | Decisión | Tipo | Por qué no ahora | Qué hacer mientras tanto | Cuándo decidir |
|---|---|---|---|---|---|
| **DEC-01** | **Pasarela concreta: Wompi vs. Mercado Pago vs. PayU** | 1 | Depende de PRG-25 (métodos requeridos: ¿PSE?, ¿Nequi?), PRG-28 (quién provee la cuenta) y de la calidad del sandbox. Elegir hoy es elegir a ciegas. | Definir ahora el **patrón**: checkout alojado + webhook firmado + consulta de estado de respaldo. Diseñar el puerto `PaymentGateway` (`crearOrden`, `consultarEstado`, `procesarWebhook`) e implementar primero un adaptador simulado. | Sprint 0–1 (la cuenta debe solicitarse temprano por tiempos de trámite); implementación real, Sprint 5. |
| **DEC-02** | **Incluir MongoDB** | 1 | No hay ningún requisito que lo exija. RNF08 no menciona volumen, esquema variable ni analítica. Añadirlo "porque estaba en el diagrama" es una decisión sin evidencia. | Interfaz `AuditEventPublisher` con implementación sobre tabla MySQL. Registro estructurado en JSON a `stdout`. Si aparece volumen o necesidad de búsqueda, se cambia la implementación sin tocar el dominio. | Sprint 6, con datos reales de volumen. Por defecto: **no**. |
| **DEC-03** | **Proveedor de despliegue (Render, Railway, AWS, Azure)** | 2 | Depende de PRG-49 (¿debe estar público?), del presupuesto y de si RNF02/RNF03 se reformulan (RSG-09). | Contenerizar con Docker y externalizar toda la configuración a variables de entorno (12-factor). Así el proveedor deja de ser una decisión estructural. **Probar un despliegue real en el Sprint 1**, aunque sea provisional. | Sprint 2–3 (no en el 8). |
| **DEC-04** | **Estrategia de tokens: solo access token vs. access + refresh; revocación** | 1 (parcialmente) | Depende de PRG-49 y del comportamiento esperado de sesión (¿cuánto dura?, ¿se cierra sesión en todos los dispositivos?). Un JWT sin estado no se puede revocar; añadir revocación después implica infraestructura nueva. | Adoptar SUP-16 como supuesto de trabajo y **aislar la emisión y validación de tokens en una sola clase**, para que el cambio quede contenido. | Sprint 1, tras confirmar PRG-12 y PRG-49. |
| **DEC-05** | **Esquema definitivo de la base de datos** | 1 | Depende de PRG-08 (precio fijo/variable), PRG-19 (recurrencia), DIS-01 (hospedaje) y PRG-04 (capacidad). Cuatro respuestas pendientes que cambian tablas centrales. | Modelar solo el núcleo estable (`Usuario`, `Rol`, `Mascota`) y usar migraciones versionadas (Flyway/Liquibase) desde el primer día, para que evolucionar el esquema sea rutina y no crisis. | Sprint 2–3, por partes. |
| **DEC-06** | **Modelo de autorización: por rol vs. por permisos granulares** | 1 | Los permisos del Empleado son desconocidos (GAP-12). Un modelo por rol cableado a tres constantes se rompe en cuanto aparezca un cuarto rol o un permiso a medida. | Modelar `Rol` 1—N `Permiso` en base de datos desde el inicio, aunque en el MVP haya exactamente tres roles. El costo adicional es bajo; el de migrar después, alto. | Sprint 1, con la matriz del Anexo B en mano. |
| **DEC-07** | **Proveedor de correo transaccional** | 2 | Depende de PRG-41 (¿hay dominio propio?) y de los límites de envío gratuitos. | Programar contra la abstracción `NotificationSender` de Spring (`JavaMailSender`), con plantillas separadas. Cambiar de proveedor debe ser cuestión de configuración. | Sprint 7, pero **verificar el remitente en el Sprint 1** (RSG-05). |
| **DEC-08** | **Estrategia de concurrencia: bloqueo optimista vs. pesimista vs. restricción única** | 1 | Depende de si los horarios son bloques fijos o continuos (PRG-11) y de si el recurso es el empleado o un cupo (PRG-04). Con bloques fijos basta una restricción `UNIQUE`; con horarios continuos hace falta detección de solapamiento con bloqueo. | Dejar decidido que **la garantía vive en la base de datos**, no solo en la capa de servicio, y escribir la prueba de concurrencia antes que la solución. | Sprint 4, tras PRG-11. |
| **DEC-09** | **Tailwind CSS vs. alternativa; biblioteca de componentes** | 2 | Reversible y de bajo impacto. No merece análisis prolongado. | Elegir rápido (Tailwind es una opción razonable con la experiencia previa) y seguir. **Advertencia:** no convertir esta decisión de tipo 2 en un debate largo mientras DEC-01 y DEC-06 siguen abiertas. | Sprint 1, en minutos. |
| **DEC-10** | **Gestión de estado en React (Context, Redux, TanStack Query)** | 2 | Depende de la complejidad real de las pantallas, que aún no está definida. | Empezar con estado local + Context para autenticación. Introducir una biblioteca solo cuando el dolor sea evidente. | Sprint 4, si hace falta. |
| **DEC-11** | **Métricas y fórmulas exactas del dashboard** | 2 | Depende de AMB-15 y PRG-31: no está definido qué cuenta como ingreso. | Diseñar la interfaz del panel con los KPIs como marcadores de posición y no implementar los cálculos hasta tener las fórmulas. | Sprint 6. |
| **DEC-12** | **Orquestación de contenedores / CI-CD** | 2 | El proyecto tiene una sola instancia. Kubernetes o similar sería complejidad injustificada. | `docker-compose` para desarrollo local y, si el tiempo lo permite, una acción de GitHub que ejecute las pruebas en cada *push*. | Sprint 8, opcional. |

### 12.1 Decisiones que **sí** conviene tomar ya

Posponer todo también es un error. Estas conviene cerrarlas en Fase 1 porque estructuran el trabajo y son baratas ahora:

1. **Arquitectura interna del backend:** monolito modular por dominios (`auth`, `usuarios`, `mascotas`, `servicios`, `reservas`, `pagos`, `promociones`, `notificaciones`), cada uno con sus capas `controller` / `service` / `repository` / `domain`, y con las integraciones externas detrás de puertos. Cumple RNF05 y RNF06 sin microservicios.
2. **Todas las integraciones externas van detrás de una interfaz propia** (pasarela, correo, almacenamiento). Es lo que permite diferir DEC-01, DEC-07 y DEC-03 sin bloquear el desarrollo.
3. **Migraciones de base de datos versionadas desde el commit inicial** (Flyway o Liquibase).
4. **Fechas y horas en UTC en la base de datos**, conversión solo en presentación (RSG-11).
5. **Nunca almacenar datos de tarjeta** (RSG-17). Restricción de diseño, no una opción.
6. **Contraseñas con BCrypt** (RNF01), factor de coste ≥ 10.
7. **Pruebas como parte del *Definition of Done* de cada sprint**, no como sprint final (RSG-14).

---

## 13. Análisis crítico de la arquitectura propuesta

### 13.1 Qué está bien

React + Spring Boot + MySQL sobre una API REST es una elección sólida, alineada con la experiencia previa y con el objetivo académico. La separación frontend/backend permite trabajar los sprints de forma incremental y cumple RNF04 y RNF05 sin esfuerzo adicional.

### 13.2 Qué está mal o incompleto en el diagrama

| Observación | Detalle |
|---|---|
| **La pasarela está en el lugar equivocado** | Aparece por debajo del nivel de datos, colgando de MySQL/MongoDB. Es una integración externa del backend, al mismo nivel conceptual que el servicio de correo. |
| **Falta el canal entrante de la pasarela** | El diagrama solo muestra la salida hacia la pasarela. La confirmación de pago (RB03) llega **de vuelta** por webhook, hacia un endpoint público del backend. Ese flujo entrante es el más delicado del sistema (RSG-16) y no está representado. |
| **Falta el servicio de correo** | El objetivo 10 y el sistema de notificaciones son requisitos explícitos, pero no aparecen en la arquitectura. |
| **Falta el planificador de tareas** | RB05 (expiración a 15 min) y el recordatorio de 24 h requieren ejecución programada (GAP-27, RSG-12). No está representado. |
| **MongoDB no tiene justificación** | Ver 13.3. |

### 13.3 Sobre MongoDB: cuándo sí y cuándo no

MongoDB para logs y eventos es una decisión válida **bajo ciertas condiciones**, ninguna de las cuales está demostrada en este proyecto:

| Condición que justificaría MongoDB | ¿Se cumple aquí? |
|---|---|
| Volumen de eventos que degradaría el rendimiento transaccional de MySQL (millones de registros/mes) | ❌ No hay estimación de volumen. Un negocio con ~24 reservas diarias genera un volumen trivial. |
| Esquema de eventos genuinamente variable entre tipos | ⚠️ Parcialmente: un campo JSON en MySQL lo resuelve. MySQL 8 soporta columnas `JSON` con índices funcionales. |
| Retención larga con datos que no deben ocupar la base transaccional | ❌ No hay política de retención definida (AMB-13). |
| Consultas analíticas o agregaciones pesadas sobre los eventos | ❌ El dashboard consulta datos de negocio (reservas, pagos), no logs. |

**Costos ciertos de incluirlo:** un segundo motor que desplegar, respaldar y monitorear; un segundo conjunto de dependencias y configuración; y, sobre todo, **la imposibilidad de tener una transacción que abarque las dos bases**: si se guarda la reserva en MySQL y el evento de auditoría en MongoDB, uno de los dos puede fallar y quedar inconsistente, justo en el requisito (RNF08) que se pretendía reforzar.

**Recomendación:** RNF08 se satisface con una tabla `auditoria` en MySQL (actor, acción, entidad, id de entidad, valor previo, valor nuevo, marca de tiempo, IP) escrita en la misma transacción que la operación auditada, más registro estructurado en JSON a `stdout` para diagnóstico operativo. Se mantiene la interfaz `AuditEventPublisher` para poder migrar sin tocar el dominio si algún día el volumen lo justifica.

Si el requisito académico exige demostrar el uso de una base NoSQL, conviene decirlo explícitamente: es un requisito válido, pero **es un requisito académico, no arquitectónico**, y debe documentarse como tal en lugar de justificarse con razones técnicas que no aplican.

### 13.4 Arquitectura propuesta (corregida)

```
                    ┌──────────────────────────┐
                    │   React SPA (browser)    │
                    │  Cliente · Empleado ·    │
                    │       Administrador      │
                    └────────────┬─────────────┘
                                 │  HTTPS / REST + JWT
                                 ▼
         ┌───────────────────────────────────────────────┐
         │           Spring Boot — monolito modular      │
         │                                               │
         │  auth · usuarios · mascotas · servicios       │
         │  reservas · pagos · promociones · notif.      │
         │                                               │
         │  ┌─────────────────────────────────────────┐  │
         │  │ Puertos (interfaces)                    │  │
         │  │  PaymentGateway · NotificationSender    │  │
         │  │  AuditEventPublisher                    │  │
         │  └─────────────────────────────────────────┘  │
         │                                               │
         │  Scheduler: expiración RB05 · recordatorios   │
         └───┬───────────────┬──────────────┬────────────┘
             │               │              │
             ▼               ▼              ▼
      ┌────────────┐  ┌─────────────┐  ┌──────────────────┐
      │   MySQL    │  │  Pasarela   │  │  Correo (SMTP /  │
      │  negocio + │  │  de pago    │  │  API transacc.)  │
      │  auditoría │  │  (externa)  │  └──────────────────┘
      └────────────┘  └──────┬──────┘
                             │
                  webhook firmado (entrante)
                             │
                             ▼
              POST /api/pagos/webhook  ── endpoint público,
              validación HMAC + idempotencia, sin JWT
```

---

## 14. Requisitos no funcionales reformulados

Los RNF del enunciado son intenciones, no requisitos: la mayoría no es verificable. Un RNF debe poder probarse con un procedimiento concreto que dé un resultado inequívoco.

| Original | Reformulación verificable | Cómo se verifica |
|---|---|---|
| **RNF01** — Las contraseñas nunca deben almacenarse en texto plano. | **RNF01-R.** Las contraseñas se almacenan con BCrypt, factor de coste ≥ 10. Toda comunicación va sobre HTTPS. Las contraseñas nunca aparecen en registros, respuestas de API ni mensajes de error. Longitud mínima de 8 caracteres. Tras 5 intentos fallidos en 15 minutos, la cuenta se bloquea temporalmente por 15 minutos. | Inspección de la tabla `usuario`; prueba automatizada que verifica que la respuesta del login no contiene el hash; revisión de registros. |
| **RNF02** — Consultas principales en menos de ~2 segundos. | **RNF02-R.** Con un conjunto de datos de referencia (500 clientes, 800 mascotas, 5.000 reservas, 5.000 pagos), el percentil 95 del tiempo de respuesta de servidor de los endpoints críticos —consulta de disponibilidad, listado de reservas del cliente, catálogo de servicios y dashboard— es ≤ 1.500 ms, con 20 usuarios concurrentes. Se excluye explícitamente el arranque en frío de la infraestructura de despliegue. | Prueba de carga (JMeter o k6) con el conjunto de datos sembrado; informe de percentiles adjunto a la entrega. |
| **RNF03** — Disponible la mayor parte del tiempo. | **RNF03-R.** El sistema está disponible ≥ 99 % del tiempo en horario de atención (SUP-01), medido mensualmente. Se excluyen las ventanas de mantenimiento anunciadas y la latencia de arranque en frío del proveedor. Un error de una integración externa (pasarela o correo) no debe impedir el resto de la operación: el sistema degrada de forma controlada. | Monitoreo con un servicio de *uptime* sobre un endpoint `/actuator/health`; prueba de degradación con la pasarela simulada caída. |
| **RNF04** — Interfaz responsive. | **RNF04-R.** La interfaz funciona sin desplazamiento horizontal ni superposición de elementos en anchos de 360 px, 768 px y 1280 px, en las dos últimas versiones estables de Chrome, Firefox, Safari y Edge. Todos los flujos principales (registro, reserva, pago, consulta y cancelación) son completables en 360 px. Contraste de texto conforme a WCAG 2.1 nivel AA. | Recorrido manual de los cinco flujos en los tres anchos, con capturas; verificación de contraste con herramienta automática. |
| **RNF05** — Backend organizado y desacoplado. | **RNF05-R.** El backend se organiza en módulos por dominio, cada uno con capas `controller` / `service` / `repository` / `domain`. Los controladores no acceden a repositorios directamente. Las entidades JPA no se exponen en la API: toda entrada y salida usa DTO. Toda integración externa se consume a través de una interfaz definida en el dominio. No existen dependencias cíclicas entre módulos. | Revisión de código y pruebas de arquitectura automatizadas (ArchUnit). |
| **RNF06** — Permitir agregar nuevos servicios. | **RNF06-R.** (a) Agregar un nuevo servicio comercial al catálogo es una operación de datos desde el panel administrativo, sin despliegue ni cambios de código. (b) Agregar un nuevo módulo funcional no requiere modificar los módulos existentes más allá de su interfaz pública. | (a) Prueba funcional: crear un servicio nuevo y reservarlo de extremo a extremo. (b) Revisión de diseño. |
| **RNF07** — Sin reservas duplicadas o inconsistentes. | **RNF07-R.** No pueden persistirse dos reservas activas (`PENDIENTE_PAGO`, `CONFIRMADA`, `EN_PROCESO`) que violen RB02 o RB10. La garantía se implementa en la base de datos —restricción de unicidad o bloqueo transaccional—, no únicamente en la capa de servicio. Existe una prueba automatizada que lanza N peticiones concurrentes sobre el mismo horario y verifica que exactamente una tiene éxito. | Prueba de concurrencia automatizada; inspección del esquema. |
| **RNF08** — Operaciones importantes rastreables. | **RNF08-R.** Se registra en una bitácora de auditoría toda creación, modificación de estado y eliminación sobre: usuarios, reservas, pagos, servicios y promociones. Cada registro contiene: actor (id de usuario), acción, entidad y su identificador, valor previo, valor nuevo, marca de tiempo UTC e IP de origen. El registro se escribe en la misma transacción que la operación auditada. Retención mínima: 12 meses. El administrador puede consultarla filtrando por actor, entidad y rango de fechas. | Prueba de integración que ejecuta una operación y verifica el registro correspondiente; revisión de la pantalla de consulta. |
| — | **RNF09-R (nuevo) — Seguridad de la integración de pagos.** El sistema nunca almacena, registra ni transmite números de tarjeta, CVV o fecha de expiración. El endpoint de webhook valida la firma del proveedor, es idempotente por identificador de transacción y verifica que el monto recibido coincida con el de la orden antes de confirmar. | Revisión de código; prueba con firma inválida (debe rechazar), con evento duplicado (debe ignorar el segundo) y con monto alterado (debe rechazar). |
| — | **RNF10-R (nuevo) — Trazabilidad de notificaciones.** Cada notificación enviada se persiste con destinatario, tipo de evento, marca de tiempo y estado de envío (`ENVIADO` / `FALLIDO`), con al menos un reintento ante fallo. | Prueba de integración con el proveedor de correo simulado. |

---

## 15. Criterios de salida de la Fase 1

La Fase 1 se considera terminada cuando:

- [ ] El cliente ha respondido las **12 preguntas bloqueantes** de la sección 8.
- [ ] La **matriz de permisos** por rol (Anexo B) está completa y aprobada.
- [ ] El **alcance del MVP** está acordado por escrito: cada elemento de la sección 11 está marcado como MVP, v2 o Fuera.
- [ ] Las **contradicciones CON-01 a CON-08** están resueltas, en particular CON-01 (hospedaje) y CON-02 (RB04 vs. RB05).
- [ ] Los **RNF reformulados** de la sección 14 están aprobados en reemplazo de los originales.
- [ ] Los **supuestos SUP-01 a SUP-18** están confirmados o corregidos.
- [ ] Está confirmado si existe **cuenta y sandbox de pasarela de pago** (PRG-28). *Este es el punto de mayor riesgo del proyecto.*
- [ ] Están definidas la **fecha de entrega**, la **duración del sprint** y la **capacidad semanal** del equipo.
- [ ] El **Product Backlog inicial** está redactado con historias, prioridad MoSCoW y estimación en story points.

> **Regla práctica:** ninguna línea de código de dominio debería escribirse antes de cerrar los cuatro primeros puntos. La configuración del proyecto, el repositorio, Docker y el esqueleto de la aplicación sí pueden avanzar en paralelo, porque no dependen de estas respuestas.

---

## Anexo A — Impacto en la planificación de sprints

La planificación propuesta (Sprints 0–8) es razonable en su secuencia, pero tiene tres problemas: no incluye las funcionalidades ausentes de la sección 6, deja las pruebas para el final, y no reserva espacio para promociones.

### A.1 Problemas detectados

| Problema | Detalle |
|---|---|
| **Pruebas en el Sprint 8** | RSG-14. Escribir las pruebas de siete sprints en uno no es viable y anula su función de red de seguridad. |
| **Promociones sin sprint** | RB08 y RB09 son requisitos, pero ningún sprint las contempla. |
| **Faltan historias imprescindibles** | Recuperación de contraseña, verificación de correo, gestión de empleados y perfil del cliente (GAP-25 a GAP-30). |
| **Despliegue en el Sprint 8** | RSG-01 y RSG-09. Los webhooks exigen una URL pública, y los problemas de despliegue descubiertos al final no dejan margen de reacción. |
| **Notificaciones después de pagos** | Un pago aprobado sin correo de confirmación es una experiencia incompleta; conviene tener al menos el envío básico antes del Sprint 5. |

### A.2 Replanificación sugerida

| Sprint | Contenido propuesto | Cambio respecto a lo original |
|---|---|---|
| **0** | Análisis, arquitectura, modelo de datos inicial, repositorio, Docker, migraciones, esqueleto de la aplicación. **Solicitud de credenciales de pasarela.** **Despliegue mínimo funcionando ("hola mundo" público).** | + trámite de pasarela, + despliegue temprano |
| **1** | Autenticación y autorización: registro, login, JWT + refresh, roles y permisos, protección de endpoints. **Verificación de correo y recuperación de contraseña** (requieren el envío de correo básico). | + GAP-25, GAP-26, + envío de correo básico adelantado |
| **2** | Usuarios (perfil, edición), **gestión de empleados y su disponibilidad/jornada**, mascotas (CRUD). | + GAP-29, GAP-07, GAP-30 |
| **3** | Servicios: CRUD, catálogo público, precios, estados. | Sin cambios |
| **4** | Motor de reservas: disponibilidad, validaciones RB01/RB02/RB10, creación, consulta, cancelación RB06, estados. **Pruebas de concurrencia.** | + RSG-03 explícito |
| **5** | Pagos: orden de pago, integración con la pasarela, webhook, RB03/RB04/RB05, job de expiración, solicitud de reembolso RB07. | Sin cambios en alcance; + job de expiración explícito |
| **6** | Panel administrativo: dashboard, búsquedas y filtros, auditoría (RNF08). | Sin cambios |
| **7** | **Promociones (RB08/RB09)** + notificaciones completas y recordatorios programados. | + promociones, que antes no tenían sprint |
| **8** | Pruebas de extremo a extremo, prueba de carga (RNF02-R), endurecimiento de seguridad, despliegue final y documentación. | Ya no es "escribir todas las pruebas", sino integración y cierre |

> **Regla transversal:** en cada sprint, la *Definition of Done* incluye pruebas unitarias del servicio, pruebas del controlador y documentación de los endpoints. El Sprint 8 valida el conjunto, no lo construye.

---

## Anexo B — Plantilla de matriz de permisos

**Esta matriz debe completarla el cliente.** Es el insumo bloqueante número uno (RSG-06, PRG-12).

Leyenda: ✅ permitido · ❌ no permitido · 🔸 permitido solo sobre registros propios · ❓ **por definir**

| Funcionalidad | Cliente | Empleado | Administrador |
|---|:---:|:---:|:---:|
| Registrarse por su cuenta | ✅ | ❌ | ❌ |
| Editar su propio perfil | ✅ | ✅ | ✅ |
| Registrar / editar / eliminar mascotas | 🔸 | ❓ | ✅ |
| Ver el catálogo de servicios | ✅ | ✅ | ✅ |
| Crear / editar / desactivar servicios | ❌ | ❌ | ✅ |
| Consultar disponibilidad | ✅ | ✅ | ✅ |
| Crear una reserva para sí mismo | ✅ | ❌ | — |
| Crear una reserva a nombre de un cliente | ❌ | ❓ | ✅ (DIS-16) |
| Ver reservas propias | 🔸 | 🔸 (asignadas) | ✅ (todas) |
| Ver reservas de todos los empleados | ❌ | ❓ | ✅ |
| Cambiar reserva a `EN_PROCESO` | ❌ | ❓ | ❓ |
| Cambiar reserva a `COMPLETADA` | ❌ | ❓ | ❓ |
| Marcar `NO_ASISTIO` | ❌ | ❓ | ❓ |
| Cancelar una reserva (RB06) | 🔸 | ❓ | ✅ (¿sin la restricción de 24 h?) |
| Bloquear su propia agenda (ausencia) | ❌ | ❓ | ✅ |
| Ver el estado de sus pagos | 🔸 | ❌ | ✅ |
| Ver todos los pagos | ❌ | ❌ | ✅ |
| Registrar / aprobar un reembolso | ❌ | ❌ | ✅ |
| Crear / editar promociones | ❌ | ❌ | ✅ |
| Aplicar un código de promoción | ✅ | — | ✅ |
| Ver el dashboard | ❌ | ❓ (¿versión reducida?) | ✅ |
| Consultar la bitácora de auditoría | ❌ | ❌ | ✅ |
| Crear cuentas de empleado | ❌ | ❌ | ✅ |
| Crear otras cuentas de administrador | ❌ | ❌ | ❓ |
| Eliminar su propia cuenta | ✅ | ❓ | ✅ |

---

## Anexo C — Glosario

| Término | Definición |
|---|---|
| **Bloque horario** | Unidad mínima de agenda (ej. 30 minutos) sobre la que se calcula la disponibilidad. |
| **Cliente** | Persona que registra mascotas y reserva servicios. Rol del sistema. |
| **Cupo** | Recurso de capacidad limitada, aplicable a hospedaje (habitación/jaula), distinto del recurso "empleado". |
| **Disponibilidad** | Conjunto de horarios en los que un servicio puede reservarse: dentro de la jornada, sin solapamiento y sin bloqueo del empleado. |
| **Idempotencia** | Propiedad por la cual procesar el mismo mensaje varias veces produce el mismo resultado que procesarlo una vez. Imprescindible en el webhook de la pasarela, que reintenta envíos. |
| **MVP** | *Minimum Viable Product*. Conjunto mínimo de funcionalidades que entrega valor y permite validar el producto. |
| **Orden de pago** | Registro que asocia una reserva con un intento de cobro, con su monto, moneda, estado y referencia externa en la pasarela. |
| **Pasarela de pago** | Servicio externo que procesa el cobro. En este proyecto no almacena datos de tarjeta en el sistema (checkout alojado). |
| **Percentil 95 (p95)** | Valor por debajo del cual se encuentra el 95 % de las mediciones. Métrica de latencia más honesta que el promedio. |
| **Puerto / adaptador** | Patrón que aísla una integración externa tras una interfaz del dominio, permitiendo sustituir la implementación (real o simulada) sin tocar la lógica de negocio. |
| **Reserva** | Compromiso de prestación de un servicio para una mascota, en una fecha y hora, con un empleado o recurso asignado. |
| **RB / RNF** | Regla de negocio / Requisito no funcional. |
| **Story point** | Unidad relativa de esfuerzo usada para estimar historias en Scrum. |
| **Webhook** | Petición HTTP que un servicio externo envía al sistema para notificar un evento. En este proyecto, la confirmación de pago. |

---

## Registro de cambios

| Versión | Fecha | Autor | Cambios |
|---|---|---|---|
| 1.0 | 2026-09-10 | Ronald Moreno | Versión inicial. Fase 1 — Levantamiento inicial de requisitos. |

---

**Fin del documento DER-PETCARE-001 v1.0**
