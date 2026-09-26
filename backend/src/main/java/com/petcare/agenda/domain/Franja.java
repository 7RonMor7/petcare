package com.petcare.agenda.domain;

import java.time.LocalTime;

public record Franja(LocalTime inicio, LocalTime fin) {

    public boolean seSolapaCon(LocalTime otroInicio,  LocalTime otroFin) {
        return inicio.isBefore(otroFin) && otroInicio.isBefore(fin);
    }
}
