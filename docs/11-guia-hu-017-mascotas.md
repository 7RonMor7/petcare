# Guía de implementación — HU-017: Listar, editar y eliminar mis mascotas

**Sprint 2 · 2 SP · IRR-03 · RB01**

> **Como** cliente, **quiero** listar, editar y eliminar mis mascotas, **para** mantener su
> información al día, **y** que el sistema impida operar sobre mascotas ajenas.

---

## La decisión central: el dueño va en el `WHERE`

```java
// Frágil: si algún día falta el if, se filtran datos ajenos
Mascota m = repo.findById(id).orElseThrow();
if (!m.getCliente().getId().equals(clienteId)) throw new AccesoDenegado();

// Sólido: la consulta no puede devolver una mascota ajena
repo.findByIdAndClienteIdAndActivoTrue(id, clienteId).orElseThrow(...);
```

Se usa la segunda forma en los cuatro endpoints. El olvido deja de ser posible.

**Y la respuesta es `404`, no `403`.** Un 403 sobre la mascota de otro confirmaría que ese id
existe. Con 404 no se distingue "no existe" de "no es tuya"; el mismo criterio del login.

## Archivos

| Archivo | Cambio | Quién |
|---|---|---|
| `MascotaRepository` | `findByClienteIdAndActivoTrueOrderByNombreAsc`, `findByIdAndClienteIdAndActivoTrue` | Tú |
| `Mascota` | `actualizarDatos(...)` y `desactivar()` | Tú |
| `RecursoNoEncontradoException` + handler 404 | Nuevo código `RECURSO_NO_ENCONTRADO` | Tú |
| `MascotaService` | `listar`, `obtener`, `actualizar`, `eliminar`, `buscarPropia` | Tú |
| `MascotaController` | GET, GET/{id}, PUT/{id}, DELETE/{id} | Tú |
| `FormularioMascota.jsx`, `DialogoConfirmacion.jsx` | Campos compartidos y confirmación de borrado | Generados |
| `MascotasPage.jsx`, `MascotaEditarPage.jsx` | Lista y edición | Tú |
| `MascotaNuevaPage.jsx` | Reescrita sobre `FormularioMascota` | Tú |

## Decisiones

- **Sin migración.** La columna `activo` existe desde V6.
- **Borrado lógico.** `DELETE` responde `204` y pone `activo = false`. Una mascota con reservas y
  pagos no puede desaparecer de la base.
- **Sin `save()` al editar.** Dentro de la transacción la entidad está gestionada e Hibernate
  genera el `UPDATE` por *dirty checking*.
- **`readOnly = true`** en las lecturas: Hibernate se ahorra el chequeo de cambios.
- **Tres permisos distintos** (leer, editar, eliminar): cambiar quién puede qué es una fila en
  `rol_permiso`, no un cambio de código.
- **`PUT` reutiliza `MascotaRequest`**, que sigue sin `clienteId`: editar no puede cambiar de dueño.
- **En el frontend, un solo formulario** para registrar y editar. La pantalla decide el verbo HTTP
  y el texto del botón.

## Comprobación

Requiere **dos cuentas CLIENTE**. El ADMINISTRADOR no sirve: no tiene `MASCOTA_LEER_PROPIA`,
así que recibiría `403` en lugar de `404`.

| # | Prueba | Esperado |
|:--:|---|---|
| 1 | `GET /mascotas` con Ana | `200` con sus mascotas activas |
| 2 | `GET /mascotas/{id}` propia | `200` |
| 3 | `PUT /mascotas/{id}` con el peso cambiado | `200` y el cambio persiste |
| 4 | `GET /mascotas` con el otro cliente | `200` con `[]` |
| 5 | `GET`, `PUT` y `DELETE` de la mascota de Ana con el otro cliente | `404` · `RECURSO_NO_ENCONTRADO` |
| 6 | `DELETE` propia | `204`, y deja de aparecer en la lista |
| 7 | `SELECT id, nombre, activo FROM mascota;` | La fila sigue existiendo con `activo = 0` |
| 8 | Repetir el `DELETE` | `404` |
| 9 | Interfaz: lista vacía | Mensaje y enlace para registrar la primera |
| 10 | Interfaz: eliminar | Pide confirmación; con Escape se cancela |

## Cerrar

```bash
git add .
git commit -m "HU-017: gestion de mascotas propias con validacion de propiedad"
```

**Siguiente:** HU-018 y el catálogo de servicios (Sprint 2).
