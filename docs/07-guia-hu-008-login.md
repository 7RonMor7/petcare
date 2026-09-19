# Guía de implementación — HU-008: Login con JWT

**Sprint 1 · 3 SP · ~4,2 h · IRR-01**

> **Como** usuario registrado, **quiero** iniciar sesión y recibir un token de acceso y uno de refresco, **para** usar la plataforma de forma segura sin reintroducir mi contraseña.

---

## El modelo de sesión que vamos a construir

```
POST /api/v1/auth/login
  correo + contraseña
        │
        ▼
  ┌─────────────────────┐   ┌──────────────────────────┐
  │  accessToken        │   │  refreshToken            │
  │  JWT firmado        │   │  cadena aleatoria opaca  │
  │  vive 15 minutos    │   │  vive 7 días             │
  │  NO se guarda       │   │  se guarda HASHEADO      │
  │  NO se puede revocar│   │  SÍ se puede revocar     │
  └─────────────────────┘   └──────────────────────────┘
        │                              │
        │ va en cada petición          │ solo se usa para
        │ Authorization: Bearer ...    │ pedir un access nuevo
        ▼                              ▼
   los endpoints                POST /api/v1/auth/refresh
```

**Por qué dos tokens y no uno.** Es un compromiso entre dos cosas que se contradicen:

- Un token **con estado** (consultar la base en cada petición) se puede revocar al instante, pero cuesta una consulta por petición.
- Un token **sin estado** (JWT firmado) no cuesta nada validar, pero sigue siendo válido hasta que expira aunque cierres sesión.

La solución estándar: un JWT sin estado de vida corta —15 minutos de exposición si te lo roban— más un token con estado de vida larga que sí puedes revocar. Lo mejor de ambos, con una ventana de riesgo acotada.

---

## Paso 1 — Migración `V3` *(ya está en el proyecto)*

`backend/src/main/resources/db/migration/V3__refresh_token.sql`

**Comprobar:** arranca y verifica `"migracionesAplicadas": 3` en `/api/v1/ping/db`.

---

## Paso 2 — Dependencias JJWT

`backend/pom.xml`. Primero en `<properties>`:

```xml
<jjwt.version>0.12.6</jjwt.version>
```

Y tres dependencias. Spring Boot **no** gestiona la versión de JJWT, por eso hay que ponerla.

**Por qué tres artefactos:** `jjwt-api` son las interfaces contra las que escribes; `jjwt-impl` y `jjwt-jackson` son la implementación y el serializador JSON, y van con `runtime`. Así tu código solo puede usar la API pública: si mañana cambias de implementación, tu código no se entera. Es el mismo principio que aplicaremos con `PaymentGateway`.

---

## Paso 3 — Configuración del secreto

Al final de `application.yml`, bajo el bloque `petcare:` que ya existe.

**Sobre el secreto:** con HS256 la clave debe medir **al menos 32 bytes**. Si es más corta, JJWT lanza `WeakKeyException` al arrancar — y hace bien.

Quien tenga esa clave puede **fabricar tokens válidos para cualquier usuario, incluido un administrador**. Por eso nunca va al repositorio con un valor real: en tu máquina usas el de desarrollo, en producción llega por variable de entorno.

---

## Pasos 4 a 10

Entidad `RefreshToken`, su repositorio, `JwtService`, `RefreshTokenService`, los DTOs, `AutenticacionService`, las excepciones y los dos endpoints. El detalle y el código están en la conversación.

---

## Comprobación final

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | Login con credenciales correctas | `200` con `accessToken`, `refreshToken`, `expiraEn: 900` |
| 2 | Pegar el `accessToken` en jwt.io | Ver `sub`, `roles`, `exp` — y entender que **cualquiera** puede leerlos |
| 3 | Login con contraseña incorrecta | `401` · `CREDENCIALES_INVALIDAS` |
| 4 | Login con correo inexistente | `401` con **el mismo mensaje** que el anterior |
| 5 | Refresh con el token recibido | `200` con un par nuevo |
| 6 | Refresh **otra vez** con el token ya usado | `401` y toda la familia revocada |
| 7 | `SELECT token_hash FROM refresh_token` | 64 caracteres hex, nada parecido al token que tienes |

Los pasos 3 y 4 juntos son el criterio de seguridad de la historia: **el mismo mensaje en ambos casos**, para no revelar qué correos existen.

El paso 6 es la detección de reuso, y merece que lo veas funcionar.

---

## Cerrar

```bash
git add .
git commit -m "HU-008: login con JWT, refresh rotativo y deteccion de reuso"
```

Anota las horas reales frente a las 4,2 estimadas.

**Siguiente:** HU-009 — autorización por rol y permisos. Ahí entra `spring-boot-starter-security` y los endpoints dejan de estar abiertos.
