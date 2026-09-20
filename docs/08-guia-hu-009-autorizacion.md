# Guía de implementación — HU-009: Autorización por rol y permisos

**Sprint 1 · 3 SP · ~3,8 h · IRR-02**

> **Como** sistema, **quiero** autorizar cada endpoint según el rol y los permisos del usuario, **para** que nadie acceda a lo que no le corresponde.

---

## Por dónde pasa una petición a partir de ahora

```
Petición con  Authorization: Bearer eyJ...
      │
      ▼
┌─────────────────────────────────────────┐
│  CADENA DE FILTROS  (Spring Security)   │
│                                         │
│  JwtAuthenticationFilter                │
│    lee la cabecera, valida la firma,    │
│    saca id y permisos del token y los   │
│    deja en el SecurityContext           │
│                                         │
│  ¿ruta pública?  → pasa                 │
│  ¿sin token?     → 401 (EntryPoint)     │
└────────────────┬────────────────────────┘
                 ▼
          DispatcherServlet
                 ▼
     @PreAuthorize("hasAuthority(...)")
          ¿tiene el permiso?
           sí → controlador
           no → 403 (AccessDeniedHandler)
```

**Lo importante:** los filtros corren **antes** que el `DispatcherServlet`. Tu `@RestControllerAdvice` solo ve lo que ocurre dentro de un controlador, así que **no puede capturar los 401 y 403 de seguridad**. Por eso hacen falta dos manejadores aparte.

---

## Los ocho pasos

1. Migración `V5__permisos.sql` *(ya está en el proyecto)*
2. Cambiar `spring-security-crypto` por `spring-boot-starter-security` en el `pom.xml`
3. Entidad `Permiso` y relación desde `Rol`
4. Añadir el claim `permisos` al `JwtService`
5. `JwtAuthenticationFilter`
6. Manejadores de 401 y 403
7. `SecurityFilterChain` en `ConfiguracionSeguridad`
8. Endpoints de prueba: `/auth/yo` y `/ping/admin`

El código y las explicaciones están en la conversación.

---

## Rol y permisos que carga V5

| Rol | Permisos |
|---|---|
| CLIENTE | 11 — mascotas propias, catálogo, disponibilidad, reservas propias, pagos propios, aplicar promoción |
| EMPLEADO | 7 — agenda propia, bloqueos, reservas asignadas y su cambio de estado, ficha de mascota |
| ADMINISTRADOR | 13 — gestión de servicios, empleados, pagos, reembolsos, promociones, auditoría y dashboard |

Fíjate en que **CLIENTE y ADMINISTRADOR no se solapan tanto como parece**: el administrador no tiene `RESERVA_CREAR` ni `MASCOTA_LEER_PROPIA`, porque no es cliente. Eso es DEC-C01 traducido a datos.

---

## Comprobación final

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | `GET /api/v1/auth/yo` sin cabecera | `401` · `NO_AUTENTICADO` |
| 2 | `GET /api/v1/auth/yo` con `Bearer <accessToken>` | `200` con tus datos y tus permisos |
| 3 | `GET /api/v1/auth/yo` con un token manipulado (cambia una letra) | `401` · `TOKEN_INVALIDO` |
| 4 | `GET /api/v1/ping/admin` con el token de Ana (CLIENTE) | `403` · `ACCESO_DENEGADO` |
| 5 | `POST /api/v1/auth/login` sin token | `200` — sigue siendo público |
| 6 | Esperar 15 min y reusar el mismo access token | `401` · `TOKEN_EXPIRADO` |

El paso 4 es el corazón de la historia: **autenticado pero sin permiso** es `403`, no `401`. Son cosas distintas — *"no sé quién eres"* frente a *"sé quién eres y no puedes"*.

---

## Cerrar

```bash
git add .
git commit -m "HU-009: autorizacion por permisos con filtro JWT"
```

**Siguiente:** HU-010 — cierre de sesión con revocación (2 SP, la más corta del sprint).
