package com.petcare.agenda.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorFranjasTest {

    private static final int FRANJA = 30;

    private static Franja tramo(String desde, String hasta) {
        return new Franja(LocalTime.parse(desde), LocalTime.parse(hasta));
    }

    @Test
    @DisplayName("Un paseo de 60 min en 08:00-12:00 da 7 inicios, de 08:00 a 11:00")
    void paseoDeUnaHora() {
        List<Franja> franjas = GeneradorFranjas.generar(List.of(tramo("08:00", "12:00")), 60, FRANJA);

        assertEquals(7, franjas.size());
        assertEquals(LocalTime.parse("08:00"), franjas.get(0).inicio());
        assertEquals(LocalTime.parse("11:00"), franjas.get(6).inicio());
        assertEquals(LocalTime.parse("12:00"), franjas.get(0).fin());
    }

    @Test
    @DisplayName("PRG-70: la consulta de 45 min ocupa 60 y no ofrece un inicio a las 11:30")
    void veterinariaOcupaDosFranjas() {
        List<Franja> franjas = GeneradorFranjas.generar(List.of(tramo("08:00", "12:00")), 45, FRANJA);

        assertEquals(7, franjas.size());
        assertEquals(30, java.time.Duration.between(
                franjas.get(0).inicio(), franjas.get(0).fin()).toMinutes());
        assertTrue(franjas.stream().noneMatch(f -> f.inicio().equals(LocalTime.parse("11:30"))));
    }

    @Test
    @DisplayName("Dos tramos con almuerzo en medio no generan franjas en el hueco")
    void jornadaPartida() {
        List<Franja> franjas = GeneradorFranjas.generar(List.of(tramo("08:00", "12:00"), tramo("14:00", "18:00")), 120, FRANJA);

        assertEquals(10, franjas.size());
        assertTrue(franjas.stream().noneMatch(f ->
                f.inicio().isAfter(LocalTime.parse("10:00")) && f.inicio().isBefore(LocalTime.parse("14:00"))));
    }

    @Test
    @DisplayName("Un servicio más largo que el tramo no genera ninguna franja")
    void servicioQueNoCabe() {
        assertTrue(GeneradorFranjas.generar(List.of(tramo("08:00", "09:00")), 120, FRANJA).isEmpty());
    }

    @Test
    @DisplayName("La ultima franja puede terminar justo al cerrar el tramo")
    void ultimaFranjaEncaja() {
        List<Franja> franjas = GeneradorFranjas.generar(List.of(tramo("08:00", "09:00")), 60, FRANJA);

        assertEquals(1, franjas.size());
        assertEquals(LocalTime.parse("09:00"), franjas.get(0).fin());
    }

    @Test
    @DisplayName("El solape ignora los intervalos que solo se tocan")
    void solapeEnLosBordes() {
        Franja f = new Franja(LocalTime.parse("09:00"), LocalTime.parse("10:00"));

        assertTrue(f.seSolapaCon(LocalTime.parse("09:30"), LocalTime.parse("10:30")));
        assertTrue(f.seSolapaCon(LocalTime.parse("08:00"), LocalTime.parse("11:00")));
        assertFalse(f.seSolapaCon(LocalTime.parse("10:00"), LocalTime.parse("11:00")));
        assertFalse(f.seSolapaCon(LocalTime.parse("08:00"), LocalTime.parse("09:00")));
    }

    @Test
    @DisplayName("Rejilla de 15 minutos: la consulta de 45 ya no se redondea")
    void rejillaDeQuince() {
        List<Franja> franjas =  GeneradorFranjas.generar(List.of(tramo("08:00", "09:00")), 45, 15);

        assertEquals(2, franjas.size());
        assertEquals(LocalTime.parse("08:15"), franjas.get(1).inicio());
    }
}
