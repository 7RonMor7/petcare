package com.petcare.agenda.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica pura: sin Spring, sin base de datos, sin fechas del sistema.
 * Todo lo que necesita llegar por parametro, asi que se puede probar entero
 * en milisegundo y razonar sobre el sin levantar la aplicacion
 */

public final class GeneradorFranjas {

    private GeneradorFranjas() {}

    public static List<Franja> generar(List<Franja> tramos, int duracionMinutos, int tamanoFranja) {
        if (duracionMinutos <= 0 || tamanoFranja <= 0) {
            throw new IllegalArgumentException("La duración y el tamaño de franja deben ser positivos");
        }

        int ocupacion = redondearArriba(duracionMinutos, tamanoFranja);
        List<Franja> franjas = new ArrayList<>();

        for (Franja tramo : tramos) {
            LocalTime inicio = tramo.inicio();
            while (!inicio.plusMinutes(ocupacion).isAfter(tramo.fin())) {
                franjas.add(new Franja(inicio, inicio.plusMinutes(ocupacion)));
                inicio = inicio.plusMinutes(tamanoFranja);
            }
        }
        return franjas;
    }

    static int redondearArriba(int duracion, int tamanoFranja) {
        return ((duracion + tamanoFranja - 1) / tamanoFranja)  * tamanoFranja;
    }
}
