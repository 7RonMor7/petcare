# Guía de implementación — HU-007: Registro de cliente

**Sprint 1 · 3 SP · ~4,3 h · IRR-01**

> **Como** visitante, **quiero** registrarme con mis datos y aceptar la política de tratamiento, **para** poder reservar servicios para mis mascotas.

Trabaja los pasos en orden. Después de cada uno hay una comprobación: si no pasa, no sigas al siguiente.

---

## Lo que vamos a construir

```
POST /api/v1/auth/registro
        │
        ▼
  AuthController        recibe JSON, valida formato, devuelve 201
        │
        ▼
  RegistroService       reglas: correo único, BCrypt, rol CLIENTE
        │
        ▼
  UsuarioRepository     guarda
        │
        ▼
  MySQL                 tablas usuario / rol / usuario_rol
```

Cinco archivos nuevos de dominio, tres de soporte, una migración.

---

## Paso 1 — La migración `V2` *(ya está en el proyecto)*

`backend/src/main/resources/db/migration/V2__usuario_rol.sql`

Crea `rol`, `usuario` y `usuario_rol`, e inserta los tres roles.

**Comprobar:** arranca el backend y mira los logs.

```
Migrating schema `petcare` to version "2 - usuario rol"
```

Y luego:

```bash
curl http://localhost:8080/api/v1/ping/db
# "migracionesAplicadas": 2
```

Si dice 1, Flyway no vio el archivo: revisa el nombre exacto (dos guiones bajos entre la versión y el nombre).

---

## Paso 2 — Dependencia para BCrypt

`backend/pom.xml`, dentro de `<dependencies>`:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

Sin versión: la gestiona el `spring-boot-starter-parent`.

**Por qué esta y no `spring-boot-starter-security`:** el starter completo trae autoconfiguración y deja *todos* los endpoints detrás de un login automático. Hoy no queremos eso. `spring-security-crypto` es solo la librería de hashing, sin autoconfiguración. El starter entra en HU-008.

---

## Paso 3 — Entidades `Rol` y `Usuario`

`backend/src/main/java/com/petcare/usuarios/domain/Rol.java`
`backend/src/main/java/com/petcare/usuarios/domain/Usuario.java`

Una entidad es la representación en Java de una fila de la tabla. `@Entity` le dice a JPA que esta clase se mapea a una tabla, y `@Table` cuál.

**Tres decisiones que conviene entender:**

- **No usamos `@Data` de Lombok en entidades.** Genera `equals` y `hashCode` con todos los campos, y eso rompe con Hibernate: dos objetos que representan la misma fila pueden dar `false`, y un proxy perezoso puede disparar consultas inesperadas al compararse. Usamos `@Getter` y nada más.
- **Constructor protegido sin argumentos.** JPA lo exige para instanciar la entidad al leer de la base. `protected` impide que el resto del código cree usuarios vacíos: para eso está el constructor real.
- **`roles` con `FetchType.EAGER`.** Normalmente EAGER se desaconseja, pero aquí el conjunto tiene uno o dos elementos y siempre lo necesitamos (para autorizar). Con LAZY tendríamos `LazyInitializationException` al construir la respuesta fuera de la transacción.

**Comprobar:** arranca. Si una columna de la entidad no coincide con la tabla, Hibernate falla con `Schema-validation: missing column`. Que arranque significa que entidad y tabla cuadran — ese es el trabajo de `ddl-auto: validate`.

---

## Paso 4 — Repositorios

`com/petcare/usuarios/repository/UsuarioRepository.java`
`com/petcare/usuarios/repository/RolRepository.java`

Interfaces que extienden `JpaRepository`. No se implementan: Spring Data genera la implementación en tiempo de arranque leyendo **el nombre del método**. `existsByCorreoIgnoreCase` se traduce a `SELECT COUNT(*) ... WHERE LOWER(correo) = LOWER(?)`.

Si te equivocas en el nombre de una propiedad, el fallo aparece **al arrancar**, no al llamar al método. Es de los pocos sitios donde Spring te avisa temprano.

---

## Paso 5 — DTOs

`com/petcare/auth/dto/RegistroClienteRequest.java`
`com/petcare/auth/dto/UsuarioResponse.java`

