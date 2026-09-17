# Historias de usuario detalladas — Sprint 0 y Sprint 1

**Plataforma PetCare · Fase 3 — Ejecución**

| Campo | Valor |
|---|---|
| Proyecto | Plataforma web de reservas y pagos — PetCare |
| Documento | SPR-PETCARE-001 |
| Versión | 1.0 |
| Fecha | 2026-09-16 |
| Autor | Ronald Moreno |
| Línea base | BKL-PETCARE-003 v3.0 — **aprobada 16/09/2026** |
| Sprints cubiertos | S0 (10 SP) y S1 (14 SP) — 9 historias, 24 SP |
| Entrega | 1 de 2 — semana 8 |

---

## Antes de empezar: un dato que conviene tener desde el primer día

Al desglosar las nueve historias en tareas con horas, los totales salen así:

| Sprint | SP | Horas de tareas | Capacidad efectiva | Margen |
|---|---:|---:|---:|---:|
| S0 | 10 | **11,9 h** | 11,4 h | −0,5 h |
| S1 | 14 | **18,3 h** | 11,4 h | **−6,9 h** |

El Sprint 0 cuadra. **El Sprint 1, tal como está planificado, pide un 60 % más de horas de las disponibles.**

Esto no reabre la línea base ni la contradice: es exactamente el tipo de señal que el punto de control del Sprint 2 existe para capturar, solo que aparece antes de escribir la primera línea de código. Tres matices antes de sacar conclusiones:

1. **Un desglose de tareas no es una medición.** Enumerar trabajo tiende a sobrestimar, porque cuenta cada pieza por separado y no el arrastre que se gana al estar dentro del contexto. La única cifra que vale es la real.
2. **S1 es, junto a S4 y S6, uno de los tres sprints más cargados** del plan (14 SP frente a una media de 12,75). Que sea el que más se desvía era previsible.
3. **La decisión correcta ahora es medir, no replanificar.** Se ejecuta el Sprint 0, se anotan las horas reales y se compara. La sección 8 explica qué hacer con cada resultado.

Lo que sí cambia desde ya, y no cuesta nada: **empezar cada sprint por la tarea de mayor incertidumbre, no por la más cómoda.** En el Sprint 0 eso significa atacar el despliegue (HU-005) antes que el andamiaje.

---

## Tabla de contenido

