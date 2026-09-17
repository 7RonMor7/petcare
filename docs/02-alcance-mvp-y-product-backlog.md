# Alcance del MVP y Product Backlog inicial — Plataforma PetCare

**Fase 2 — Definición de alcance y planificación**

| Campo | Valor |
|---|---|
| Proyecto | Plataforma web de reservas y pagos — PetCare |
| Documento | BKL-PETCARE-001 |
| Versión | 1.0 |
| Fecha | 2026-09-15 |
| Autor | Ronald Moreno |
| Documento previo | DER-PETCARE-001 v1.0 — Especificación de requisitos (Fase 1) |
| Estado | **Requiere una decisión del cliente sobre plazo vs. alcance (sección 7)** |
| Equipo | 1 desarrollador · ~15 h/semana |
| Plazo objetivo | 8 semanas · sprints de 1 semana |

---

## Advertencia previa

Las 12 respuestas cierran la mayor parte de la incertidumbre funcional. El backlog ya se puede construir, y está construido en la sección 6.

Pero al estimarlo aparece un problema que no es de análisis sino de aritmética: **el MVP declarado por el cliente suma 217 story points y la capacidad de 8 semanas a 15 h/semana es de aproximadamente 45**. La sección 7 desarrolla el cálculo, los escenarios posibles y la recomendación. Es el punto más importante de este documento y conviene leerlo antes que el backlog.

---

## Tabla de contenido

