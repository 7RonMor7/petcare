package com.petcare.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Endpoint temporal de verificacion. Existe solo para comprobar que la cadena
 * completa funciona: HTTP -> Spring -> conexion JDBC -> MySQL.
 *
 * Se elimina en cuanto tengamos endpoints reales (HU-007 en adelante).
 */
@RestController
@RequestMapping("/api/v1")
public class PingController {

    private final JdbcTemplate jdbcTemplate;

    public PingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "servicio", "petcare-backend",
                "estado", "arriba",
                "marcaTiempo", Instant.now().toString()
        );
    }

    @GetMapping("/ping/db")
    public Map<String, Object> pingBaseDatos() {
        String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
        Integer migraciones = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = 1", Integer.class);

        return Map.of(
                "conexion", "ok",
                "motor", version,
                "migracionesAplicadas", migraciones
        );
    }
}
