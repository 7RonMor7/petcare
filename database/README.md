# database/

Esta carpeta **no contiene el esquema**.

El esquema de PetCare vive en las migraciones de Flyway, dentro de
`backend/src/main/resources/db/migration/`. Flyway las aplica solas al
arrancar el backend, en orden (`V1__`, `V2__`, ...), y lleva su propio
registro en la tabla `flyway_schema_history`.

Por que asi y no con scripts sueltos: el esquema es codigo. Debe estar
versionado junto al codigo que lo usa, aplicarse igual en tu portatil y en
el servidor, y poder reconstruirse desde cero en cualquier maquina.

Aqui solo van utilidades locales: volcados de datos de prueba, notas de
consultas, cosas de ese estilo.

MySQL corre en Docker (`docker-compose.yml` en la raiz) y sus datos persisten
en el volumen `petcare-mysql-data`, no en esta carpeta.
