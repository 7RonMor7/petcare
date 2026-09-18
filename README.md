# PetCare

Plataforma web de reservas y pagos para servicios de mascotas: paseo, baño, peluquería, consulta veterinaria y hospedaje.

Proyecto académico de desarrollo de software. Java + Spring Boot en el backend, React en el frontend, MySQL como base de datos.

---

## Stack

| Capa | Tecnologías |
|---|---|
| Backend | Java 17 · Spring Boot 3.4 · Spring Web · Spring Data JPA · Bean Validation · Flyway · Maven · Lombok |
| Frontend | React 18 · Vite · Tailwind CSS 4 · Axios · React Router |
| Base de datos | MySQL 8.4 (en Docker) |
| Herramientas | Git · Docker · IntelliJ IDEA · VS Code |

Spring Security y JWT entran en el Sprint 1, cuando toque el registro y el login. No están todavía: añadir `spring-boot-starter-security` deja todos los endpoints detrás de un login automático, y eso confunde más de lo que ayuda mientras no hay usuarios.
> En este equipo, MySQL está publicado en el puerto 3307 (el 3306 está ocupado).

---

## Estructura

```
PetCare/
├── backend/            Spring Boot
│   └── src/main/
│       ├── java/com/petcare/
│       └── resources/
│           ├── application.yml
│           └── db/migration/    ← el esquema vive aquí (Flyway)
├── frontend/           React + Vite
│   └── src/
├── database/           notas locales (el esquema NO está aquí)
├── docs/               análisis, backlog e historias
├── docker-compose.yml  MySQL
└── .env                configuración local (no se sube a Git)
```

---

## Arrancar el proyecto

Necesitas Java 17, Maven, Node 20+, Docker y Git.

### 1. Base de datos

```bash
docker compose up -d
docker compose ps          # espera a que aparezca "healthy"
```

Levanta MySQL 8.4 en el puerto 3306 con la base `petcare`, usuario `petcare` y contraseña `petcare`. Los datos persisten en el volumen `petcare-mysql-data`, así que puedes apagar el contenedor sin perderlos.

> En local MySQL está en 3307

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

Arranca en `http://localhost:8080`. La primera vez, Maven descarga dependencias y tarda unos minutos.

Al arrancar, Flyway aplica las migraciones de `db/migration` y deja constancia en la tabla `flyway_schema_history`. En los logs verás algo como `Successfully applied 1 migration`.

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Abre `http://localhost:5173`.

---

## Verificar que todo funciona

La pantalla del frontend muestra tres tarjetas. Si las tres están en verde, el entorno está listo.

También puedes comprobarlo desde la terminal:

```bash
curl http://localhost:8080/api/v1/ping
# {"servicio":"petcare-backend","estado":"arriba","marcaTiempo":"..."}

curl http://localhost:8080/api/v1/ping/db
# {"conexion":"ok","motor":"8.4.x","migracionesAplicadas":1}

curl http://localhost:8080/actuator/health
# {"status":"UP", ...}
```

| Comprobación | Qué demuestra |
|---|---|
| `/api/v1/ping` responde | Spring Boot arranca y sirve HTTP |
| `/api/v1/ping/db` responde | Hay conexión real a MySQL y Flyway corrió |
| La pantalla de React está en verde | Vite compila y Axios llega al backend sin problemas de CORS |
| `docker compose ps` dice `healthy` | MySQL está arriba |
| `git status` responde | El repositorio está inicializado |

`/api/v1/ping` y `/api/v1/ping/db` son temporales: existen solo para esta verificación y se borran cuando lleguen los endpoints reales.

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

**Fechas.** Todo se guarda en UTC. La conversión a `America/Bogota` ocurre solo al mostrar.

**Ramas.** Una por historia: `feat/HU-007-registro-cliente`. Commits que digan qué hacen: `HU-007: validar unicidad del correo en el registro`.

**Secretos.** Nunca en el repositorio. `.env` está en `.gitignore`; `.env.example` documenta qué variables hacen falta.

---

## Estado

| Sprint | Contenido | Estado |
|---|---|:--:|
| 0 | Entorno, estructura y despliegue | En curso |
| 1 | Registro, login, autorización, logout, mascotas | Pendiente |

El análisis, el backlog y las historias detalladas están en `docs/`.
