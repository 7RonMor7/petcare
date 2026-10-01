# Guía de implementación — Sprint 4: el motor de disponibilidad

**HU-030 (3 SP) · HU-031 (5 SP) · HU-024 (2 SP) · HU-038 (3 SP) — IRR-23, IRR-06**

> **HU-030.** Generar las franjas horarias de cada empleado a partir de su jornada laboral.
> **HU-031.** Consultar las franjas disponibles de un servicio en una fecha, descontando reservas y bloqueos.
> **HU-024.** Como empleado, bloquear franjas de mi agenda (RB13).
> **HU-038.** Como cliente, elegir empleado o indicar "sin preferencia" (DEC-C24).

El sprint de mayor riesgo técnico del proyecto. Se abordó en cuatro pasos, del más aislado al más
integrado: primero el algoritmo puro con pruebas, después la consulta real, luego los bloqueos y
por último la asignación.

---

## El algoritmo

```
GET /disponibilidad?servicioId=3&fecha=2026-10-05[&empleadoId=9]
   │
   ├─ 1. Servicio activo y POR_SERVICIO              → 404 / 422
   ├─ 2. Fecha dentro de la ventana permitida        → 422
   ├─ 3. ¿QUIÉN sabe hacerlo?   empleado_servicio    (HU-021)
   ├─ 4. ¿CUÁNDO trabaja?       jornada_laboral      (HU-022)
   ├─ 5. Generar franjas        GeneradorFranjas     (HU-030)
   ├─ 6. ¿QUÉ tiene ocupado?    reservas activas + bloqueos
   ├─ 7. Quitar solapadas
   ├─ 8. Quitar las que incumplen la antelación mínima
   └─ 9. Agrupar por hora → cada franja con sus empleados disponibles
```

**La regla de solape, una sola vez y en dos idiomas:** `inicio < otroFin && otroInicio < fin`, en
Java (`Franja.seSolapaCon`) y en JPQL (`fechaHoraFin > :desde AND fechaHoraInicio < :hasta`).
Aparece en el motor, en los bloqueos y reaparecerá en la creación de reservas.

## Archivos

| Archivo | Quién |
|---|---|
| `V10__bloqueo_agenda.sql` (+ parámetro `zona_horaria`), `V11__reserva.sql` | Generados |
| `Franja`, `GeneradorFranjas`, `AsignadorEmpleado` (lógica pura) | Tú |
| `GeneradorFranjasTest`, `AsignadorEmpleadoTest` (11 pruebas) | Tú |
| `BloqueoAgenda`, `Reserva`, `EstadoReserva` | Tú |
| `BloqueoRepository`, `ReservaRepository`, consultas de ocupación y carga | Tú |
| `DisponibilidadService`, `BloqueoService` | Tú |
| `DisponibilidadController`, `MiAgendaController` | Tú |
| `ConflictoException` + handler 409 | Tú |

## Decisiones

- **La tabla `reserva` se crea un sprint antes de poder crear reservas.** El motor no puede
  descontar lo que no existe. La entidad es de solo lectura hasta HU-032.
- **Lógica pura separada del framework.** `GeneradorFranjas` y `AsignadorEmpleado` no conocen
  Spring ni la base: se prueban en milisegundos y se razona sobre ellas sin levantar nada.
- **La rejilla y el redondeo vienen de `parametro_sistema`.** PRG-70 (veterinaria de 45 min
  ocupando 60) es reversible con un `UPDATE`, y hay una prueba que lo demuestra.
- **Zona horaria explícita.** La jornada se define en hora local y los instantes se guardan en UTC;
  la conversión ocurre en la frontera (DTO ↔ servicio) con `zona_horaria`. Comparar sin convertir
  no falla: simplemente da resultados equivocados cinco horas corridos.
- **`Franja` es un `record`**, así que sirve de clave de mapa por valor: dos empleados con la misma
  franja se agrupan solos.
- **Tres consultas con `IN`**, no una por empleado: el N+1 se nota ya con cinco empleados.
- **409 frente a 422.** `BLOQUEO_CON_RESERVAS` y `FRANJA_NO_DISPONIBLE` son conflictos con el
  estado actual (mañana la misma petición podría valer); `JORNADA_SOLAPADA` o `FECHA_FUERA_DE_RANGO`
  son reglas que nunca se cumplirían.
- **El mensaje de RB13 dice qué reservas estorban** y a qué hora, en lugar de un "no se puede".
- **`/mi-agenda` en vez de `/empleados/{id}`:** el dueño sale del token; un empleado no puede
  siquiera nombrar a otro.
- **Asignación automática: el menos ocupado del día, desempate por id menor.** Estable, explicable
  y probable; al azar no se puede probar ni justificar ante el cliente.
- **Los bloqueos se borran de verdad.** No tienen historial contable ni nadie que los referencie;
  el borrado lógico se reserva para lo que sí lo tiene.

## Comprobación

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | `mvn test` | 11 pruebas unitarias en verde, sin levantar Spring |
| 2 | Romper el borde del bucle o el redondeo a propósito | Las pruebas se ponen rojas |
| 3 | Disponibilidad de un día con reserva y bloqueo | Faltan las franjas que los pisan |
| 4 | Cancelar la reserva | Vuelven esas franjas |
| 5 | Día sin jornada | `[]` |
| 6 | Fecha pasada o a más de 30 días | `422` · `FECHA_FUERA_DE_RANGO` |
| 7 | Hoy | No aparecen franjas a menos de 2 horas |
| 8 | Servicio `POR_DIA` | `422` · `SERVICIO_SIN_AGENDA` |
| 9 | `tamano_franja_minutos = 15` | Aparecen franjas intermedias (PRG-70 reversible) |
| 10 | Bloquear sobre una reserva activa | `409` · `BLOQUEO_CON_RESERVAS`, con las horas |
| 11 | Borrar el bloqueo de otro empleado | `404` |
| 12 | `/disponibilidad/asignacion` repetido | Siempre el mismo empleado |
| 13 | Añadir carga al elegido y repetir | Asigna al otro |

## Lección del sprint

El fallo más costoso de la comprobación no estuvo en el código sino en un dato de prueba: una
reserva insertada con hora local en una columna UTC hizo que dos pasos "pasaran" por la razón
equivocada. Cuando una prueba depende de datos insertados a mano, **el dato también hay que
verificarlo**; lo que entra por la API se convierte solo, lo que entra por SQL no.

## Deuda anotada

- `LocalTime.plusMinutes` da la vuelta a medianoche: con turnos nocturnos, el bucle del generador
  no terminaría. Hoy lo impide `jornada_hora_cierre`.
- Al reemplazar la jornada no se revisan las reservas que queden fuera del nuevo horario.
- La asignación automática no considera preferencias del cliente ni continuidad ("el mismo
  empleado de la vez pasada").

## Cerrar

```bash
git add .
git commit -m "Sprint 4: motor de disponibilidad, bloqueos de agenda y asignacion de empleado"
```

**Siguiente:** Sprint 5 — crear reservas (HU-032), la restricción de concurrencia en base de datos
(HU-033) y la máquina de estados (HU-035).