1. [Decisiones del cliente registradas](#1-decisiones-del-cliente-registradas)
2. [Qué se cierra del documento de Fase 1](#2-qué-se-cierra-del-documento-de-fase-1)
3. [Contradicciones nuevas detectadas](#3-contradicciones-nuevas-detectadas)
4. [Preguntas de segunda ronda](#4-preguntas-de-segunda-ronda)
5. [Alcance del MVP](#5-alcance-del-mvp)
6. [Product Backlog](#6-product-backlog)
7. [Análisis de capacidad: la brecha](#7-análisis-de-capacidad-la-brecha)
8. [Plan de sprints recomendado](#8-plan-de-sprints-recomendado)
9. [Escalera de sacrificio](#9-escalera-de-sacrificio)
10. [Definición de Terminado](#10-definición-de-terminado)
11. [Criterios de aceptación de las historias críticas](#11-criterios-de-aceptación-de-las-historias-críticas)
12. [Impacto en el modelo de datos](#12-impacto-en-el-modelo-de-datos)
13. [Máquinas de estado actualizadas](#13-máquinas-de-estado-actualizadas)
14. [Riesgos actualizados](#14-riesgos-actualizados)

---

## 1. Decisiones del cliente registradas

| # | Tema | Decisión | ID |
|---|---|---|---|
| 1 | Rol Empleado | Permisos operativos sobre su propia agenda y sus reservas asignadas. Sin acceso a administración, pagos, precios, promociones ni dashboard. | **DEC-C01** |
| 2 | Servicios en MVP | Los 5 servicios. Hospedaje con modelo simplificado de **cupos diarios**. | **DEC-C02** |
| 3 | Precios | Precio base **fijo** por servicio. Hospedaje: **$70.000 por día**. Única variación: promociones. | **DEC-C03** |
| 4 | Horario y disponibilidad | Lun–Sáb 08:00–18:00, domingo cerrado. Disponibilidad = jornada − reservas − bloqueos. **Franjas horarias**, no minuto libre. | **DEC-C04** |
| 5 | Métodos de pago | Solo electrónicos vía pasarela. **Efectivo fuera del MVP.** | **DEC-C05** |
| 6 | Pasarela | Sin decidir. Spike comparativo en Sprint 0. Desarrollo contra `PaymentGateway` simulado. | **DEC-C06** |
| 7 | Reembolso | **Manual.** El sistema genera `SOLICITUD_REEMBOLSO`; el administrador la procesa y marca `REEMBOLSADO`. | **DEC-C07** |
| 8 | Reprogramación | Fuera del MVP → V2. Cancelar y volver a reservar. | **DEC-C08** |
| 9 | Reservas recurrentes | Fuera del MVP → V2. | **DEC-C09** |
| 10 | Promociones | En el MVP, con alcance básico: código, %, vigencia, máximo de usos, estado, un uso por cliente configurable. | **DEC-C10** |
| 11 | Capacidad y plazo | 1 desarrollador, ~15 h/semana, 8 semanas, sprints de 1 semana. | **DEC-C11** |
| 12 | Prioridades | Lista de irrenunciables, sacrificables y V2. | **DEC-C12** |

---

## 2. Qué se cierra del documento de Fase 1

| Elemento de Fase 1 | Estado tras las respuestas |
|---|---|
| GAP-01 Horarios de atención | ✅ Cerrado por DEC-C04 |
| GAP-06 Modelo de precios | ✅ Cerrado por DEC-C03 *(con la excepción del hospedaje, ver CN-01)* |
| GAP-07 Disponibilidad del empleado | ✅ Cerrado por DEC-C04 + DEC-C01 |
| GAP-09 Responsables de las transiciones | ✅ Cerrado por DEC-C01 |
| GAP-10 Reprogramación | ✅ Cerrado por DEC-C08 (fuera) |
| GAP-11 Reservas recurrentes | ✅ Cerrado por DEC-C09 (fuera) |
| GAP-14 Métodos de pago | ✅ Cerrado por DEC-C05 |
| GAP-16 Mecánica del reembolso | ✅ Cerrado por DEC-C07 |
| GAP-20/21 Ámbito y tipo de promoción | ✅ Cerrado por DEC-C10 (solo porcentaje, sin ámbito por servicio) |
| CON-01 Hospedaje vs. flujo de reserva | 🟡 **Parcial.** Se acepta hospedaje con cupos, pero eso confirma que hay **dos motores de reserva**, no uno. Ver CN-02. |
| CON-03 Estados de pago incompletos | ✅ Cerrado: se añade `SOLICITUD_REEMBOLSO` |
| CON-05 RB02 sin jornada laboral | ✅ Cerrado por DEC-C04 |
| CON-07 Panel administrativo vs. Empleado | ✅ Cerrado por DEC-C01 |
| DIS-01 Hospedaje | ✅ Entra, versión simplificada |
| DIS-02 Veterinaria | 🔴 **Sigue abierto.** El cliente confirmó el servicio, pero no respondió si guarda historia clínica (PRG-45). Ver CN-07. |
| DIS-05 Promociones | ✅ Entra, alcance reducido |
| RSG-06 Permisos sin definir | ✅ Mitigado por DEC-C01 |
| **GAP-02** Paseos grupales | 🔴 **Sigue abierto.** No se respondió PRG-04. Afecta directamente RB02. |
| **GAP-13** Consecuencia de `NO_ASISTIO` | 🔴 **Sigue abierto.** El empleado puede marcarlo, pero no se define el efecto sobre el pago. |
| **GAP-12** Cancelación originada por PetCare | 🔴 **Sigue abierto y ahora es más grave.** Ver CN-03. |
| **AMB-05** ¿El cliente elige empleado? | 🔴 **Sigue abierto.** Bloquea el diseño del asistente de reserva. |
| **PRG-07** Duración de cada servicio | 🔴 **Sigue abierto.** Sin duraciones no se generan las franjas. |

> **Seis puntos siguen bloqueando el Sprint 4.** La sección 4 los convierte en preguntas concretas.

---

## 3. Contradicciones nuevas detectadas

Las respuestas resuelven mucho, pero introducen inconsistencias nuevas entre sí. Estas son las que hay que resolver antes de modelar.

| ID | Contradicción | Detalle | Resolución propuesta |
|---|---|---|---|
| **CN-01** | **"Precio fijo" vs. "$70.000 por día"** | La respuesta 3 dice que el precio base es fijo y que no habrá precios variables. Pero el hospedaje se cobra **por día**, así que su total depende del rango de fechas: 5 días = $350.000. Eso *es* un precio variable, solo que la variable es el tiempo y no la mascota. `Servicio.Precio` como valor único no lo soporta. | Añadir `Servicio.unidadCobro ∈ {POR_SERVICIO, POR_DIA}`. El total de la reserva se calcula y **se congela** al crearla. Cambio pequeño ahora, costoso en el Sprint 5. |
| **CN-02** | **Un flujo de reserva, dos motores** | La respuesta 4 modela la disponibilidad como jornada + franjas + empleado. La respuesta 2 modela el hospedaje como cupos diarios. Son dos algoritmos distintos, con recursos distintos (empleado vs. cupo) y unidades distintas (franja vs. día). El flujo `Servicio → Mascota → Fecha → Hora → Empleado` **no aplica al hospedaje**. | Aceptar explícitamente dos tipos de reserva: `RESERVA_POR_FRANJA` y `RESERVA_POR_DIAS`, con un asistente de reserva que se bifurca según `Servicio.modalidadAgenda`. Documentarlo como decisión, no descubrirlo en el Sprint 5. |
| **CN-03** | **Bloqueo de agenda vs. reservas ya confirmadas** | La respuesta 1 permite al empleado bloquear franjas "por una incapacidad". Una incapacidad es precisamente el caso en que ya hay reservas confirmadas y pagadas en esas franjas. El sistema tiene que decidir algo, y no está decidido. | Tres opciones: (a) impedir el bloqueo sobre franjas ocupadas; (b) permitirlo y escalar al administrador para reasignar; (c) permitirlo, cancelar las reservas y generar reembolso automático. **Recomendación para el MVP: opción (a)**, y que el administrador gestione los casos reales. Requiere confirmación (PRG-55). |
| **CN-04** | **Horario 08:00–18:00 vs. hospedaje** | El hospedaje es un servicio de 24 horas por definición: la mascota permanece durante la noche. La validación "el horario sea válido" (dentro de la jornada) no aplica. | El horario de atención acota el **check-in y el check-out**, no la permanencia. Definir ambas horas (PRG-53). |
| **CN-05** | **Expiración de 15 minutos vs. cupos de hospedaje** | RB05 retiene el recurso durante 15 minutos mientras se paga. Para una reserva de hospedaje de 10 días, eso significa retener 10 cupos-día simultáneamente. El modelo de cupos necesita un estado de **retención temporal**, no solo ocupado/libre. | El contador de cupo debe distinguir `ocupados` de `retenidos`, y el job de expiración debe liberar las retenciones. Suma complejidad a HU-068. |
| **CN-06** | **"Notificaciones sacrificables" vs. dependencias reales** | El cliente clasificó las notificaciones como sacrificables. Pero la **recuperación de contraseña** y la **verificación de correo** dependen de la misma infraestructura de correo, y sin recuperación de contraseña el sistema es inusable desde el primer olvido. | Separar: la **infraestructura de correo** es irrenunciable (la exige la recuperación de contraseña). Lo sacrificable son los **recordatorios programados** y los avisos al empleado. |
| **CN-07** | **Veterinaria en MVP sin definir su alcance** | Se confirmó el servicio pero no se respondió PRG-45 (¿historia clínica?). Si el alcance incluyera diagnóstico y tratamiento, el esfuerzo se multiplica. | Asumir **solo agendamiento** (SUP-14) y pedir confirmación explícita. |
| **CN-08** | **Auditoría irrenunciable vs. dashboard sacrificable** | RNF08 y "auditoría de operaciones importantes" están en la lista de irrenunciables, pero la pantalla que permite consultarla cae bajo "filtros avanzados / reportes complejos", que es sacrificable. Una bitácora sin forma de consultarla no cumple "poder rastrearse". | Definir el mínimo: tabla de auditoría escrita transaccionalmente + un endpoint de consulta con filtros básicos. La pantalla puede ser una tabla simple. Confirmar (PRG-60). |
| **CN-09** | **Pasarela sin sandbox vs. "pago electrónico irrenunciable" y "despliegue funcional"** | Ambos están en la lista de irrenunciables, pero la respuesta 6 dice que no hay credenciales ni pasarela elegida. Si las credenciales no llegan a tiempo, el requisito obligatorio del proyecto no se puede demostrar de extremo a extremo. | **Pregunta bloqueante (PRG-52):** ¿se acepta el MVP con el adaptador simulado si la pasarela real no está disponible? De la respuesta depende la definición de "terminado". |
| **CN-10** | **Sprints de 1 semana con 15 h de capacidad** | Un sprint de 15 h con planning, review y retro consume entre el 10 % y el 15 % del sprint en ceremonias. Con un solo desarrollador, esa proporción es pura pérdida. | Mantener sprints de 1 semana (dan buena cadencia de medición) pero con ceremonias de 45 min en total, y una retrospectiva escrita en vez de reunión. |

---

## 4. Preguntas de segunda ronda

De estas, **PRG-52, PRG-61, PRG-62 y PRG-63 bloquean sprints concretos** y deberían responderse antes del Sprint 3.

| ID | Pregunta | Prio | Bloquea |
|---|---|---|---|
| **PRG-52** | Si la pasarela real no está disponible a tiempo, ¿se acepta el MVP con el adaptador simulado y el webhook probado contra un emulador? | P0 | Definición de Terminado |
| **PRG-53** | Hospedaje: ¿cuántos cupos hay en total? ¿A qué hora son el check-in y el check-out? ¿Se cobra la noche de salida? | P0 | Sprint de hospedaje |
| **PRG-54** | Hospedaje: ¿requiere un empleado asignado, o basta con el cupo? | P0 | Modelo de datos |
| **PRG-55** | Si un empleado bloquea franjas que ya tienen reservas confirmadas y pagadas, ¿qué debe hacer el sistema? (CN-03) | P0 | Sprint 4 |
| **PRG-56** | `NO_ASISTIO`: ¿se reembolsa al cliente, se pierde el pago, o hay penalización? | P1 | Sprint de pagos |
| **PRG-57** | ¿Desde qué estados puede marcarse `NO_ASISTIO` y dentro de qué ventana de tiempo? | P1 | Sprint 3 |
| **PRG-58** | Consulta veterinaria: ¿confirma que en el MVP es únicamente agendamiento, sin historia clínica ni diagnóstico? (CN-07) | P0 | Alcance |
| **PRG-59** | Recuperación de contraseña y verificación de correo no aparecen en la lista de irrenunciables. ¿Confirma que sí lo son? (CN-06) | P0 | Sprint 1 |
| **PRG-60** | Auditoría: ¿basta con la tabla y un endpoint de consulta, o se exige pantalla con filtros? (CN-08) | P1 | Alcance |
| **PRG-61** | ¿Cuánto dura cada servicio? (paseo, baño, peluquería, veterinaria) **Sin esto no se pueden generar las franjas.** | P0 | Sprint 4 |
| **PRG-62** | ¿De cuánto es la franja horaria: 30 o 60 minutos? | P0 | Sprint 4 |
| **PRG-63** | ¿El cliente elige al empleado de una lista, o el sistema lo asigna? ¿Hay opción "sin preferencia"? (AMB-05) | P0 | Sprint 4 |
| **PRG-64** | ¿Un paseador puede llevar varias mascotas a la vez? (GAP-02, afecta RB02) | P0 | Sprint 4 |
| **PRG-65** | Si una reserva con promoción expira o se cancela, ¿el cliente recupera el derecho a usar ese código? | P1 | Sprint de promociones |
| **PRG-66** | ¿Con cuánta antelación mínima puede reservarse? ¿Y con cuánta anticipación máxima? | P1 | Sprint 4 |
| **PRG-67** | **Dado el análisis de capacidad de la sección 7, ¿qué escenario acepta el cliente?** | P0 | Todo el plan |

---

## 5. Alcance del MVP

### 5.1 Dentro del MVP

| Área | Incluye |
|---|---|
| **Cuentas** | Registro de cliente, login con JWT, autorización por rol, cierre de sesión, recuperación de contraseña, verificación de correo |
| **Mascotas** | Registrar, listar, editar y eliminar mascotas propias |
| **Servicios** | CRUD administrativo, catálogo público con precios, activar/desactivar |
| **Empleados** | CRUD administrativo, jornada laboral, servicios que presta, bloqueo de franjas |
| **Agenda del empleado** | Consultar agenda propia, ver reservas asignadas, ver ficha de la mascota, cambiar estado a `EN_PROCESO` / `COMPLETADA` / `NO_ASISTIO` |
| **Disponibilidad** | Motor por franjas: jornada − reservas − bloqueos |
| **Reservas por franja** | Paseo, baño, peluquería, veterinaria. Validaciones RB01, RB02, RB10. Unicidad garantizada en base de datos |
| **Reservas por días** | Hospedaje con cupos diarios, retención temporal y cálculo por día |
| **Cancelación** | Regla de 24 horas (RB06) |
| **Pagos** | Orden de pago, checkout, webhook firmado e idempotente, RB03, RB04, RB05 (expiración a 15 min), consulta de estado |
| **Reembolsos** | Generación de `SOLICITUD_REEMBOLSO` y procesamiento manual por el administrador |
| **Promociones** | CRUD administrativo básico, aplicación del código, un uso por cliente configurable |
| **Administración** | Listado de reservas y pagos con filtros, KPIs numéricos, bitácora de auditoría |
| **Notificaciones** | Correo de reserva confirmada, pago aprobado, pago rechazado, cancelación |
| **Calidad** | Pruebas unitarias, de servicio, de controlador y de endpoint; responsive en 360/768/1280; despliegue público |

### 5.2 Fuera del MVP → V2

Reprogramación · reservas recurrentes · reembolso automático vía pasarela · precios dinámicos por tamaño, raza u horario · promociones complejas (2x1, combos, acumulables) · gráficas del dashboard · recordatorio programado 24 h antes · avisos al empleado · vista de ocupación del hospedaje · creación de reservas por el administrador (canal telefónico) · eliminación de cuenta con anonimización · aplicación móvil · chat · calificaciones · múltiples pasarelas simultáneas · prueba de carga.

### 5.3 Fuera de alcance por completo

Historia clínica veterinaria · facturación electrónica DIAN · multi-sede · pago en efectivo · geolocalización y rutas de paseo.

---

## 6. Product Backlog

**Escala de estimación.** 1 SP ≈ 2 horas de trabajo efectivo de un desarrollador (código + pruebas + integración + corrección). Los story points son normalmente relativos, pero con un equipo de una sola persona una calibración explícita es lo que hace verificable el plan. La calibración se revisa al cerrar el Sprint 1 con la velocidad real.

**Prioridad (MoSCoW).** M = Must (irrenunciable) · S = Should (importante, sacrificable) · C = Could (deseable) · W = Won't (V2).

**Sprint.** Asignación del plan recomendado de la sección 8. `S8+` = fuera del plazo de 8 semanas al ritmo estimado. `V2` = segunda versión.

### E0 — Fundación y arquitectura · 15 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-001 | Como desarrollador, quiero la estructura modular de Spring Boot con Docker Compose, Flyway y perfiles de entorno, para tener una base desplegable y versionada desde el primer día | M | 3 | S0 | Pendiente |
| HU-002 | Como desarrollador, quiero el proyecto React con Tailwind, router y cliente Axios con interceptor de JWT, para consumir la API de forma uniforme | M | 2 | S0 | Pendiente |
| HU-003 | Como equipo, quiero comparar Wompi, Mercado Pago y PayU en soporte de PSE/Nequi, calidad de sandbox, documentación e integración con Spring Boot, para elegir la pasarela con evidencia *(timebox: 4 h)* | M | 2 | S0 | Pendiente |
| HU-004 | Como desarrollador, quiero el puerto `PaymentGateway` con un adaptador simulado, para desarrollar todo el flujo de pagos sin depender de credenciales externas | M | 3 | S0 | Pendiente |
| HU-005 | Como equipo, quiero un despliegue público mínimo funcionando, para validar temprano el entorno y poder recibir webhooks | M | 3 | S0 | Pendiente |
| HU-006 | Como desarrollador, quiero integración continua que ejecute build y pruebas en cada push | C | 2 | S8+ | Pendiente |

### E1 — Autenticación y autorización · 20 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-007 | Como visitante, quiero registrarme con mis datos y aceptar la política de tratamiento de datos, para poder reservar servicios | M | 3 | S1 | Pendiente |
| HU-008 | Como usuario registrado, quiero iniciar sesión y recibir un token de acceso y uno de refresco, para usar la plataforma de forma segura | M | 3 | S1 | Pendiente |
| HU-009 | Como sistema, quiero autorizar cada endpoint según el rol y los permisos del usuario, para que nadie acceda a lo que no le corresponde | M | 3 | S1 | Pendiente |
| HU-010 | Como usuario, quiero cerrar sesión y que mi token de refresco quede revocado | M | 2 | S1 | Pendiente |
| HU-011 | Como usuario, quiero verificar mi correo tras registrarme, para que las confirmaciones me lleguen | S | 3 | S8+ | Pendiente |
| HU-012 | Como usuario, quiero recuperar mi contraseña por correo cuando la olvide | S | 3 | S8+ | Pendiente |
| HU-013 | Como usuario autenticado, quiero cambiar mi contraseña | S | 1 | S8+ | Pendiente |
| HU-014 | Como sistema, quiero bloquear temporalmente la cuenta tras 5 intentos fallidos en 15 minutos | C | 2 | S8+ | Pendiente |

### E2 — Cliente y mascotas · 13 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-015 | Como cliente, quiero ver y editar los datos de mi perfil | S | 2 | S8+ | Pendiente |
| HU-016 | Como cliente, quiero registrar una mascota con sus datos, para poder reservarle servicios | M | 3 | S1 | Pendiente |
| HU-017 | Como cliente, quiero listar, editar y eliminar mis mascotas, y que el sistema impida operar sobre mascotas ajenas | M | 2 | S2 | Pendiente |
| HU-018 | Como cliente, quiero una interfaz para gestionar mis mascotas | M | 3 | S2 | Pendiente |
| HU-019 | Como cliente, quiero eliminar mi cuenta y que mis datos se anonimicen conservando la integridad contable | W | 3 | V2 | Pendiente |

### E3 — Empleados y agenda · 19 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-020 | Como administrador, quiero crear, editar y desactivar empleados | M | 3 | S3 | Pendiente |
| HU-021 | Como administrador, quiero asignar a cada empleado los servicios que puede prestar | M | 2 | S2 | Pendiente |
| HU-022 | Como administrador, quiero definir la jornada laboral semanal de cada empleado | M | 3 | S3 | Pendiente |
| HU-023 | Como empleado, quiero consultar mi agenda y las reservas que tengo asignadas | M | 3 | S3 | Pendiente |
| HU-024 | Como empleado, quiero bloquear franjas de mi agenda por incapacidad o compromiso, para que no me asignen reservas en ellas | M | 3 | S4 | Pendiente |
| HU-025 | Como empleado, quiero consultar la información de la mascota de la reserva que voy a atender | M | 2 | S3 | Pendiente |
| HU-026 | Como empleado, quiero cambiar el estado de mis reservas a `EN_PROCESO`, `COMPLETADA` o `NO_ASISTIO` | M | 3 | S3 | Pendiente |

### E4 — Servicios · 6 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-027 | Como administrador, quiero crear y editar servicios con nombre, descripción, precio, duración, unidad de cobro y estado | M | 3 | S2 | Pendiente |
| HU-028 | Como visitante, quiero consultar el catálogo de servicios con sus precios | M | 2 | S2 | Pendiente |
| HU-029 | Como administrador, quiero desactivar un servicio sin afectar las reservas ya confirmadas | M | 1 | S2 | Pendiente |

### E5 — Disponibilidad y reservas · 33 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-030 | Como sistema, quiero generar las franjas horarias de cada empleado a partir de su jornada laboral | M | 3 | S4 | Pendiente |
| HU-031 | Como cliente, quiero consultar las franjas disponibles de un servicio en una fecha, descontando reservas y bloqueos | M | 5 | S4 | Pendiente |
| HU-032 | Como cliente, quiero crear una reserva y que el sistema valide RB01, RB02, RB10 y la validez del horario | M | 5 | S5 | Pendiente |
| HU-033 | Como sistema, quiero garantizar en la base de datos que dos reservas nunca ocupen el mismo recurso y franja, incluso bajo peticiones concurrentes | M | 3 | S5 | Pendiente |
| HU-034 | Como cliente, quiero un asistente de reserva que me guíe por servicio, mascota, fecha, hora, empleado y resumen | M | 5 | S6 | Pendiente |
| HU-035 | Como sistema, quiero una máquina de estados que solo permita transiciones válidas de reserva | M | 3 | S5 | Pendiente |
| HU-036 | Como cliente, quiero listar mis reservas y ver el detalle de cada una | M | 3 | S6 | Pendiente |
| HU-037 | Como cliente, quiero cancelar una reserva hasta 24 horas antes, y recibir un mensaje claro si ya no es posible | M | 3 | S7 | Pendiente |
| HU-038 | Como cliente, quiero elegir empleado o indicar "sin preferencia" y que el sistema asigne uno disponible | M | 3 | S4 | Pendiente |

### E6 — Pagos · 37 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-039 | Como sistema, quiero generar una orden de pago al crear la reserva, con el total congelado | M | 3 | S5 | Pendiente |
| HU-040 | Como cliente, quiero ser redirigido al checkout de la pasarela y volver a la plataforma al terminar | M | 3 | S6 | Pendiente |
| HU-041 | Como sistema, quiero recibir el webhook de la pasarela validando firma, idempotencia y monto | M | 5 | S6 | Pendiente |
| HU-042 | Como sistema, quiero confirmar la reserva cuando el pago sea aprobado (RB03) | M | 2 | S5 | Pendiente |
| HU-043 | Como cliente, quiero reintentar el pago cuando sea rechazado, con la reserva en `PENDIENTE_PAGO` (RB04) | M | 3 | S7 | Pendiente |
| HU-044 | Como sistema, quiero expirar las reservas no pagadas a los 15 minutos y liberar el horario (RB05) | M | 5 | S7 | Pendiente |
| HU-045 | Como cliente, quiero consultar el estado de mis pagos | M | 2 | S7 | Pendiente |
| HU-046 | Como sistema, quiero generar una `SOLICITUD_REEMBOLSO` cuando se cancele una reserva pagada cumpliendo RB06 (RB07) | M | 3 | S8+ | Pendiente |
| HU-047 | Como administrador, quiero ver las solicitudes de reembolso y marcarlas como `REEMBOLSADO` | M | 3 | S8+ | Pendiente |
| HU-048 | Como desarrollador, quiero configurar las credenciales y el entorno de la pasarela elegida | M | 3 | S8+ | Pendiente |
| HU-049 | Como sistema, quiero integrar el checkout y el webhook reales del proveedor seleccionado | M | 5 | S8+ | Pendiente |

### E7 — Promociones · 15 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-050 | Como administrador, quiero crear promociones con código, porcentaje, vigencia, máximo de usos y estado | S | 5 | S8+ | Pendiente |
| HU-051 | Como cliente, quiero aplicar un código de promoción y ver el total recalculado antes de pagar | S | 5 | S8+ | Pendiente |
| HU-052 | Como sistema, quiero impedir que un cliente use la misma promoción más de una vez, salvo configuración del administrador (RB09) | S | 3 | S8+ | Pendiente |
| HU-053 | Como cliente, quiero recuperar el uso de mi promoción si la reserva expira o se cancela | W | 2 | V2 | Pendiente |

### E8 — Panel administrativo y auditoría · 29 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-054 | Como administrador, quiero listar reservas filtrando por cliente, mascota, servicio, empleado, fecha y estado | S | 5 | S8+ | Pendiente |
| HU-055 | Como administrador, quiero listar pagos filtrando por cliente, fecha, método, estado y valor | S | 3 | S8+ | Pendiente |
| HU-056 | Como sistema, quiero registrar en una bitácora, dentro de la misma transacción, toda operación importante sobre reservas, pagos, servicios, promociones y usuarios (RNF08) | M | 5 | S8+ | Pendiente |
| HU-057 | Como administrador, quiero consultar la bitácora de auditoría filtrando por actor, entidad y rango de fechas | S | 3 | S8+ | Pendiente |
| HU-058 | Como administrador, quiero ver los KPIs de reservas de hoy, ingresos del mes, clientes registrados y servicios realizados | S | 3 | S8+ | Pendiente |
| HU-059 | Como administrador, quiero ver gráficas de reservas, ingresos y servicios más vendidos | W | 5 | V2 | Pendiente |
| HU-060 | Como administrador, quiero crear una reserva a nombre de un cliente que llamó por teléfono | W | 5 | V2 | Pendiente |

### E9 — Notificaciones · 12 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-061 | Como sistema, quiero una infraestructura de correo con plantillas y registro del estado de cada envío | M | 3 | S8+ | Pendiente |
| HU-062 | Como cliente, quiero recibir un correo cuando mi reserva se confirme y cuando mi pago sea aprobado | S | 2 | S8+ | Pendiente |
| HU-063 | Como cliente, quiero recibir un correo cuando mi pago sea rechazado, mi reserva expire o la cancele | S | 2 | S8+ | Pendiente |
| HU-064 | Como cliente, quiero recibir un recordatorio el día anterior a mi reserva | W | 3 | V2 | Pendiente |
| HU-065 | Como empleado, quiero recibir aviso cuando me asignen o me cancelen una reserva | W | 2 | V2 | Pendiente |

### E10 — Hospedaje · 21 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-066 | Como administrador, quiero definir la capacidad diaria de cupos de hospedaje | S | 5 | S8+ | Pendiente |
| HU-067 | Como cliente, quiero reservar hospedaje indicando fecha de entrada y de salida, con el total calculado por día | S | 8 | S8+ | Pendiente |
| HU-068 | Como sistema, quiero validar que haya cupo en todos los días del rango y retenerlo durante los 15 minutos del pago | S | 5 | S8+ | Pendiente |
| HU-069 | Como administrador, quiero ver la ocupación del hospedaje por fecha | W | 3 | V2 | Pendiente |

### E11 — Calidad y despliegue · 23 SP

| ID | Historia | Prio | SP | Sprint | Estado |
|---|---|:--:|:--:|:--:|---|
| HU-070 | Como equipo, quiero pruebas de controlador y de endpoint sobre los flujos críticos | M | 5 | S7 | Pendiente |
| HU-071 | Como equipo, quiero pruebas de los componentes críticos del frontend | S | 3 | S8+ | Pendiente |
| HU-072 | Como cliente, quiero usar la plataforma sin problemas en móvil, tablet y escritorio (RNF04-R) | M | 5 | S7 | Pendiente |
| HU-073 | Como equipo, quiero el despliegue final con variables de entorno y HTTPS | M | 3 | S7 | Pendiente |
| HU-074 | Como equipo, quiero la documentación de la API con OpenAPI | S | 2 | S8+ | Pendiente |
| HU-075 | Como equipo, quiero una prueba de carga que valide RNF02-R | W | 3 | V2 | Pendiente |
| HU-076 | Como cliente, quiero un manual de usuario y la documentación de entrega | S | 2 | S8+ | Pendiente |

### 6.1 Resumen del backlog

| Épica | Historias | SP |
|---|:--:|:--:|
| E0 Fundación y arquitectura | 6 | 15 |
| E1 Autenticación y autorización | 8 | 20 |
| E2 Cliente y mascotas | 5 | 13 |
| E3 Empleados y agenda | 7 | 19 |
| E4 Servicios | 3 | 6 |
| E5 Disponibilidad y reservas | 9 | 33 |
| E6 Pagos | 11 | 37 |
| E7 Promociones | 4 | 15 |
| E8 Panel administrativo y auditoría | 7 | 29 |
| E9 Notificaciones | 5 | 12 |
| E10 Hospedaje | 4 | 21 |
| E11 Calidad y despliegue | 7 | 23 |
| **Total** | **76** | **243** |

| Prioridad | Historias | SP |
|---|:--:|:--:|
| Must — irrenunciable | 47 | 148 |
| Should — importante, sacrificable | 19 | 65 |
| Could — deseable | 2 | 4 |
| Won't — aplazado a V2 | 8 | 26 |
| **MVP declarado por el cliente (M + S + C)** | **68** | **217** |

---

## 7. Análisis de capacidad: la brecha

### 7.1 Capacidad disponible

| Concepto | Cálculo | Horas |
|---|---|---:|
| Tiempo bruto | 8 semanas × 15 h | 120 |
| − Ceremonias Scrum | 1 h × 8 sprints | −8 |
| − Reserva para imprevistos, bloqueos y aprendizaje (20 %) | 20 % de 112 | −22 |
| **Horas efectivas de desarrollo** | | **90** |
| **Velocidad estimada** | 90 h ÷ 2 h/SP | **45 SP** |
| **Por sprint** | 45 ÷ 8 | **≈ 5,6 SP** |

### 7.2 La brecha

| Alcance | SP | Velocidad requerida | Relación con la capacidad |
|---|---:|---:|---:|
| Backlog completo | 243 | 30,4 SP/sprint | **5,4×** |
| MVP declarado por el cliente | 217 | 27,1 SP/sprint | **4,8×** |
| Solo las historias Must | 148 | 18,5 SP/sprint | **3,3×** |
| **MVP Núcleo propuesto** (sección 8) | **126** | **15,8 SP/sprint** | **2,8×** |
| Capacidad estimada | 45 | 5,6 SP/sprint | 1,0× |

**El MVP declarado no cabe en 8 semanas a 15 h/semana.** No es cuestión de recortar un par de funcionalidades: sobra casi cinco veces el alcance. Incluso quitando todo lo sacrificable y todo lo de segunda versión, las historias irrenunciables solas piden casi el triple del tiempo disponible.

### 7.3 Escenarios

| Escenario | Alcance | Plazo | Dedicación | Viabilidad |
|---|---|---|---|---|
| **A — Plazo fijo, alcance completo** | 217 SP | 8 semanas | 54 h/semana | ❌ Inviable |
| **B — Alcance completo, plazo abierto** | 217 SP | ~39 semanas | 15 h/semana | ❌ Inviable académicamente |
| **C — Plazo fijo, alcance mínimo real** | ~45 SP | 8 semanas | 15 h/semana | ⚠️ Viable, pero no alcanza a completar un ciclo reservar→pagar. Sin pagos no cumple el requisito obligatorio del proyecto |
| **D — MVP Núcleo con revisión de velocidad** *(recomendado)* | 126 SP | 8 semanas | 15 h/semana | ⚠️ Exige 15,8 SP/sprint, 2,8× la estimación conservadora |
| **E — MVP Núcleo con plazo ampliado** | 126 SP | 14 semanas | 15 h/semana | ✅ Viable con la estimación conservadora |

### 7.4 Recomendación

**Escenario D como plan de trabajo, con una revisión obligatoria al cerrar el Sprint 1, y el Escenario E como alternativa preparada.**

El razonamiento: la estimación de 2 h/SP es deliberadamente conservadora y está calculada para un desarrollador que empieza de cero. Con experiencia previa en este mismo stack, la velocidad real puede ser bastante mayor. Pero *puede* no es *es*, y comprometer un alcance basándose en una velocidad que nadie ha medido es exactamente el error que Scrum existe para evitar.

Por eso el plan tiene un punto de control explícito:

- **Sprints 0 y 1** se ejecutan con el alcance del MVP Núcleo y se **mide la velocidad real**.
- **Al cerrar el Sprint 1** se recalcula: `SP completados en S0+S1 ÷ 2 = velocidad real`.
  - Si la velocidad real ≥ 14 SP/sprint → el MVP Núcleo es alcanzable. Se continúa.
  - Si está entre 9 y 14 → se aplica la escalera de sacrificio (sección 9) hasta que el alcance quepa.
  - Si es < 9 → se escala al cliente y se negocia plazo (Escenario E) o alcance (Escenario C).

Esto convierte una discusión de opiniones en una decisión con datos, y la toma en la semana 2 y no en la 7.

### 7.5 Qué queda fuera del plazo de 8 semanas

Aun cumpliendo el MVP Núcleo, estas piezas del MVP declarado quedan fuera y deben negociarse como segunda entrega:

| Área | SP | Nota |
|---|---:|---|
| Hospedaje (E10) | 18 | El servicio más costoso del MVP, por ser un segundo motor de reservas |
| Promociones (E7) | 13 | |
| Panel administrativo y auditoría (E8) | 19 | La auditoría es irrenunciable según el cliente pero no cabe |
| Notificaciones (E9) | 7 | |
| Verificación de correo y recuperación de contraseña | 7 | Ver CN-06: la recuperación de contraseña debería subir de prioridad |
| Integración real de la pasarela (HU-048, HU-049) | 8 | Depende de credenciales que aún no existen |
| Reembolsos (HU-046, HU-047) | 6 | |
| Otros: integración continua, perfil del cliente, bloqueo por intentos, pruebas de frontend, OpenAPI, manual de entrega | 13 | |
| **Total fuera del plazo** | **91** | ≈ 6 sprints adicionales al ritmo del MVP Núcleo |

---

## 8. Plan de sprints recomendado

**MVP Núcleo: 126 SP en 8 sprints de 1 semana.** El objetivo de cada sprint es un incremento demostrable, no un conjunto de tareas técnicas.

| Sprint | Objetivo demostrable | Historias | SP |
|---|---|---|---:|
| **S0** | *"La aplicación existe, está desplegada y sé con qué pasarela voy a trabajar."* | HU-001, HU-002, HU-003, HU-004, HU-005 | 13 |
| **S1** | *"Un cliente puede registrarse, iniciar sesión y registrar su mascota. Los endpoints están protegidos."* | HU-007, HU-008, HU-009, HU-010, HU-016 | 14 |
| **S2** | *"El cliente gestiona sus mascotas y ve el catálogo de servicios con precios."* | HU-017, HU-018, HU-027, HU-028, HU-029, HU-021 | 13 |
| **S3** | *"El administrador registra empleados con su jornada; el empleado ve su agenda y cambia el estado de sus reservas."* | HU-020, HU-022, HU-023, HU-025, HU-026 | 14 |
| **S4** | *"El sistema calcula y muestra la disponibilidad real de cada servicio."* — el corazón del producto | HU-024, HU-030, HU-031, HU-038 | 14 |
| **S5** | *"Se puede crear una reserva válida, con su orden de pago, sin que dos usuarios se pisen."* | HU-032, HU-033, HU-035, HU-039, HU-042 | 16 |
| **S6** | *"El cliente reserva desde la interfaz, paga en el checkout y su reserva se confirma por webhook."* — el ciclo completo | HU-034, HU-036, HU-040, HU-041 | 16 |
| **S7** | *"El cliente cancela, reintenta pagos, las reservas sin pagar expiran, y todo funciona en móvil y está desplegado."* | HU-037, HU-043, HU-044, HU-045, HU-070, HU-072, HU-073 | **26** ⚠️ |
| | | **Total** | **126** |

### 8.1 Notas del plan

- **HU-005 (despliegue) va en el Sprint 0, no en el 8.** Los webhooks no llegan a `localhost`. Sin URL pública, el Sprint 6 se bloquea. Es la lección más cara de aprender tarde.
- **HU-003 (spike de pasarela) está en timebox de 4 horas.** No es investigación abierta: es comparar tres proveedores en cuatro criterios y escribir media página de conclusión.
- **El Sprint 4 es el sprint de mayor riesgo técnico.** Si algo se va a desbordar, será el motor de disponibilidad. Conviene empezarlo con el diagrama del algoritmo escrito antes de codificar.
- **Las pruebas unitarias y de servicio no aparecen como historias:** están en la Definición de Terminado de cada sprint (sección 10). HU-070 cubre solo las pruebas de endpoint de los flujos críticos, que sí son un trabajo aparte.
- **El Sprint 7 está sobrecargado a 26 SP**, un 65 % por encima de la media del plan. No es un descuido: es lo que queda cuando el alcance no cabe y no hay un sprint 8 al que empujarlo. Un último sprint sobrecargado es la forma más común de terminar un proyecto a medias, porque es donde se acumula todo el retraso anterior. La escalera de sacrificio de la sección 9 libera 19 SP, **10 de ellos del propio Sprint 7** (que baja a 16) y el resto de los sprints 3, 4 y 6, que es precisamente su función.
- **Los sprints 5 y 6 están a 16 SP**, por encima de la media. Si la velocidad medida en S0–S1 no lo sostiene, son los siguientes candidatos a recorte.

---

## 9. Escalera de sacrificio

Acordar el orden de recorte **antes** de necesitarlo evita que la decisión se tome bajo presión y a última hora. Se recorta de arriba hacia abajo.

| Orden | Qué se recorta | SP liberados | Qué se pierde |
|:--:|---|---:|---|
| 1 | HU-072 responsive completo → solo móvil y escritorio, sin tablet | 2 | Cobertura parcial de RNF04-R |
| 2 | HU-038 selección de empleado → asignación automática siempre | 3 | El cliente no elige profesional |
| 3 | HU-045 consulta de estado de pagos → visible dentro del detalle de la reserva | 2 | Una pantalla menos |
| 4 | HU-025 ficha de mascota para el empleado → solo nombre y observaciones | 1 | Menos contexto para el empleado |
| 5 | HU-020 CRUD de empleados → se cargan por migración de datos | 3 | El administrador no gestiona empleados desde la interfaz |
| 6 | HU-043 reintento de pago → el cliente cancela y vuelve a reservar | 3 | **Se incumple RB04** — requiere aprobación del cliente |
| 7 | HU-070 pruebas de endpoint → solo del flujo de reserva y pago | 3 | Menos cobertura |
| 8 | HU-036 listado de reservas → vista simplificada sin detalle | 2 | Menos usabilidad |
| | **Total recortable sin romper el ciclo principal** | **19** | 10 salen del Sprint 7, que baja de 26 a 16 SP |

**Lo que no se recorta bajo ninguna circunstancia**, porque sin ello no hay producto demostrable: HU-001, HU-005, HU-007, HU-008, HU-009, HU-016, HU-027, HU-030, HU-031, HU-032, HU-033, HU-034, HU-039, HU-041, HU-042, HU-044, HU-073.

---

## 10. Definición de Terminado

Una historia está terminada cuando **todo** lo siguiente es cierto:

- [ ] El código está en la rama principal, sin conflictos y con un commit descriptivo.
- [ ] Los criterios de aceptación de la historia se cumplen y fueron verificados manualmente.
- [ ] Hay **pruebas unitarias** de la lógica de negocio que la historia introduce.
- [ ] Hay **pruebas de servicio** de los casos límite y de los errores esperados.
- [ ] Las validaciones de entrada están implementadas y devuelven mensajes de error claros.
- [ ] Los endpoints nuevos están protegidos según la matriz de permisos.
- [ ] No se registran datos sensibles en logs (contraseñas, tokens, datos de pago).
- [ ] Las migraciones de base de datos están versionadas en Flyway.
- [ ] La interfaz funciona a 360 px y a 1280 px.
- [ ] La aplicación sigue desplegándose correctamente.

Un sprint está terminado cuando todas sus historias lo están y el incremento es **demostrable en el entorno desplegado**, no solo en local.

---

## 11. Criterios de aceptación de las historias críticas

Solo se detallan las historias donde la ambigüedad tiene coste alto. El resto recibe sus criterios en el planning de su sprint.

### HU-031 — Consultar disponibilidad

- **Dado** un servicio activo, una fecha y un empleado habilitado para ese servicio,
  **cuando** el cliente consulta la disponibilidad,
  **entonces** el sistema devuelve las franjas del tamaño configurado (PRG-62) que estén dentro de la jornada del empleado, libres de reservas activas y libres de bloqueos.
- Una franja se descarta si el intervalo `[inicio, inicio + duración del servicio]` se solapa con cualquier reserva en estado `PENDIENTE_PAGO`, `CONFIRMADA` o `EN_PROCESO`.
- Una franja se descarta si `inicio + duración` excede el cierre de la jornada.
- Una franja se descarta si `inicio` es anterior a `ahora + antelación mínima` (PRG-66).
- Los domingos y los días sin jornada definida devuelven una lista vacía, no un error.
- La consulta responde en menos de 1.500 ms con la carga de referencia de RNF02-R.

### HU-032 — Crear reserva

- **Dado** un cliente autenticado, **cuando** intenta reservar para una mascota que no es suya, **entonces** el sistema responde `403` y no crea nada (RB01).
- **Dado** un empleado con una reserva activa que se solapa, **cuando** se intenta reservar esa franja, **entonces** el sistema responde `409` con un mensaje explicativo (RB02).
- **Dado** una mascota con otra reserva activa solapada, **cuando** se intenta reservar, **entonces** el sistema responde `409` (RB10).
- **Dado** un servicio inactivo, **cuando** se intenta reservar, **entonces** el sistema responde `409`.
- La reserva se crea en estado `PENDIENTE_PAGO` y con el total **congelado** al momento de crearla.
- La creación de la reserva y la de su orden de pago ocurren en la misma transacción.

### HU-033 — Unicidad bajo concurrencia

- Existe una restricción a nivel de base de datos que impide dos reservas activas sobre el mismo recurso y franja.
- Existe una prueba automatizada que lanza **20 peticiones concurrentes** sobre la misma franja y verifica que **exactamente una** devuelve `201` y las otras 19 devuelven `409`.
- La prueba no depende de retardos ni de `sleep`.

### HU-037 — Cancelar reserva (RB06)

- **Dado** una reserva confirmada cuyo inicio está a más de 24 horas, **cuando** el cliente la cancela, **entonces** pasa a `CANCELADA` y la franja queda disponible de nuevo.
- **Dado** una reserva cuyo inicio está a menos de 24 horas, **cuando** el cliente intenta cancelarla, **entonces** el sistema responde `409` con el texto exacto: *"La reserva no puede ser cancelada debido a que faltan menos de 24 horas para su realización."*
- El cálculo se hace en zona horaria `America/Bogota`.
- Si la reserva tenía un pago `APROBADO`, se genera una `SOLICITUD_REEMBOLSO` (RB07).

### HU-041 — Webhook de la pasarela

- El endpoint es público (sin JWT) y valida la firma HMAC del proveedor; una firma inválida devuelve `401` y **no** modifica nada.
- El endpoint es idempotente: recibir dos veces el mismo identificador de transacción produce el mismo resultado que recibirlo una vez.
- El monto recibido se contrasta contra el de la orden; si no coincide, el pago se marca para revisión manual y **no** se confirma la reserva.
- El endpoint responde en menos de 5 segundos y siempre con `2xx` cuando el evento se procesó o se descartó por duplicado, para que el proveedor no reintente indefinidamente.
- Todo evento recibido se registra en la bitácora, válido o no.

### HU-044 — Expiración a los 15 minutos (RB05)

- Un proceso programado se ejecuta al menos cada minuto y expira las reservas en `PENDIENTE_PAGO` cuya primera orden de pago tenga más de 15 minutos.
- Al expirar: la reserva pasa a `EXPIRADA`, la franja vuelve a estar disponible y el pago pendiente pasa a `CANCELADO`.
- **Caso límite obligatorio:** si llega un pago aprobado sobre una reserva ya `EXPIRADA`, el sistema no la confirma silenciosamente. Aplica la política definida en PRG-32 y registra el caso en la bitácora.
- La expiración y la confirmación por webhook nunca se ejecutan sobre la misma reserva a la vez (bloqueo transaccional).

### HU-024 — Bloqueo de agenda del empleado

- El empleado solo puede bloquear franjas de **su propia** agenda.
- **Dado** que la franja ya tiene una reserva activa, **cuando** el empleado intenta bloquearla, **entonces** el sistema lo impide e informa cuántas reservas hay en ese rango *(comportamiento sujeto a PRG-55)*.
- Un bloqueo activo excluye la franja de la disponibilidad desde el momento en que se crea.

---

## 12. Impacto en el modelo de datos

Cambios que las respuestas del cliente obligan a incorporar desde el diseño inicial:

| Entidad | Cambio | Motivo |
|---|---|---|
| `Servicio` | + `unidadCobro ∈ {POR_SERVICIO, POR_DIA}` | CN-01 — el hospedaje se cobra por día |
| `Servicio` | + `modalidadAgenda ∈ {POR_FRANJA, POR_DIAS}` | CN-02 — dos motores de reserva |
| `Servicio` | `duracionMinutos` es nulo cuando `modalidadAgenda = POR_DIAS` | CN-04 |
| `Empleado` | + relación N–M con `Servicio` (servicios que puede prestar) | DEC-C01, HU-021 |
| `JornadaLaboral` | **Nueva.** `empleado`, `diaSemana`, `horaInicio`, `horaFin` | DEC-C04 |
| `BloqueoAgenda` | **Nueva.** `empleado`, `inicio`, `fin`, `motivo` | DEC-C01, HU-024 |
| `Reserva` | + `tipoReserva`, `fechaInicio`, `fechaFin`, `totalCongelado`, `empleado` nulo para hospedaje | CN-01, CN-02 |
| `CupoHospedaje` | **Nueva.** `fecha`, `capacidad`, `ocupados`, `retenidos` | CN-05 — la retención de 15 min necesita su propio contador |
| `Pago` | + estado `SOLICITUD_REEMBOLSO`; + `referenciaExterna`, `metodoPago`, `intentos` | DEC-C07, CON-03 |
| `Promocion` | + `usosMaximosPorCliente` (por defecto 1) | DEC-C10, AMB-09 |
| `UsoPromocion` | **Nueva.** `promocion`, `cliente`, `reserva`, `fecha` — permite RB09 y liberar el uso | DEC-C10, PRG-65 |
| `Auditoria` | **Nueva.** `actor`, `accion`, `entidad`, `entidadId`, `valorPrevio`, `valorNuevo`, `fechaUtc`, `ip` | RNF08-R |
| `Notificacion` | **Nueva.** `destinatario`, `tipoEvento`, `fechaUtc`, `estadoEnvio` | RNF10-R |
| *Todas* | Fechas y horas almacenadas en UTC | RSG-11 |

---

## 13. Máquinas de estado actualizadas

### Reserva

```
                    crear reserva
                          │
                          ▼
                  ┌───────────────┐
        RB04 ◄────┤ PENDIENTE_PAGO├────► EXPIRADA        (RB05, job a 15 min)
     (reintento)  └───────┬───────┘
                          │ pago APROBADO (RB03)
                          ▼
                  ┌───────────────┐
                  │  CONFIRMADA   ├────► CANCELADA       (RB06, > 24 h antes)
                  └───────┬───────┘          │
                          │                  └──► genera SOLICITUD_REEMBOLSO
          empleado inicia │                       si el pago estaba APROBADO (RB07)
                          ▼
                  ┌───────────────┐
                  │  EN_PROCESO   │
                  └───────┬───────┘
                          │ empleado finaliza
                          ▼
                  ┌───────────────┐
                  │  COMPLETADA   │
                  └───────────────┘

   CONFIRMADA ──► NO_ASISTIO   (empleado; efecto sobre el pago pendiente de PRG-56)
```

### Pago

```
        ┌───────────┐
        │ PENDIENTE │
        └─────┬─────┘
              ├────────────► APROBADO ──────► SOLICITUD_REEMBOLSO ──► REEMBOLSADO
              │                                   (RB07, automático)   (admin, manual)
              ├────────────► RECHAZADO ──► (reintento: vuelve a PENDIENTE, RB04)
              │
              └────────────► CANCELADO    (la reserva expiró, RB05)
```

> `SOLICITUD_REEMBOLSO` es el estado nuevo que resuelve CON-03: separa "hay que devolver el dinero" de "el dinero ya se devolvió".

---

## 14. Riesgos actualizados

| ID | Riesgo | Cambio respecto a Fase 1 |
|---|---|---|
| **RSG-02** Alcance excesivo | 🔴 **Escalado a crítico y cuantificado.** La brecha es de 4,8×. Es ahora el riesgo principal del proyecto y tiene una decisión pendiente del cliente (PRG-67). |
| **RSG-01** Pasarela | 🟠 **Mitigado parcialmente.** DEC-C06 aprueba desarrollar contra el adaptador simulado, que era la mitigación propuesta. Queda abierto si el MVP se acepta sin la integración real (PRG-52). |
| **RSG-06** Permisos sin definir | ✅ **Cerrado** por DEC-C01. |
| **RSG-07** Hospedaje | 🟠 **Se mantiene.** Simplificarlo a cupos reduce el esfuerzo, pero confirma que son dos motores de reserva (CN-02) y añade la retención de cupos (CN-05). Es el área más costosa del MVP: 18 SP. |
| **RSG-14** Pruebas al final | ✅ **Mitigado.** Las pruebas están en la Definición de Terminado de cada sprint. |
| **RSG-15** Funcionalidades ausentes | 🟡 **Parcialmente mitigado.** Ya están en el backlog, pero la recuperación de contraseña queda fuera del plazo de 8 semanas, lo cual es un problema (CN-06). |
| **RSG-19** *(nuevo)* **Sprints de 1 semana con 15 h** | 🟡 Alta sobrecarga proporcional de ceremonias y poco margen: un solo día perdido equivale al 20 % del sprint. Mitigación: ceremonias de 45 min y retrospectiva escrita (CN-10). |
| **RSG-20** *(nuevo)* **Velocidad real desconocida** | 🟠 Todo el plan depende de una estimación no verificada. Mitigación: punto de control obligatorio al cerrar el Sprint 1 (sección 7.4). |
| **RSG-21** *(nuevo)* **Bloqueo de agenda sobre reservas confirmadas** | 🟠 CN-03. Sin decisión, el Sprint 4 se implementa a ciegas sobre un caso que ocurrirá con seguridad. |

---

## Registro de cambios

| Versión | Fecha | Autor | Cambios |
|---|---|---|---|
| 1.0 | 2026-09-15 | Ronald Moreno | Versión inicial. Alcance del MVP y Product Backlog a partir de las 12 decisiones del cliente. |

---

**Fin del documento BKL-PETCARE-001 v1.0**
