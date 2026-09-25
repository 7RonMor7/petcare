package com.petcare.common.parametros;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ParametroService {

    public static final String TAMANO_FRANJA = "tamano_franja_minutos";
    public static final String HORA_APERTURA = "jornada_hora_apertura";
    public static final String HORA_CIERRE = "jornada_hora_cierra";

    private final ParametroRepository parametroRepository;

    @Transactional(readOnly = true)
    public int entero(String clave, int porDefecto) {
        return parametroRepository.findById(clave)
                .map(p -> Integer.parseInt(p.getValor()))
                .orElse(porDefecto);
    }

    @Transactional(readOnly = true)
    public LocalTime hora(String clave, LocalTime porDefecto) {
        return parametroRepository.findById(clave)
                .map(p -> LocalTime.parse(p.getValor()))
                .orElse(porDefecto);
    }
}
