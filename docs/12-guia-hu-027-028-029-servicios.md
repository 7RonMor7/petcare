# Guía de implementación — HU-028, HU-027 y HU-029: catálogo y gestión de servicios

**Sprint 2 · 2 + 3 + 1 SP · IRR-04, IRR-16**

> **HU-028.** Como visitante, quiero consultar el catálogo de servicios con sus precios.
> **HU-027.** Como administrador, quiero crear y editar servicios con nombre, descripción, precio,
> duración, unidad de cobro y estado.
> **HU-029.** Como administrador, quiero desactivar un servicio sin afectar las reservas confirmadas.

---

## El modelo: por qué `unidad_cobro` es una columna

CN-01 del análisis: el precio es fijo por servicio, salvo el hospedaje, que se cobra por noche.
Guardarlo como dato y no como una suposición en el código permite que el cálculo del total de la
reserva (Sprint 5) sea uno solo para todos los servicios. El total **se congela al crear la
reserva**: cambiar el precio del catálogo no altera lo ya vendido.

La coherencia entre unidad y duración se garantiza en dos sitios, a propósito:

| Dónde | Qué aporta |
|---|---|
| `CHECK ck_servicio_duracion` en V7 | Ninguna fila incoherente entra a la base, venga de donde venga |
| `@AssertTrue` en `ServicioRequest` | El usuario recibe un mensaje claro en vez de un error 500 de la base |

## Archivos

| Archivo | Quién |
|---|---|
| `V7__servicio.sql` (tabla + los cinco servicios) | Generado |
| `UnidadCobro`, `Servicio` | Tú |
| `ServicioRepository`, `ServicioService`, `ServicioController` | Tú |
| `ServicioRequest`, `ServicioResponse`, `ServicioAdminResponse`, `EstadoRequest` | Tú |
| `ServicioYaExisteException` + handler 409 | Tú |
| Lista blanca de `ConfiguracionSeguridad` (solo GET) | Tú |
| `FormularioServicio`, `Insignia`, `CatalogoPage`, `api/formato.js`, `separarErrores` | Generados |
| `ServiciosAdminPage`, `ServicioFormPage` | Tú |

## Decisiones

- **Dos DTO de salida para una entidad.** El público no expone `activo`; el del panel sí. La
  visibilidad de un campo es parte del contrato de cada audiencia.
- **Dos reglas de búsqueda.** El catálogo usa `findByIdAndActivoTrue`; el panel usa `findById`,
  porque el administrador tiene que poder editar y reactivar lo desactivado.
- **`permitAll` con `HttpMethod.GET`.** Abrir `/api/v1/servicios/**` sin indicar el método habría
  dejado públicos el POST, el PUT y el PATCH. El `@PreAuthorize` seguiría protegiéndolos, pero la
  defensa no debe depender de una sola capa.
- **`PATCH /{id}/estado` en lugar de `DELETE`.** También sirve para reactivar, y el nombre dice la
  verdad: cambia un estado, no borra.
- **`existsByNombreIgnoreCaseAndIdNot`** al editar: sin `AndIdNot`, guardar un servicio sin cambiarle
  el nombre chocaría con su propia fila.
- **Errores que no son de un campo.** `@AssertTrue` llega con el nombre del método
  (`duracionCoherenteConLaUnidad`). `separarErrores` los envía a la alerta general para que no se
  pierdan silenciosamente.

## Comprobación

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | `GET /servicios` sin token | `200` con los activos, sin el campo `activo` |
| 2 | `GET /servicios/gestion` con CLIENTE | `403` · `ACCESO_DENEGADO` |
| 3 | `GET /servicios/gestion` sin token | `401` |
| 4 | `POST` con nombre repetido (otras mayúsculas) | `409` · `SERVICIO_YA_EXISTE` |
| 5 | `POR_DIA` con duración, o `POR_SERVICIO` sin ella | `400` con el mensaje de coherencia |
| 6 | `PATCH /{id}/estado` con `false` | `200`, y desaparece del catálogo público |
| 7 | La fila sigue en la base | `activo = 0`, nada se borró |
| 8 | Interfaz: "Gestión de servicios" en la barra | Solo con `SERVICIO_GESTIONAR` |
| 9 | Interfaz: elegir "Por día" | El campo de duración desaparece |
| 10 | Móvil de 375 px | Sin scroll horizontal |

## Cerrar

```bash
git add .
git commit -m "HU-027, HU-028 y HU-029: catalogo publico y panel de servicios"
```

**Siguiente:** Sprint 3 — empleados (HU-020), jornada laboral (HU-022) y servicios por empleado
(HU-021), que son la base del motor de disponibilidad del Sprint 4.
