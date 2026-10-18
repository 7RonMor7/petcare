# Guía — HU-005: Despliegue público

**Sprint 0 (ejecutada al cerrar el Sprint 6) · 3 SP · IRR-24, RSG-01**

> Como equipo, quiero la aplicación desplegada en una URL pública con HTTPS, para poder recibir
> webhooks reales y mostrar el producto funcionando.

**Por qué no se puede posponer más:** los webhooks de la pasarela no llegan a `localhost`. Hasta
que exista una URL pública, HU-041 solo se puede probar con el simulador.

---

## Arquitectura del despliegue

```
Cloudflare Pages / Vercel        Railway
┌──────────────────────┐      ┌─────────────────────────────┐
│  frontend estático   │─────▶│  backend (contenedor Java)  │
│  VITE_API_URL ───────┼──────┤  Flyway migra al arrancar   │
└──────────────────────┘      │             │               │
                              │      MySQL gestionado       │
                              └─────────────────────────────┘
                                        ▲
                                  webhook de Wompi
```

El frontend compilado son archivos estáticos: no necesita servidor propio y no se duerme.

## Elección de plataforma (octubre 2026)

| Plataforma | Situación | Veredicto |
|---|---|---|
| **Railway** | MySQL y Postgres gestionados; crédito de 5 USD el primer mes, 1 USD/mes después | **Elegida**: es la única con MySQL gestionado accesible |
| Render | Servicios gratis que se duermen a los 15 min; base gratis **solo PostgreSQL**, expira a 30 días | Obligaría a migrar 15 migraciones a Postgres |
| Fly.io | Sin free tier para cuentas nuevas | Descartada |

Los límites cambian: verificarlos al registrarse.

## Preparación en el repositorio

| Archivo | Qué hace |
|---|---|
| `backend/Dockerfile` | Build en dos etapas: Maven+JDK para compilar, JRE para ejecutar (~250 MB) |
| `backend/.dockerignore` | Evita copiar `target/`, `.env` y los `.json` de pruebas al contexto |
| `frontend/.dockerignore` | Igual para `node_modules` y `dist` |

**Las dos etapas importan:** la imagen publicada no lleva Maven, ni el código fuente, ni las
dependencias de compilación. Menos peso, despliegues más rápidos y menos superficie de ataque.
Además el contenedor corre con un usuario sin privilegios, no como root.

## Variables de entorno en producción

Ninguna va al repositorio. En el panel de Railway, para el servicio del backend:

| Variable | Valor |
|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | Los que expone el servicio MySQL de Railway |
| `SERVER_PORT` | `${PORT}` — la plataforma asigna el puerto |
| `JWT_SECRETO` | Cadena larga y aleatoria, **distinta** a la de desarrollo |
| `CORS_ORIGENES` | La URL pública del frontend |
| `PAGOS_PROVEEDOR` | `simulado` hasta que exista el adaptador de Wompi |
| `PAGOS_SECRETO` | Secreto del webhook, aleatorio |
| `PAGOS_URL_CHECKOUT`, `PAGOS_URL_RETORNO` | Rutas del frontend público |

En el frontend (Cloudflare Pages o Vercel), una sola: `VITE_API_URL` con la URL pública del backend
más `/api/v1`. Vite la incrusta **en tiempo de compilación**, así que cambiarla exige volver a
construir, no solo reiniciar.

## Lista de comprobación del despliegue

| # | Comprobación | Cómo |
|:--:|---|---|
| 1 | La imagen compila localmente | `docker build -t petcare-backend ./backend` |
| 2 | Flyway aplica las 15 migraciones en la base nueva | Logs del primer arranque |
| 3 | `/actuator/health` responde `UP` | Navegador o `curl` |
| 4 | `/api/v1/servicios` devuelve el catálogo | Es público: se ve sin sesión |
| 5 | El frontend carga y el registro funciona | Crear un usuario de prueba |
| 6 | HTTPS activo y sin aviso del navegador | Candado en la barra |
| 7 | CORS correcto | Ninguna petición bloqueada en la consola |
| 8 | El ciclo completo: reservar → pagar → confirmar | Con el simulador, ya en público |
| 9 | La tarea de expiración corre en el servidor | Dejar una reserva sin pagar y mirar los logs |

