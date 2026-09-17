package com.petcare;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de humo del Sprint 0.
 *
 * Comprueba lo unico que nos importa en este punto: que la aplicacion
 * levanta, que hay conexion a MySQL y que Flyway aplico las migraciones.
 *
 * REQUIERE que MySQL este corriendo:  docker compose up -d
 * Si no lo esta, usa:                 mvn package -DskipTests
 */
@SpringBootTest
class PetcareApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void laAplicacionArrancaYSeConectaALaBaseDeDatos() {
        Integer resultado = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(resultado).isEqualTo(1);
    }

    @Test
    void flywayAplicoLaMigracionInicial() {
        Integer parametros = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM parametro_sistema", Integer.class);
        assertThat(parametros).isGreaterThan(0);
    }
}
