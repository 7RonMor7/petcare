# Guía de implementación — HU-020, HU-022 y HU-021: empleados, jornada y servicios

**Sprint 3 · 3 + 3 + 2 SP · IRR-15, IRR-23**

> **HU-020.** Como administrador, quiero crear, editar y desactivar empleados desde el panel.
> **HU-022.** Como administrador, quiero definir la jornada laboral semanal de cada empleado.
> **HU-021.** Como administrador, quiero asignar a cada empleado los servicios que puede prestar.

Las tres juntas son los tres ladrillos del motor de disponibilidad del Sprint 4:
**quién existe**, **cuándo trabaja** y **qué sabe hacer**.

---

## Las decisiones de modelo

**Un empleado es un `usuario` con rol EMPLEADO.** No hay tabla `empleado`: crearla duplicaría
nombre, correo y contraseña, y obligaría a mantener dos identidades de la misma persona. Lo que sí
tiene tabla propia es lo que *solo* pertenece al empleado: su jornada y sus servicios, ambas
apuntando a `usuario(id)`.

**`usuario.activo` ya existía y el login ya lo comprueba**, así que desactivar a un empleado le
impide entrar sin escribir una línea más (con el margen de los 15 minutos del access token).

**La jornada se modela como tramos**, no como "hora de entrada y hora de salida". Sin tramos no se
puede expresar el almuerzo, y el motor ofrecería horas en las que no hay nadie.

**Los días usan `java.time.DayOfWeek` (`MONDAY`…).** Es la única excepción a "el dominio en
español": en el Sprint 4 se hará `fecha.getDayOfWeek()`, y una tabla de conversión propia sería
justo donde aparecerían los desfases.

## Archivos

| Archivo | Quién |
|---|---|
| `V8__jornada_laboral.sql`, `V9__empleado_servicio.sql` | Generados |
| `EmpleadoRequest`, `EmpleadoActualizarRequest`, `EmpleadoResponse` | Tú |
| `EmpleadoService`, `EmpleadoController` | Tú |
| `ParametroSistema`, `ParametroRepository`, `ParametroService` | Tú |
| `JornadaLaboral`, `JornadaRepository`, `JornadaService`, DTOs de tramo | Tú |
| `EmpleadoServicio`, `EmpleadoServicioRepository`, `EmpleadoServicioService` | Tú |
| `ReglaNegocioException` + handler 422 | Tú |
| `EditorJornada.jsx`, `SelectorServicios.jsx` | Generados |
| `EmpleadosAdminPage`, `EmpleadoFormPage`, `EmpleadoDetallePage` | Tú |

## Decisiones de diseño

- **`buscarEmpleado` filtra por rol.** Sin ese filtro, `PATCH /empleados/3/estado` podría desactivar
  a un cliente o al propio administrador cambiando el id. El endpoint dice "empleados" y el código
  tiene que hacerlo cierto.
- **`ReglaNegocioException` → `422`.** El `400` es "no entiendo lo que mandaste"; el `422` es
  "lo entiendo, cada campo es válido, pero juntos rompen una regla". El frontend puede distinguir
  "corrige este campo" de "esta combinación no se permite". Se reutilizará en reservas y pagos.
- **Reemplazo completo (`PUT`) para jornada y servicios.** La pantalla edita la semana o el conjunto
  entero; con altas y bajas sueltas, un guardado a medias dejaría media jornada y validar solapes
  exigiría releer lo ya guardado. Enviar una lista vacía es válido y significa "nada".
- **Los solapes se validan en el servicio, no en la base.** Un `CHECK` compara columnas de una fila;
  el solape compara filas entre sí. La base sí impide el duplicado exacto (`UNIQUE`).
- **Las reglas leen `parametro_sistema`.** Cambiar la rejilla de 30 a 15 minutos (PRG-70) es un
  `UPDATE`, no un despliegue. Con valor por defecto en el código por si falta la fila.
- **`findAllById` + `distinct()`** al asignar servicios: una sola consulta, y `[3, 3]` no se
  confunde con "un id que no existe".
- **Un servicio inactivo no se puede asignar** (`422 SERVICIO_INACTIVO`): existe, pero la regla lo
  impide.
- **`key` en `EditorJornada` y `SelectorServicios`:** `valorInicial` solo se lee al montar, así que
  cambiar el `key` es lo que refresca el componente con lo recién guardado.

## Endpoints

| Método | Ruta | Permiso |
|---|---|---|
| GET/POST | `/api/v1/empleados` | `EMPLEADO_GESTIONAR` |
| PUT | `/api/v1/empleados/{id}` | `EMPLEADO_GESTIONAR` |
| PATCH | `/api/v1/empleados/{id}/estado` | `EMPLEADO_GESTIONAR` |
| GET/PUT | `/api/v1/empleados/{id}/jornada` | `EMPLEADO_GESTIONAR` |
| GET/PUT | `/api/v1/empleados/{id}/servicios` | `EMPLEADO_GESTIONAR` |

## Comprobación

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | Crear empleado con correo repetido | `409` · `CORREO_YA_REGISTRADO` |
| 2 | Desactivar y volver a iniciar sesión con él | `401` · `CREDENCIALES_INVALIDAS` |
| 3 | `PATCH /empleados/{idDeUnCliente}/estado` | `404` |
| 4 | Jornada con tramos solapados | `422` · `JORNADA_SOLAPADA` |
| 5 | Jornada 08:10–12:00 | `422` · `JORNADA_DESALINEADA` |
| 6 | Jornada 07:00–12:00 | `422` · `JORNADA_FUERA_DE_HORARIO` |
| 7 | Jornada 08:00–12:00 y 12:00–15:00 | `200` (tramos pegados, sin solape) |
| 8 | Servicios con id repetido | `200`, una sola asignación |
| 9 | Servicios con el hospedaje (inactivo) | `422` · `SERVICIO_INACTIVO` |
| 10 | Todo lo anterior con un CLIENTE | `403` |

## Deuda anotada

- Al reemplazar la jornada no se comprueban las reservas ya creadas que queden fuera del nuevo
  horario. Se decidirá en HU-030, cuando exista la tabla `reserva`.
- El empleado todavía no puede ver su propia jornada: los endpoints son del administrador. Llega en
  HU-023 con `AGENDA_LEER_PROPIA`.
- La contraseña temporal no obliga a cambiarla en el primer inicio de sesión.

## Cerrar

```bash
git add .
git commit -m "HU-020, HU-021 y HU-022: empleados, jornada laboral y servicios por empleado"
```

**Siguiente:** Sprint 4 — HU-030 (generar las franjas a partir de la jornada) y HU-031 (consultar
disponibilidad real descontando reservas y bloqueos). Es el sprint de mayor riesgo técnico.
