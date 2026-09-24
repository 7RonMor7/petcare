# PetCare

Plataforma web de reservas y pagos para servicios de mascotas: paseo, baño, peluquería, consulta veterinaria y hospedaje.

Proyecto académico de desarrollo de software. Java + Spring Boot en el backend, React en el frontend, MySQL como base de datos.

---

## Stack

| Capa | Tecnologías |
|---|---|
| Backend | Java 17 · Spring Boot 3.4 · Spring Web · Spring Data JPA · Bean Validation · Spring Security · JJWT · Flyway · Maven · Lombok |
| Frontend | React 18 · Vite 6 · Tailwind CSS 4 · Axios · React Router 6 |
| Base de datos | MySQL 8.4 (en Docker) |
| Herramientas | Git · Docker · IntelliJ IDEA · VS Code |

---

## Estructura

```
PetCare/
├── backend/            Spring Boot
│   └── src/main/
│       ├── java/com/petcare/
│       │   ├── auth/         registro, login, refresco, logout, filtro JWT
│       │   ├── usuarios/     Usuario, Rol, Permiso
│       │   ├── mascotas/     HU-016 y HU-017
│       │   └── common/       CORS, seguridad y manejo de errores
│       └── resources/
│           ├── application.yml
│           └── db/migration/    ← el esquema vive aquí (Flyway)
├── frontend/           React + Vite
│   └── src/
│       ├── api/        cliente axios, interceptores y traducción de errores
│       ├── auth/       contexto de sesión y rutas protegidas
│       ├── components/ piezas reutilizables de interfaz
│       └── pages/      una pantalla por archivo
├── database/           notas locales (el esquema NO está aquí)
├── docs/               análisis, backlog, historias y guías por HU
├── docker-compose.yml  MySQL
└── .env                configuración local (no se sube a Git)
```

---

## Arrancar el proyecto

Necesitas Java 17, Maven, Node 20+, Docker y Git.

### 1. Configuración local

Copia `.env.example` a `.env` en la raíz. El puerto de MySQL se define ahí con `DB_PORT`
(por defecto 3306; en este equipo se usa **3307** porque el 3306 está ocupado).

El frontend tiene su **propio** archivo de configuración: copia `frontend/.env.example`
a `frontend/.env`. Sin `VITE_API_URL`, axios usa como base la propia página y todas las
peticiones terminan en el servidor de Vite en lugar del backend.

```
# frontend/.env
VITE_API_URL=http://localhost:8080/api/v1
```

Vite lee ese archivo **solo al arrancar**: después de cambiarlo hay que reiniciar `npm run dev`.

### 2. Base de datos

```bash
docker compose up -d
docker compose ps          # espera a que aparezca "healthy"
```

Levanta MySQL 8.4 con la base `petcare`. Los datos persisten en el volumen
`petcare-mysql-data`, así que puedes apagar el contenedor sin perderlos.

### 3. Backend

```bash
cd backend
mvn spring-boot:run
```

Arranca en `http://localhost:8080`. Al arrancar, Flyway aplica las migraciones de
`db/migration` y deja constancia en `flyway_schema_history`.

### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

Abre `http://localhost:5173`. Sin sesión iniciada, la aplicación lleva al login.

---

## Autenticación y autorización

- **Access token JWT** de 15 minutos, sin estado. Lleva el id del usuario, sus roles y sus permisos.
- **Refresh token** opaco de 7 días, guardado en la base **con hash SHA-256** y rotado en cada uso.
  Si un token ya usado vuelve a aparecer, se revoca toda su familia (detección de reutilización).
- **Autorización por permiso, no por rol:** los endpoints exigen `hasAuthority('MASCOTA_CREAR')`.
  El rol es solo un paquete de permisos, y se ajusta con filas en `rol_permiso`.
- **En el navegador**, el access token vive en memoria y el refresh en `localStorage`. El interceptor
  de axios renueva la sesión al recibir un 401 y reintenta la petición una sola vez.

Los tres roles son CLIENTE, EMPLEADO y ADMINISTRADOR. `POST /auth/registro` siempre crea un CLIENTE.

---

## Endpoints