**La pregunta importante: ¿por qué no devolver la entidad directamente?**

Cuatro razones, y la primera es suficiente:

1. **`Usuario` tiene `contrasenaHash`.** Si devuelves la entidad, Jackson serializa todos sus campos y el hash sale en la respuesta HTTP.
2. Lo que **entra** y lo que **sale** son distintos: entra una contraseña en claro, sale un id. No son la misma forma.
3. La entidad refleja la base de datos. Si mañana renombras una columna, tu API no debería cambiar con ella.
4. Las validaciones de entrada son del DTO, no de la tabla.

Son `record` porque un DTO es un contenedor inmutable de datos: el `record` te da constructor, getters, `equals` y `toString` en una línea.

---

## Paso 6 — `PasswordEncoder`

`com/petcare/common/ConfiguracionSeguridad.java`

Un `@Bean` que expone `BCryptPasswordEncoder`.

**BCrypt es deliberadamente lento** (~100 ms por hash). Eso es la característica, no un defecto: un atacante con la base de datos robada no puede probar millones de contraseñas por segundo. El "factor de coste" controla cuánto: el predeterminado es 10, y RNF01-R exige ≥ 10.

Además incorpora una *sal* aleatoria en cada hash. Por eso la misma contraseña genera hashes distintos cada vez, y por eso no se puede usar una tabla precalculada para romperlos.

Se declara como bean y no se instancia con `new` en el servicio para poder cambiarlo en un solo sitio, y para poder sustituirlo por uno falso en las pruebas.

---

## Paso 7 — Excepción y manejador de errores

`com/petcare/common/error/CorreoYaRegistradoException.java`
`com/petcare/common/error/RespuestaError.java`
`com/petcare/common/error/ManejadorErrores.java`

Sin el `@RestControllerAdvice`, una validación fallida devuelve el error genérico de Spring, con una estructura distinta a la de tus errores de negocio. El frontend tendría que saber distinguir dos formatos.

Con él, **todos** los errores de la API salen con la misma forma, y el frontend escribe el manejo una sola vez.

Un detalle que importa: aquí el mensaje dice *"ya existe una cuenta con ese correo"*, lo cual confirma que el correo existe. En el registro es inevitable — hay que decírselo al usuario. **En el login haremos lo contrario**: un mensaje genérico, precisamente para no revelar qué correos están registrados.

---

## Paso 8 — Controlador

`com/petcare/auth/controller/AuthController.java`

Fino a propósito: recibe, delega, responde. Sin lógica de negocio.

`@Valid` es lo que dispara las validaciones del DTO. **Sin esa anotación las anotaciones del record se ignoran en silencio** — es el olvido más común de Spring y no da ningún aviso.

---

## Comprobación final

Crea `backend/registro.json`:

```json
{
  "nombre": "Ana",
  "apellido": "Restrepo",
  "correo": "ana@ejemplo.com",
  "telefono": "3001234567",
  "contrasena": "Segura123",
  "aceptaPoliticaDatos": true
}
```

| # | Comando | Esperado |
|:--:|---|---|
| 1 | `curl -i -X POST localhost:8080/api/v1/auth/registro -H "Content-Type: application/json" -d @registro.json` | `201` con id y rol CLIENTE, **sin** contraseña |
| 2 | Repetir el mismo comando | `409` · `CORREO_YA_REGISTRADO` |
| 3 | Cambiar el correo y poner `"contrasena": "123"` | `400` · detalle del campo `contrasena` |
| 4 | Cambiar el correo y poner `"aceptaPoliticaDatos": false` | `400` |
| 5 | `docker exec -it petcare-mysql mysql -upetcare -ppetcare petcare -e "SELECT correo, contrasena_hash FROM usuario;"` | El hash empieza por `$2a$10$` y no se parece a la contraseña |

El paso 5 es el que cierra RNF01-R: **verificar con tus ojos que la contraseña no está en texto plano.**

---

## Cerrar la historia

```bash
git add .
git commit -m "HU-007: registro de cliente con BCrypt y validaciones"
```

Anota en tu hoja de velocidad las horas reales frente a las 4,3 estimadas. Esa cifra es la que alimenta el punto de control del Sprint 2.

**Siguiente:** HU-008 — login con JWT.
