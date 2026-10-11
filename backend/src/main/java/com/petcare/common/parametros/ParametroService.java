package com.petcare.common.parametros;

import com.petcare.pagos.repository.OrdenPagoRepository;
import com.petcare.reservas.repository.OcupacionFranjaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParametroService {

    public static final String TAMANO_FRANJA = "tamano_franja_minutos";
    public static final String HORA_APERTURA = "jornada_hora_apertura";
    public static final String HORA_CIERRE = "jornada_hora_cierra";
    public static final String ANTELACION_MINIMA = "antelacion_minima_horas";
    public static final String ANTELACION_MAXIMA = "antelacion_maxima_dias";
    public static final String ZONA_HORARIA = "zona_horaria";
    public static final String EXPIRACION_PAGO = "expiracion_pago_minutos";
    public static final String CANCELACION_MINIMA = "cancelacion_minima_horas";

    private final ParametroRepository parametroRepository;
    private final OcupacionFranjaRepository  ocupacionRepository;
    private final OrdenPagoRepository  ordenPagoRepository;

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

    @Transactional(readOnly = true)
    public String texto(String clave, String porDefecto) {
        return parametroRepository.findById(clave).map(ParametroSistema::getValor).orElse(porDefecto);
    }
}
