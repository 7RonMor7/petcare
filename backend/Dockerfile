# ---------------------------------------------------------------------------
# Imagen del backend en DOS ETAPAS.
#
# Etapa 1 (build): necesita Maven y el JDK completo -> imagen de ~800 MB.
# Etapa 2 (runtime): solo necesita el JRE y un .jar -> imagen de ~250 MB.
#
# Lo que se publica es la segunda. La primera se descarta: el servidor no
# necesita Maven, ni el codigo fuente, ni las dependencias de compilacion.
# Menos peso es menos tiempo de despliegue y menos superficie de ataque.
# ---------------------------------------------------------------------------

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Primero el pom solo: si no cambian las dependencias, Docker reutiliza esta
# capa y no vuelve a descargar medio internet en cada despliegue.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B
# -DskipTests: las pruebas necesitan MySQL y ya corrieron en tu maquina.
# Cuando exista CI (y Testcontainers), este skip se quita.

# ---------------------------------------------------------------------------
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Usuario sin privilegios: si alguien lograra ejecutar codigo dentro del
# contenedor, no seria root.
RUN useradd --system --uid 1001 petcare
USER petcare

COPY --from=build /app/target/*.jar app.jar

# Railway, Render y casi todos inyectan el puerto en la variable PORT.
ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