1. [Convenciones](#1-convenciones)
2. [Decisiones técnicas del Sprint 0 (ADR)](#2-decisiones-técnicas-del-sprint-0-adr)
3. [Sprint 0 — Fundación](#3-sprint-0--fundación)
4. [Sprint 1 — Autenticación y primera mascota](#4-sprint-1--autenticación-y-primera-mascota)
5. [Contrato de API del Sprint 1](#5-contrato-de-api-del-sprint-1)
6. [Modelo de datos y migraciones](#6-modelo-de-datos-y-migraciones)
7. [Catálogo de permisos por rol](#7-catálogo-de-permisos-por-rol)
8. [Medición de velocidad](#8-medición-de-velocidad)
9. [Checklist de arranque](#9-checklist-de-arranque)

---

## 1. Convenciones

### 1.1 Formato de cada historia

Cada historia lleva: identificador y título, la historia en formato *Como… quiero… para…*, criterios de aceptación en formato Dado/Cuando/Entonces, desglose de tareas con horas, dependencias y notas de riesgo. Los criterios están redactados para poder marcarse como cumplidos o no cumplidos sin discusión.

### 1.2 Definition of Done

Vigente de BKL-PETCARE-001 §10. Se recuerda porque las horas de cada historia **ya la incluyen**:

- Código en la rama principal, con commit descriptivo.
- Criterios de aceptación verificados.
- Pruebas unitarias de la lógica de negocio que la historia introduce.
- Pruebas de servicio de los casos límite y errores esperados.
- Validaciones de entrada con mensajes de error claros.
- Endpoints nuevos protegidos según la matriz de permisos.
- Sin datos sensibles en logs.
- Migraciones versionadas en Flyway.
- Interfaz funcional a 360 px y a 1280 px.
- La aplicación sigue desplegándose correctamente.

### 1.3 Ramas y commits

`main` protegida. Una rama por historia: `feat/HU-007-registro-cliente`. Commits en imperativo y en español, referenciando la historia: `HU-007: validar unicidad del correo en el registro`.

---

## 2. Decisiones técnicas del Sprint 0 (ADR)

Estas decisiones estructuran todo lo demás y son baratas ahora, caras después. Se registran como *Architecture Decision Records* en `docs/adr/`.

| ADR | Decisión | Justificación |
|---|---|---|
| **ADR-001** | Java 21 (LTS) + Spring Boot 3.3.x + Maven | LTS con soporte largo; Spring Boot 3.x exige Java 17+. Fijar la versión evita que una actualización rompa el proyecto a mitad de camino |
| **ADR-002** | Estructura modular por dominio, no por capa técnica | `auth`, `usuarios`, `mascotas`, `servicios`, `empleados`, `reservas`, `pagos`, `promociones`, `notificaciones`, `auditoria`, `common`. Cada uno con `controller`, `service`, `repository`, `domain`, `dto`, `mapper`. Cumple RNF05-R y permite que RNF06-R (añadir módulos sin tocar los existentes) sea verificable |
| **ADR-003** | Versionado de API en la ruta: `/api/v1/...` | Barato ahora, imposible de añadir sin romper clientes después |
| **ADR-004** | Formato de error uniforme en todas las respuestas de error | Un `@RestControllerAdvice` central. El frontend maneja un único contrato de error en lugar de uno por endpoint. Definido en §5.1 |
| **ADR-005** | Dominio en español, técnica en inglés | Entidades, campos, endpoints y estados en español (`Reserva`, `fechaNacimiento`, `/api/v1/mascotas`, `PENDIENTE_PAGO`); anotaciones, patrones y términos del framework en inglés (`@Service`, `Repository`, `DTO`). El negocio se habla en español y traducirlo introduce ambigüedad gratuita. Lo importante es fijarlo ahora: la mezcla incoherente es lo que hace ilegible un proyecto |
| **ADR-006** | Autorización por **permisos**, no por rol cableado | `Rol` N–M `Permiso` en base de datos. Aunque en el MVP haya exactamente tres roles, añadir un permiso a medida no debe obligar a tocar código. Decisión ya anticipada como DEC-06 en la Fase 1 |
| **ADR-007** | Todas las fechas y horas en UTC en base de datos | `TIMESTAMP` / `Instant`. Conversión a `America/Bogota` solo en la capa de presentación. Zona horaria de la JVM y de MySQL fijadas explícitamente. Mitiga RSG-11 |
| **ADR-008** | Esquema gestionado por Flyway; `ddl-auto: validate` | Nunca `update` ni `create`. El esquema es código versionado, no un efecto secundario del arranque |
| **ADR-009** | Parámetros de negocio en tabla `parametro_sistema` | Tamaño de franja, antelación mínima y máxima, minutos de expiración, capacidad de hospedaje. Es lo que hace reversible la decisión de PRG-70 sin tocar código |
| **ADR-010** | Pasarela de pago seleccionada | **Resultado de HU-003.** Se redacta al cerrar el spike |
| **ADR-011** | Proveedor de despliegue | **Resultado de HU-005.** Se redacta al cerrar la historia |

---

## 3. Sprint 0 — Fundación

> **Objetivo del sprint:** *"La aplicación existe, está desplegada en una URL pública y sé con qué pasarela voy a trabajar."*

| | |
|---|---|
| Historias | HU-001, HU-002, HU-003, HU-005 |
| Story points | 10 |
| Horas de tareas | 11,9 h |
| Capacidad efectiva | 11,4 h |
| Orden recomendado | **HU-005 primero** (mayor incertidumbre), luego HU-001, HU-002, HU-003 |

> **Sobre el orden:** desplegar antes de tener algo que desplegar parece contraintuitivo, pero HU-005 es la única historia del sprint que depende de un tercero y puede fallar por motivos ajenos al código. Descubrirlo el lunes deja seis días para reaccionar; descubrirlo el sábado, ninguno. Basta con un `hello world` de Spring Boot para empezar.

---

### HU-001 — Estructura modular del backend · 3 SP 🔒

**Como** desarrollador, **quiero** un proyecto Spring Boot con estructura modular por dominio, Docker Compose, Flyway y perfiles de entorno, **para** tener una base desplegable, versionada y verificable desde el primer día.

#### Criterios de aceptación

- **CA-1.** El proyecto compila con `mvn clean verify` sin errores ni avisos de dependencias no resueltas.
- **CA-2.** Existen los paquetes de ADR-002, cada uno con sus subpaquetes `controller`, `service`, `repository`, `domain`, `dto`, `mapper`. Los módulos aún vacíos contienen solo el `package-info.java`.
- **CA-3.** `docker compose up` levanta MySQL 8 y la aplicación; la aplicación conecta a la base de datos sin configuración manual.
- **CA-4.** Existen los perfiles `local`, `dev` y `prod`. **Ningún secreto está en el repositorio**: credenciales, claves y URLs vienen de variables de entorno, con `.env.example` documentando cuáles.
- **CA-5.** Flyway ejecuta `V1__esquema_base.sql` al arrancar. `spring.jpa.hibernate.ddl-auto` está en `validate` (ADR-008).
- **CA-6.** `GET /actuator/health` devuelve `200` con `{"status":"UP"}` e incluye el estado de la base de datos.
- **CA-7.** Existe una prueba ArchUnit que falla si un `controller` accede directamente a un `repository`, y otra que falla si hay dependencias cíclicas entre módulos de dominio.
- **CA-8.** El `README.md` explica cómo levantar el proyecto en local en menos de cinco pasos.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Generar el proyecto (Spring Initializr): web, data-jpa, security, validation, actuator, flyway, mysql, lombok | 0,2 |
| 2 | Crear la estructura de paquetes de ADR-002 con `package-info.java` | 0,5 |
| 3 | `docker-compose.yml` con MySQL 8, volumen persistente y healthcheck | 0,5 |
| 4 | Configurar Flyway y escribir `V1__esquema_base.sql` | 0,3 |
| 5 | Perfiles `local`/`dev`/`prod`, externalización de configuración y `.env.example` | 0,4 |
| 6 | Pruebas ArchUnit (capas y ciclos) + prueba de humo del contexto | 0,7 |
| 7 | `README.md` de arranque | 0,2 |
| | **Total** | **2,8** |

**Dependencias:** ninguna. **Riesgo:** bajo.

---

### HU-002 — Base del frontend · 2 SP 🔒

**Como** desarrollador, **quiero** un proyecto React con Tailwind, enrutado y un cliente HTTP con manejo de token, **para** consumir la API de forma uniforme y no repetir la lógica de sesión en cada pantalla.

#### Criterios de aceptación

- **CA-1.** El proyecto arranca con `npm run dev` y compila con `npm run build` sin errores.
- **CA-2.** Tailwind está configurado y aplica estilos; existe un layout base con cabecera y contenedor responsive.
- **CA-3.** El enrutado distingue rutas públicas de privadas. Un componente `RutaProtegida` redirige a `/login` cuando no hay sesión.
- **CA-4.** El cliente Axios toma la URL base de una variable de entorno, nunca de una constante en el código.
- **CA-5.** Un interceptor de petición añade `Authorization: Bearer <token>` cuando hay token.
- **CA-6.** Un interceptor de respuesta, ante un `401`, intenta **una sola vez** renovar el token; si falla, limpia la sesión y redirige a `/login`. Dos peticiones simultáneas que reciben `401` no disparan dos renovaciones.
- **CA-7.** El estado de sesión vive en un contexto de React accesible desde cualquier pantalla.
- **CA-8.** El layout no produce desplazamiento horizontal a 360 px.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Scaffold con Vite + React + Tailwind; estructura de carpetas | 0,5 |
| 2 | Enrutado, layout base y `RutaProtegida` | 0,7 |
| 3 | Cliente Axios con ambos interceptores y control de renovación concurrente | 1,0 |
| 4 | `AuthContext` con estado de sesión y persistencia del token | 0,5 |
| | **Total** | **2,7** |

**Dependencias:** ninguna (el interceptor de renovación se prueba de verdad en el Sprint 1, contra HU-008).
**Riesgo:** medio en la tarea 3. El control de renovación concurrente es la parte donde suelen aparecer bucles de peticiones; conviene resolverlo con una única promesa compartida.

---

### HU-003 — Spike: selección de pasarela · 2 SP 🔒

**Como** equipo, **quiero** comparar Wompi, Mercado Pago y PayU con criterios definidos, **para** elegir la pasarela con evidencia y no por costumbre.

> **Timebox estricto: 3 horas.** Al agotarse, se decide con lo que haya. Un spike sin límite de tiempo deja de ser un spike.

#### Criterios de aceptación

- **CA-1.** Existe una tabla comparativa de los tres proveedores sobre estos siete criterios:
  1. Métodos soportados: tarjeta, **PSE**, **Nequi**.
  2. Sandbox disponible **sin contrato firmado** y sin cuenta de comercio aprobada.
  3. Calidad de la documentación en español.
  4. SDK oficial para Java, o API REST suficientemente documentada.
  5. Mecanismo de confirmación: webhook, firma, política de reintentos, idempotencia.
  6. Requisitos para abrir cuenta de comercio en Colombia.
  7. Comisiones por transacción.
- **CA-2.** Existe una recomendación razonada de media página, con el criterio que resultó decisivo.
- **CA-3.** La decisión queda registrada como **ADR-010**.
- **CA-4.** **No se escribe código de integración.** El spike produce conocimiento y una decisión, no implementación.
- **CA-5.** Queda documentado, para cada proveedor, cómo se firman los webhooks — es el dato que HU-041 necesitará en el Sprint 7.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Revisar documentación de los tres proveedores sobre los siete criterios | 2,0 |
| 2 | Tabla comparativa y recomendación | 0,7 |
| 3 | Redactar ADR-010 | 0,3 |
| | **Total** | **3,0** |

**Dependencias:** ninguna.
**Riesgo:** que la documentación pública no aclare si el sandbox exige cuenta aprobada. Si al cerrar el timebox eso sigue sin estar claro en los tres, se elige por los demás criterios y se anota como incertidumbre abierta: DEC-C13 ya cubre el escenario de desarrollar contra el adaptador simulado.

---

### HU-005 — Despliegue público mínimo · 3 SP 🔒

**Como** equipo, **quiero** la aplicación desplegada en una URL pública con HTTPS, **para** validar el entorno temprano y disponer de un endpoint alcanzable por los webhooks.

> **Esta es la historia crítica del sprint.** Los webhooks del Sprint 7 no llegan a `localhost`. Sin una URL pública funcionando, el cierre del ciclo de pago no se puede probar.

#### Criterios de aceptación

- **CA-1.** El backend responde en una URL pública con **HTTPS válido**.
- **CA-2.** El frontend responde en una URL pública con HTTPS válido y consume la API del backend sin errores de CORS.
- **CA-3.** Existe una base de datos MySQL accesible desde el backend desplegado, con sus credenciales en variables de entorno del proveedor.
- **CA-4.** `GET /actuator/health` devuelve `200` **desde internet**, no solo desde local.
- **CA-5.** Flyway aplica las migraciones en el entorno desplegado al arrancar.
- **CA-6.** No hay ningún secreto en el repositorio ni en la imagen.
- **CA-7.** El procedimiento de despliegue está documentado en `docs/despliegue.md`: pasos, variables necesarias y cómo revertir.
- **CA-8.** La decisión de proveedor queda registrada como **ADR-011**, incluyendo si la instancia se suspende por inactividad y cuánto tarda el arranque en frío — dato que condiciona RNF02-R y RNF03-R.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Elegir proveedor y crear la cuenta | 0,3 |
| 2 | Provisionar MySQL gestionado y verificar conectividad | 0,8 |
| 3 | Desplegar el backend con variables de entorno y verificar migraciones | 1,3 |
| 4 | Desplegar el frontend y configurar CORS | 0,7 |
| 5 | Verificar HTTPS, documentar el procedimiento y redactar ADR-011 | 0,3 |
| | **Total** | **3,4** |

**Dependencias:** HU-001 debe existir al menos como esqueleto arrancable.
**Riesgo: alto — es el mayor del sprint.** Los planes gratuitos de MySQL gestionado son escasos y a veces caducan (RSG-09). Si el proveedor elegido no ofrece uno viable, la alternativa es MySQL en un contenedor del mismo proveedor o un plan de pago mínimo. **Si la tarea 2 supera 1,5 h, conviene cambiar de proveedor en lugar de insistir.**

---

## 4. Sprint 1 — Autenticación y primera mascota

> **Objetivo del sprint:** *"Un cliente se registra, inicia sesión y registra su mascota. Los endpoints están protegidos por rol."*

| | |
|---|---|
| Historias | HU-007, HU-008, HU-009, HU-010, HU-016 |
| Story points | 14 |
| Horas de tareas | 18,3 h |
| Capacidad efectiva | 11,4 h |
| Orden obligado | HU-007 → HU-008 → HU-009 → HU-010 → HU-016 |

> **Este sprint no cuadra en horas** (sección "Antes de empezar"). Si al cerrar el Sprint 0 las horas reales confirman el desajuste, el candidato natural a moverse es **HU-016** (3 SP): es la única historia del sprint que no bloquea a ninguna otra, y el Sprint 2 ya la necesita como base. Mover HU-016 deja S1 en 11 SP y S2 en 16, lo que exige a su vez sacar 2 SP de S2 hacia S3. **No se decide ahora: se decide con las horas reales del Sprint 0 en la mano.**

---

### HU-007 — Registro de cliente · 3 SP 🔒

**Como** visitante, **quiero** registrarme con mis datos y aceptar la política de tratamiento, **para** poder reservar servicios para mis mascotas.

#### Criterios de aceptación

```gherkin
Escenario: Registro exitoso
  Dado que no existe ninguna cuenta con el correo "ana@ejemplo.com"
  Cuando envío nombre, apellido, correo, teléfono, contraseña válida
        y la aceptación de la política de tratamiento
  Entonces el sistema responde 201
  Y crea el usuario con el rol CLIENTE
  Y la respuesta contiene id, nombre, apellido, correo y rol
  Y la respuesta NO contiene la contraseña ni su hash

Escenario: Correo ya registrado
  Dado que ya existe una cuenta con el correo "ana@ejemplo.com"
  Cuando intento registrarme con ese mismo correo
  Entonces el sistema responde 409 con el código CORREO_YA_REGISTRADO

Escenario: Contraseña insuficiente
  Cuando envío una contraseña de menos de 8 caracteres
  Entonces el sistema responde 400 con el detalle del campo "contrasena"

Escenario: Sin aceptar la política de tratamiento
  Cuando envío "aceptaPoliticaDatos": false
  Entonces el sistema responde 400
  Y no se crea ningún usuario
```

- **CA-5.** La contraseña se almacena con **BCrypt, factor de coste ≥ 10** (RNF01-R). Verificable inspeccionando la tabla.
- **CA-6.** Se registran `fechaAceptacionPolitica` y `versionPolitica` (Ley 1581 de 2012, GAP-24).
- **CA-7.** La contraseña no aparece en ningún log, ni siquiera en nivel `DEBUG`.
- **CA-8.** El formulario del frontend valida en cliente, muestra los errores del servidor por campo y deshabilita el envío mientras la petición está en curso.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Entidades `Usuario` y `Rol`, repositorios y migración `V2` | 0,8 |
| 2 | DTO de entrada y salida con validaciones Bean Validation | 0,5 |
| 3 | Servicio de registro: unicidad, BCrypt, asignación de rol, política | 0,5 |
| 4 | Controlador y manejo de errores del `@RestControllerAdvice` | 0,5 |
| 5 | Pruebas unitarias del servicio y de controlador (los cuatro escenarios) | 1,0 |
| 6 | Formulario de registro en React con validación y estados | 1,0 |
| | **Total** | **4,3** |

**Riesgo:** bajo.

---

### HU-008 — Inicio de sesión con JWT · 3 SP 🔒

**Como** usuario registrado, **quiero** iniciar sesión y recibir un token de acceso y uno de refresco, **para** usar la plataforma de forma segura sin reintroducir mi contraseña.

#### Criterios de aceptación

```gherkin
Escenario: Login correcto
  Dado un usuario activo con credenciales válidas
  Cuando envío correo y contraseña a /api/v1/auth/login
  Entonces recibo 200 con accessToken, refreshToken y los datos del usuario
  Y el accessToken expira en 15 minutos
  Y el refreshToken expira en 7 días

Escenario: Credenciales inválidas
  Cuando envío una contraseña incorrecta, o un correo inexistente
  Entonces recibo 401 con el mismo mensaje genérico en ambos casos
  Y el sistema no revela si el correo existe

Escenario: Renovación del token
  Dado un refreshToken válido y no revocado
  Cuando lo envío a /api/v1/auth/refresh
  Entonces recibo un accessToken nuevo y un refreshToken nuevo
  Y el refreshToken anterior queda revocado

Escenario: Reutilización de un token ya usado
  Dado un refreshToken que ya fue canjeado
  Cuando lo envío de nuevo
  Entonces recibo 401
  Y se revocan todos los tokens de esa familia
```

- **CA-5.** El *access token* incluye `sub`, `roles`, `permisos`, `iat`, `exp`, `jti`. **No incluye datos personales** más allá del identificador.
- **CA-6.** El *refresh token* se almacena **hasheado** en base de datos, nunca en claro.
- **CA-7.** La clave de firma proviene de una variable de entorno; no hay ninguna clave por defecto en el código.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | `JwtService`: emisión, validación, extracción de claims | 1,0 |
| 2 | Entidad `RefreshToken`, migración `V3`, rotación y detección de reuso | 1,0 |
| 3 | Endpoints `/login` y `/refresh` | 0,7 |
| 4 | Pruebas de los cuatro escenarios | 0,8 |
| 5 | Pantalla de login y almacenamiento de la sesión | 0,7 |
| | **Total** | **4,2** |

**Dependencias:** HU-007.
**Riesgo:** medio. La detección de reutilización (revocar toda la familia) es la parte sutil; conviene modelarla con un identificador de familia en la propia entidad.

---

### HU-009 — Autorización por rol y permisos · 3 SP 🔒

**Como** sistema, **quiero** autorizar cada endpoint según el rol y los permisos del usuario, **para** que nadie acceda a lo que no le corresponde.

#### Criterios de aceptación

```gherkin
Escenario: Acceso sin token
  Cuando llamo a un endpoint protegido sin cabecera Authorization
  Entonces recibo 401 con el formato de error uniforme

Escenario: Token válido sin el permiso requerido
  Dado un usuario con rol CLIENTE
  Cuando llamo a un endpoint que exige el permiso SERVICIO_GESTIONAR
  Entonces recibo 403

Escenario: Token expirado
  Cuando uso un accessToken caducado
  Entonces recibo 401 con el código TOKEN_EXPIRADO
  Y el frontend lo renueva automáticamente una vez

Escenario: Endpoint público
  Cuando llamo a /api/v1/auth/registro sin token
  Entonces la petición se procesa normalmente
```

- **CA-5.** Existe el modelo `Rol` N–M `Permiso` en base de datos (ADR-006), no constantes en código.
- **CA-6.** Los roles y permisos de la sección 7 se cargan por migración Flyway, no manualmente.
- **CA-7.** Los endpoints públicos están declarados en una **lista blanca explícita**. Todo lo no declarado requiere autenticación por defecto.
- **CA-8.** Existe una prueba de integración que, por cada rol, comprueba el acceso permitido y el denegado sobre al menos un endpoint.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Entidad `Permiso`, relación N–M y migración `V4` con la semilla | 0,8 |
| 2 | `JwtAuthenticationFilter` y `SecurityFilterChain` con lista blanca | 1,0 |
| 3 | Anotaciones `@PreAuthorize` por permiso y `@EnableMethodSecurity` | 0,5 |
| 4 | Manejo uniforme de 401 y 403 en el `@RestControllerAdvice` | 0,5 |
| 5 | Pruebas de acceso por rol | 1,0 |
| | **Total** | **3,8** |

**Dependencias:** HU-008.
**Riesgo:** medio. Es la historia que más se arrastra si queda mal hecha: todos los endpoints posteriores dependen de ella.

---

### HU-010 — Cierre de sesión con revocación · 2 SP 🔒

**Como** usuario, **quiero** cerrar sesión y que mi token de refresco quede revocado, **para** que nadie pueda seguir usando mi sesión desde otro dispositivo.

#### Criterios de aceptación

```gherkin
Escenario: Cierre de sesión
  Dado que tengo una sesión activa
  Cuando llamo a /api/v1/auth/logout con mi refreshToken
  Entonces recibo 204
  Y ese refreshToken queda marcado como revocado

Escenario: Uso posterior del token revocado
  Dado un refreshToken revocado por cierre de sesión
  Cuando intento renovarlo
  Entonces recibo 401
```

- **CA-3.** El *access token* sigue siendo válido hasta su expiración natural (máximo 15 minutos). **Esto es una propiedad conocida de JWT sin estado y queda documentada**, no es un defecto: revocar el acceso de forma inmediata exigiría consultar la base de datos en cada petición, lo que se descartó por coste.
- **CA-4.** El frontend limpia el contexto de sesión y el almacenamiento local, y redirige a `/login`.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Endpoint `/logout` y revocación en base de datos | 0,5 |
| 2 | Pruebas de los dos escenarios | 0,5 |
| 3 | Cierre de sesión en el frontend y limpieza del contexto | 0,4 |
| 4 | Documentar la ventana de validez del access token | 0,2 |
| | **Total** | **1,6** |

**Dependencias:** HU-008. **Riesgo:** bajo.

---

### HU-016 — Registrar mascota · 3 SP 🔒

**Como** cliente, **quiero** registrar una mascota con sus datos, **para** poder reservarle servicios.

#### Criterios de aceptación

```gherkin
Escenario: Registro exitoso
  Dado que estoy autenticado como CLIENTE
  Cuando envío nombre, especie, raza, sexo, fecha de nacimiento,
        peso y observaciones
  Entonces recibo 201 con la mascota creada
  Y la mascota queda asociada a mi cuenta

Escenario: Intento de asignar la mascota a otro cliente
  Dado que estoy autenticado como el cliente A
  Cuando envío un cuerpo que incluye "clienteId" del cliente B
  Entonces la mascota se asocia al cliente A
  Y el campo enviado se ignora por completo

Escenario: Fecha de nacimiento futura
  Cuando envío una fecha de nacimiento posterior a hoy
  Entonces recibo 400 con el detalle del campo

Escenario: Peso fuera de rango
  Cuando envío un peso menor o igual a 0, o mayor a 200 kg
  Entonces recibo 400 con el detalle del campo
```

- **CA-5.** El cliente propietario se toma **siempre** del token, nunca del cuerpo de la petición. Es la base de RB01 y del resto del control de propiedad.
- **CA-6.** `especie` y `sexo` son enumerados: `especie ∈ {PERRO, GATO, OTRO}`, `sexo ∈ {MACHO, HEMBRA}`. Un valor no contemplado devuelve `400`.
- **CA-7.** `observaciones` admite hasta 500 caracteres y es opcional.
- **CA-8.** El endpoint exige el permiso `MASCOTA_CREAR`.
- **CA-9.** El formulario funciona a 360 px sin desplazamiento horizontal.

#### Tareas

| # | Tarea | h |
|:--:|---|---:|
| 1 | Entidad `Mascota`, enumerados, repositorio y migración `V5` | 0,7 |
| 2 | DTO con validaciones y mapeo | 0,6 |
| 3 | Servicio: asociación al cliente autenticado ignorando el cuerpo | 0,5 |
| 4 | Controlador con `@PreAuthorize("hasAuthority('MASCOTA_CREAR')")` | 0,3 |
| 5 | Pruebas de los cuatro escenarios | 1,0 |
| 6 | Formulario de registro de mascota en React | 1,3 |
| | **Total** | **4,4** |

**Dependencias:** HU-009.
**Riesgo:** bajo, pero **CA-5 es importante**: es el primer sitio donde se establece el patrón de propiedad que después sostiene RB01 en las reservas. Conviene resolverlo bien aquí para copiarlo después.

---

## 5. Contrato de API del Sprint 1

### 5.1 Formato de error uniforme (ADR-004)

Todas las respuestas de error, sin excepción:

```json
{
  "marcaTiempo": "2026-09-16T14:03:22Z",
  "estado": 400,
  "codigo": "VALIDACION_FALLIDA",
  "mensaje": "La solicitud contiene campos inválidos",
  "ruta": "/api/v1/auth/registro",
  "detalles": [
    { "campo": "contrasena", "mensaje": "Debe tener al menos 8 caracteres" }
  ],
  "traceId": "9f2a4c1e"
}
```

`detalles` solo aparece en errores de validación. `traceId` permite correlacionar con los logs y es la base de la trazabilidad que pedirá RNF08-R.

**Códigos definidos en este sprint:** `VALIDACION_FALLIDA`, `CORREO_YA_REGISTRADO`, `CREDENCIALES_INVALIDAS`, `TOKEN_EXPIRADO`, `TOKEN_INVALIDO`, `TOKEN_REVOCADO`, `ACCESO_DENEGADO`, `RECURSO_NO_ENCONTRADO`, `ERROR_INTERNO`.

### 5.2 Endpoints

| Método | Ruta | Auth | Permiso | Éxito | Errores |
|---|---|:--:|---|:--:|---|
| `POST` | `/api/v1/auth/registro` | Pública | — | `201` | `400`, `409` |
| `POST` | `/api/v1/auth/login` | Pública | — | `200` | `400`, `401` |
| `POST` | `/api/v1/auth/refresh` | Pública | — | `200` | `401` |
| `POST` | `/api/v1/auth/logout` | Sí | — | `204` | `401` |
| `POST` | `/api/v1/mascotas` | Sí | `MASCOTA_CREAR` | `201` | `400`, `401`, `403` |
| `GET` | `/actuator/health` | Pública | — | `200` | — |

#### `POST /api/v1/auth/registro`

```json
// Petición
{
  "nombre": "Ana", "apellido": "Restrepo",
  "correo": "ana@ejemplo.com", "telefono": "3001234567",
  "contrasena": "Segura123", "aceptaPoliticaDatos": true
}
// Respuesta 201
{
  "id": 1, "nombre": "Ana", "apellido": "Restrepo",
  "correo": "ana@ejemplo.com", "rol": "CLIENTE"
}
```

#### `POST /api/v1/auth/login`

```json
// Petición
{ "correo": "ana@ejemplo.com", "contrasena": "Segura123" }
// Respuesta 200
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "eyJhbGciOi...",
  "expiraEn": 900,
  "usuario": { "id": 1, "nombre": "Ana", "correo": "ana@ejemplo.com", "rol": "CLIENTE" }
}
```

#### `POST /api/v1/mascotas`

```json
// Petición
{
  "nombre": "Max", "especie": "PERRO", "raza": "Golden Retriever",
  "sexo": "MACHO", "fechaNacimiento": "2022-03-15",
  "pesoKg": 28.5, "observaciones": "Nervioso con desconocidos"
}
// Respuesta 201 — nótese que clienteId lo pone el servidor, no el cliente
{
  "id": 1, "nombre": "Max", "especie": "PERRO", "raza": "Golden Retriever",
  "sexo": "MACHO", "fechaNacimiento": "2022-03-15", "pesoKg": 28.5,
  "observaciones": "Nervioso con desconocidos", "clienteId": 1
}
```

---

## 6. Modelo de datos y migraciones

| Migración | Sprint | Contenido |
|---|:--:|---|
| `V1__esquema_base.sql` | S0 | Tabla de control y configuración de charset `utf8mb4` |
| `V2__usuario_rol.sql` | S1 | `usuario`, `rol`, `usuario_rol` |
| `V3__refresh_token.sql` | S1 | `refresh_token` |
| `V4__permisos.sql` | S1 | `permiso`, `rol_permiso` y semilla de la sección 7 |
| `V5__mascota.sql` | S1 | `mascota` |

### 6.1 Tablas

**`usuario`** — `id`, `nombre`, `apellido`, `correo` (único), `telefono`, `contrasena_hash`, `activo`, `fecha_aceptacion_politica`, `version_politica`, `creado_en`, `actualizado_en`.

**`rol`** — `id`, `nombre` (único: `CLIENTE`, `EMPLEADO`, `ADMINISTRADOR`), `descripcion`.

**`usuario_rol`** — `usuario_id`, `rol_id`. Clave primaria compuesta.

**`permiso`** — `id`, `nombre` (único), `descripcion`.

**`rol_permiso`** — `rol_id`, `permiso_id`. Clave primaria compuesta.

**`refresh_token`** — `id`, `usuario_id`, `token_hash`, `familia_id`, `expira_en`, `revocado`, `creado_en`. Índice por `token_hash` y por `familia_id`.

**`mascota`** — `id`, `cliente_id` → `usuario.id`, `nombre`, `especie`, `raza`, `sexo`, `fecha_nacimiento`, `peso_kg`, `observaciones`, `activo`, `creado_en`, `actualizado_en`. Índice por `cliente_id`.

> Todas las columnas de fecha y hora son `TIMESTAMP` en UTC (ADR-007). `fecha_nacimiento` es `DATE`, porque es una fecha civil sin hora ni zona.

---

## 7. Catálogo de permisos por rol

Implementa la matriz del Anexo B de DER-PETCARE-001 y la decisión DEC-C01. Se carga por `V4__permisos.sql`. Los permisos de historias posteriores se incluyen ya, para no tener que migrar el catálogo cada sprint.

| Permiso | Cliente | Empleado | Administrador |
|---|:--:|:--:|:--:|
| `MASCOTA_CREAR` | ✅ | | ✅ |
| `MASCOTA_LEER_PROPIA` | ✅ | | |
| `MASCOTA_EDITAR_PROPIA` | ✅ | | |
| `MASCOTA_ELIMINAR_PROPIA` | ✅ | | |
| `MASCOTA_LEER_DE_RESERVA` | | ✅ | ✅ |
| `SERVICIO_LEER` | ✅ | ✅ | ✅ |
| `SERVICIO_GESTIONAR` | | | ✅ |
| `EMPLEADO_GESTIONAR` | | | ✅ |
| `AGENDA_LEER_PROPIA` | | ✅ | |
| `AGENDA_BLOQUEAR_PROPIA` | | ✅ | |
| `DISPONIBILIDAD_CONSULTAR` | ✅ | ✅ | ✅ |
| `RESERVA_CREAR` | ✅ | | |
| `RESERVA_LEER_PROPIA` | ✅ | | |
| `RESERVA_LEER_ASIGNADA` | | ✅ | |
| `RESERVA_LEER_TODAS` | | | ✅ |
| `RESERVA_CANCELAR_PROPIA` | ✅ | | |
| `RESERVA_CAMBIAR_ESTADO_ASIGNADA` | | ✅ | |
| `RESERVA_CAMBIAR_ESTADO_CUALQUIERA` | | | ✅ |
| `PAGO_LEER_PROPIO` | ✅ | | |
| `PAGO_LEER_TODOS` | | | ✅ |
| `REEMBOLSO_GESTIONAR` | | | ✅ |
| `PROMOCION_GESTIONAR` | | | ✅ |
| `PROMOCION_APLICAR` | ✅ | | |
| `AUDITORIA_LEER` | | | ✅ |
| `DASHBOARD_LEER` | | | ✅ |

> `RESERVA_CAMBIAR_ESTADO_CUALQUIERA` existe para el administrador porque RB12 se lo exige: pasadas las dos horas de la ventana, marcar `NO_ASISTIO` solo le corresponde a él.

---

## 8. Medición de velocidad

El punto de control formal es al cerrar el Sprint 2, pero la medición empieza el primer día.

### 8.1 Qué se registra

Por cada historia: SP estimados, horas estimadas (de este documento), **horas reales** y si quedó terminada según la DoD. Una historia a medias cuenta como **cero** puntos completados: medio puente no es medio cruce.

### 8.2 Qué hacer con el resultado del Sprint 0

| Horas reales de S0 | Lectura | Acción |
|---|---|---|
| **≤ 12 h** | La estimación de tareas es fiable y la velocidad asumida se sostiene | Ejecutar S1 tal como está planificado |
| **12 – 16 h** | Ritmo intermedio | Mover HU-016 de S1 a S2 y reequilibrar S2/S3 antes de empezar S1 |
| **> 16 h** | El ritmo real está más cerca de la estimación conservadora | Ejecutar S1 igualmente, pero **avisar al cliente ya**, sin esperar al Sprint 2 |

El tercer caso merece una aclaración: el mecanismo aprobado dice que por debajo de 11,25 SP/sprint se convoca al cliente. Enterarse en la semana 1 y callarlo hasta la semana 3 sería cumplir la letra del acuerdo y traicionar su propósito.

### 8.3 Plantilla de registro

| Sprint | Historia | SP | h estimadas | h reales | Terminada |
|:--:|---|:--:|---:|---:|:--:|
| S0 | HU-005 | 3 | 3,4 | | |
| S0 | HU-001 | 3 | 2,8 | | |
| S0 | HU-002 | 2 | 2,7 | | |
| S0 | HU-003 | 2 | 3,0 | | |
| | **Total S0** | **10** | **11,9** | | |

> Se registran también las horas de S1 con la misma plantilla. El total estimado de S1 es **18,3 h**: si las reales se acercan a esa cifra, el desajuste del sprint queda confirmado con datos y no con suposiciones.

---

## 9. Checklist de arranque

Antes de escribir la primera línea:

- [ ] Repositorio creado en GitHub con `main` protegida y `.gitignore` de Java y Node.
- [ ] `docs/adr/` creado, con ADR-001 a ADR-009 redactados (ADR-010 y ADR-011 salen del sprint).
- [ ] Tablero de sprint con las nueve historias y sus tareas.
- [ ] Hoja de registro de horas de la sección 8.3 preparada.
- [ ] Java 21, Maven, Node 20 y Docker instalados y verificados.
- [ ] Cuenta creada en el proveedor de despliegue candidato.
- [ ] Bloques de trabajo reservados en el calendario: 15 h semanales no aparecen solas.

**Primera tarea del proyecto:** HU-005, tarea 2 — provisionar MySQL gestionado y verificar conectividad. Es la que más puede desviarse y la que menos depende de lo demás.

---

## Registro de cambios

| Versión | Fecha | Cambios |
|---|---|---|
| 1.0 | 2026-09-16 | Historias detalladas de S0 y S1, ADR-001 a ADR-011, contrato de API, modelo de datos, catálogo de permisos y mecanismo de medición de velocidad |

---

**Fin del documento SPR-PETCARE-001 v1.0**
