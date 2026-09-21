# Guía de implementación — HU-010: Cierre de sesión con revocación

**Sprint 1 · 2 SP · ~1,6 h · IRR-02**

> **Como** usuario, **quiero** cerrar sesión y que mi token de refresco quede revocado, **para** que nadie pueda seguir usando mi sesión.

---

## Qué se construye

`POST /api/v1/auth/logout` — requiere access token + refresh token en el cuerpo. Revoca **toda la familia** del refresh token y responde `204`.

Sin migración: la tabla `refresh_token` y el método `revocarFamilia` ya existen desde HU-008.

## Tres archivos tocados

| Archivo | Cambio |
|---|---|
| `RefreshTokenService` | Método `cerrarSesion(tokenCrudo, usuarioId)` |
| `AutenticacionService` | Método que delega en el anterior |
| `AuthController` | Endpoint `POST /logout` |

## Decisiones

- **Se revoca la familia, no solo el token.** Cerrar sesión termina la cadena completa que nació de ese login.
- **Se verifica que el token pertenezca al usuario autenticado.** El id sale del `SecurityContext`, no del cuerpo.
- **Es idempotente.** Token inexistente, ajeno o ya revocado → `204` igualmente. Cerrar una sesión ya cerrada no es un error, y responder distinto filtraría información.
- **El access token sigue vivo hasta que expire** (máx. 15 min). Limitación conocida de JWT sin estado, aceptada en HU-008.

## Comprobación

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | `POST /logout` con Bearer + refresh token | `204` |
| 2 | `POST /refresh` con ese refresh token | `401` · `TOKEN_INVALIDO` |
| 3 | Repetir el logout | `204` (idempotente) |
| 4 | `POST /logout` sin Bearer | `401` · `NO_AUTENTICADO` |
| 5 | `GET /auth/yo` con el mismo access token | `200` — sigue vivo hasta expirar |
| 6 | Consulta a `refresh_token` | Toda la familia con `revocado = 1` |

## Cerrar

```bash
git add .
git commit -m "HU-010: cierre de sesion con revocacion de la familia de tokens"
```

**Bloque de autenticación del backend cerrado:** HU-007, HU-008, HU-009, HU-010.