| Método | Ruta | Permiso | Historia |
|---|---|---|---|
| POST | `/api/v1/auth/registro` | público | HU-007 |
| POST | `/api/v1/auth/login` | público | HU-008 |
| POST | `/api/v1/auth/refresh` | público (con refresh token) | HU-008 |
| GET | `/api/v1/auth/yo` | autenticado | HU-009 |
| POST | `/api/v1/auth/logout` | autenticado | HU-010 |
| POST | `/api/v1/mascotas` | `MASCOTA_CREAR` | HU-016 |
| GET | `/api/v1/mascotas` | `MASCOTA_LEER_PROPIA` | HU-017 |
| GET | `/api/v1/mascotas/{id}` | `MASCOTA_LEER_PROPIA` | HU-017 |
| PUT | `/api/v1/mascotas/{id}` | `MASCOTA_EDITAR_PROPIA` | HU-017 |
| DELETE | `/api/v1/mascotas/{id}` | `MASCOTA_ELIMINAR_PROPIA` | HU-017 |

Todos los errores usan el mismo formato:

```json
{
  "marcaTiempo": "2026-09-23T22:00:00Z",
  "estado": 400,
  "codigo": "VALIDACION_FALLIDA",
  "mensaje": "La solicitud contiene campos inválidos",
  "ruta": "/api/v1/mascotas",
  "detalles": [{ "campo": "pesoKg", "mensaje": "debe ser menor o igual que 200.00" }]
}
```

Códigos en uso: `VALIDACION_FALLIDA`, `CUERPO_INVALIDO`, `CORREO_YA_REGISTRADO`,
`CREDENCIALES_INVALIDAS`, `TOKEN_INVALIDO`, `NO_AUTENTICADO`, `ACCESO_DENEGADO`,
`RECURSO_NO_ENCONTRADO`.

**404 y no 403 en los recursos ajenos:** si una mascota no es tuya, la respuesta es la misma que si
no existiera. Un 403 confirmaría que ese id existe.

---

## Verificar que todo funciona

```bash
curl http://localhost:8080/api/v1/ping
# {"servicio":"petcare-backend","estado":"arriba","marcaTiempo":"..."}

curl http://localhost:8080/api/v1/ping/db
# {"conexion":"ok","motor":"8.4.x","migracionesAplicadas":6}

curl http://localhost:8080/actuator/health
# {"status":"UP", ...}
```

Desde la aplicación: regístrate, inicia sesión, registra una mascota y vuelve a cargar con F5.
Si la sesión sobrevive a la recarga, funcionan el interceptor y la renovación del token.

`/api/v1/ping`, `/api/v1/ping/db` y `/api/v1/ping/admin` son temporales: existen para verificar el
entorno y se borran al cerrar el Sprint 1.

---

## Pruebas

```bash
cd backend
mvn test                 # requiere MySQL corriendo
mvn package -DskipTests  # si no quieres levantar la base de datos
```

---

## Convenciones

**Nombres.** El dominio va en español (`Reserva`, `fechaNacimiento`, `/api/v1/mascotas`, `PENDIENTE_PAGO`); lo técnico del framework, en inglés (`@Service`, `Repository`, `DTO`). El negocio se habla en español y traducirlo introduce ambigüedad.

**Esquema de base de datos.** Solo se toca mediante migraciones de Flyway, nunca a mano ni con `ddl-auto: update`. Cada cambio es un archivo nuevo `V2__...sql`, `V3__...sql`. Un archivo ya aplicado no se edita: se corrige con otro nuevo.

**Propiedad de los datos.** El dueño de un recurso sale siempre del token, nunca del cuerpo de la petición, y las consultas filtran por él (`findByIdAndClienteId...`) en lugar de comprobarlo con un `if`.

**Borrado.** Lógico: `activo = false`. Una mascota con reservas y pagos no se elimina de la base.

**Fechas.** Todo se guarda en UTC. La conversión a `America/Bogota` ocurre solo al mostrar.

**Ramas.** Una por historia: `feat/HU-007-registro-cliente`. Commits que digan qué hacen: `HU-007: validar unicidad del correo en el registro`.

**Secretos.** Nunca en el repositorio. `.env` está en `.gitignore`; `.env.example` documenta qué variables hacen falta.

---

## Estado

| Sprint | Contenido | Estado |
|---|---|:--:|
| 0 | Entorno, estructura y repositorio | Completado |
| 1 | Registro, login, autorización, logout, mascotas | En curso |

Historias cerradas: **HU-007** (registro), **HU-008** (login con JWT), **HU-009** (autorización por
permisos), **HU-010** (logout con revocación), **HU-016** (registrar mascota), **HU-017** (listar,
editar y eliminar mascotas propias).

El análisis, el backlog y las guías de cada historia están en `docs/`.
