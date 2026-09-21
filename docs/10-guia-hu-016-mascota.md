# Guía de implementación — HU-016: Registrar mascota

**Sprint 1 · 3 SP · ~4,4 h · IRR-03 · RB01**

> **Como** cliente, **quiero** registrar una mascota con sus datos, **para** poder reservarle servicios.

---

## Qué se construye

`POST /api/v1/mascotas` — exige el permiso `MASCOTA_CREAR`. El dueño se toma **siempre del token**, nunca del cuerpo.

```
MascotaController  →  MascotaService  →  MascotaRepository  →  tabla mascota
      ▲                     ▲
 @PreAuthorize        dueño = id del token
```

## Archivos

| Archivo | Quién |
|---|---|
| `V6__mascota.sql` | Generado, ya en el proyecto |
| `Especie.java`, `Sexo.java` | Tú |
| `Mascota.java` | Tú |
| `MascotaRepository.java` | Tú |
| `MascotaRequest.java`, `MascotaResponse.java` | Tú |
| `MascotaService.java` | Tú |
| `MascotaController.java` | Tú |
| Handler de `HttpMessageNotReadableException` en `ManejadorErrores` | Tú |

## Las decisiones de la historia

- **Enumerados como texto**, con `EnumType.STRING`. Nunca `ORDINAL`.
- **`@JdbcTypeCode(SqlTypes.VARCHAR)`** en los enumerados: Hibernate 6 en MySQL espera un tipo `ENUM` nativo; así le decimos que es `VARCHAR`.
- **`BigDecimal` para el peso**, contra una columna `DECIMAL(5,2)`.
- **`LocalDate` para la fecha de nacimiento**: fecha civil, no instante.
- **El DTO de entrada no tiene `clienteId`**: por eso es imposible asignar la mascota a otro. Protección contra *mass assignment* por diseño.
- **`getReferenceById`** para asociar el dueño sin hacer un `SELECT`.
- **`MascotaResponse.desde(mascota)`**: el mapeo entidad → DTO vive en un solo sitio.

## Comprobación

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | Registrar mascota válida | `201` con `clienteId` = id de Ana |
| 2 | Enviar `"clienteId": 999` en el cuerpo | `201` y `clienteId` sigue siendo el de Ana |
| 3 | Fecha de nacimiento futura | `400` con detalle del campo |
| 4 | Peso `0` o `250` | `400` con detalle del campo |
| 5 | Especie `"LORO"` | `400` · `CUERPO_INVALIDO` |
| 6 | Sin token | `401` |
| 7 | Consulta a la tabla | `especie` guardada como texto `PERRO` |

## Cerrar

```bash
git add .
git commit -m "HU-016: registro de mascota con propiedad tomada del token"
```

**Siguiente:** bloque de frontend de autenticación (HU-007 a HU-010) + formulario de mascota.