## Riesgos y deudas

- **El crédito de Railway se agota.** Para la sustentación conviene verificar el día anterior que
  los servicios siguen arriba.
- **Una sola instancia**, así que la tarea `@Scheduled` no necesita bloqueo distribuido todavía.
- **Las pruebas se saltan en el build** (`-DskipTests`) porque necesitan MySQL. Lo correcto es
  ejecutarlas en CI con Testcontainers, que es la mejora de infraestructura ya acordada.
- **La base de producción arranca vacía**: hay que crear el administrador inicial y revisar que el
  catálogo de V7 se haya cargado.
- **El adaptador real de Wompi sigue pendiente.** El despliegue lo desbloquea, no lo reemplaza.

---

## Lo que falló de verdad al desplegar

El despliegue salió, pero ninguno de los cinco problemas estaba en el código: todos eran de
configuración o de infraestructura. Vale la pena dejarlos escritos, porque son los que se repiten.

**1. Railway construyó con Nixpacks en vez del Dockerfile.** Detectó el proyecto Maven por su
cuenta, compiló con Java 21 y arrancó como root. El `Dockerfile` existía pero nadie lo usaba. Se
corrige fijando el *Builder* en Dockerfile desde el panel, o declarándolo en `railway.json`.
Lección: tener el archivo no significa que la plataforma lo esté usando.

**2. `Communications link failure` contra MySQL.** La red privada de Railway es solo IPv6 y la JVM
prefiere IPv4, así que el backend no resolvía el host interno. Se resolvió apuntando al *TCP proxy*
público del servicio MySQL. La alternativa era `JAVA_TOOL_OPTIONS=-Djava.net.preferIPv6Addresses=true`.

**3. `Incorrect usage of UPDATE and LIMIT` al crear el administrador.** La consola SQL de Railway
añade un `LIMIT` a cada sentencia y MySQL no lo admite en un `UPDATE` con `JOIN`. La misma
actualización escrita con subconsultas, sobre una sola tabla, pasa sin problema. El rol se cambia en
base de datos porque **ningún endpoint crea administradores**: es una decisión de diseño, no un
olvido.

**4. La URL de pago salió con una coma dentro.** A `PAGOS_URL_CHECKOUT` se le pusieron las dos URLs
separadas por coma, copiando el formato de `CORS_ORIGENES`. Pero `origenes-permitidos` se inyecta en
un `String[]` y Spring lo parte por comas, mientras que `url-checkout` se inyecta en un `String` y se
concatena tal cual. **Una variable de entorno admite varios valores solo si el tipo destino los
admite.** Además la URL mala quedó persistida en `orden_pago.url_checkout`, porque `CheckoutService`
solo pide sesión nueva `if (!orden.tieneCheckoutAbierto())`: arreglar la variable no arregla las
órdenes ya emitidas.

**5. Vercel devolvía 404 en `/pago-simulado`.** En una SPA las rutas las resuelve React Router en el
navegador; el servidor solo tiene `index.html`. Vercel buscaba un archivo con ese nombre y respondía
su propio 404 antes de que React arrancara. Se corrige con `frontend/vercel.json` reenviando
cualquier ruta a `index.html`. En local no se nota porque el servidor de Vite ya lo hace.

## Qué quedó verificado en producción

Las nueve comprobaciones de la lista pasaron, y sobre la aplicación desplegada se ejecutó el ciclo
completo: administrador inicial creado, empleado dado de alta con jornada y servicios asignados,
cliente registrado desde el formulario público, mascota creada, reserva tomada desde el asistente,
pago por la pasarela simulada y reserva **CONFIRMADA** por el webhook firmado.

La verificación de que la reserva quedó asignada al empleado correcto se hizo por SQL, no por la
interfaz: el empleado todavía no tiene pantalla donde ver sus reservas. `MiAgendaController` expone
jornada y bloqueos, nada más. Eso es **HU-025/026** y queda como lo primero del siguiente sprint,
porque hoy el empleado no puede saber a quién atiende sin mirar la base de datos.
