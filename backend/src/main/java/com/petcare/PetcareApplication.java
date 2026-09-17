package com.petcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PetcareApplication {

    /**
     * Toda fecha/hora se guarda en UTC y solo se convierte a America/Bogota
     * al mostrarla. Fijar aqui la zona de la JVM evita que el comportamiento
     * dependa de la maquina donde se ejecute (tu portatil vs. el servidor).
     */
    @PostConstruct
    void configurarZonaHoraria() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(PetcareApplication.class, args);
    }
}
