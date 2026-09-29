package com.petcare.agenda.service;

import com.petcare.agenda.domain.BloqueoAgenda;
import com.petcare.agenda.domain.Franja;
import com.petcare.agenda.domain.GeneradorFranjas;
import com.petcare.agenda.dto.FranjaDisponibleResponse;
import com.petcare.agenda.repository.BloqueoRepository;
import com.petcare.agenda.repository.EmpleadoServicioRepository;
import com.petcare.agenda.repository.JornadaRepository;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.error.ReglaNegocioException;
import com.petcare.common.parametros.ParametroService;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import com.petcare.reservas.repository.ReservaRepository;
import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.domain.UnidadCobro;
import com.petcare.servicios.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {

    private final ServicioRepository servicioRepository;
    private final EmpleadoServicioRepository empleadoServicioRepository;
    private final JornadaRepository jornadaRepository;
    private final ReservaRepository reservaRepository;
    private final BloqueoRepository bloqueoRepository;
    private final ParametroService parametros;

    @Transactional(readOnly = true)
    public List<FranjaDisponibleResponse> consultar(Long servicioId, LocalDate fecha) {

        Servicio servicio = servicioRepository.findByIdAndActivoTrue(servicioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El servicio no existe"));

        if (servicio.getUnidadCobro() != UnidadCobro.POR_SERVICIO) {
            throw new ReglaNegocioException("SERVICIO_SIN_AGENDA",
                    "Este servicio no se reserva por franjas horarias");
        }

        ZoneId zona = ZoneId.of(parametros.texto(ParametroService.ZONA_HORARIA, "America/Bogota"));
        int tamanoFranja = parametros.entero(ParametroService.TAMANO_FRANJA, 30);
        int antelacionMin = parametros.entero(ParametroService.ANTELACION_MINIMA, 2);
        int antelacionMax = parametros.entero(ParametroService.ANTELACION_MAXIMA, 30);

        LocalDate hoy = LocalDate.now(zona);
        if (fecha.isBefore(hoy) || fecha.isAfter(hoy.plusDays(antelacionMax))) {
            throw new ReglaNegocioException("FECHA_FUERA_DE_RANGO",
                    "La fecha debe estar entre hoy y " + antelacionMax + " días");
        }

        List<Long> empleadoIds = empleadoServicioRepository
                .findByServicioIdAndEmpleadoActivoTrue(servicioId).stream()
                .map(es -> es.getEmpleado().getId())
                .toList();
        if (empleadoIds.isEmpty()) return List.of();

        Instant inicioDia = fecha.atStartOfDay(zona).toInstant();
        Instant finDia = fecha.plusDays(1).atStartOfDay(zona).toInstant();
        Instant minimo = Instant.now().plus(antelacionMin, ChronoUnit.HOURS);

        // --- Lo ocupado, por empleado y en hora local ---
        Map<Long, List<Franja>> ocupado = new HashMap<>();
        for (Reserva r : reservaRepository.ocupacion(empleadoIds, EstadoReserva.ACTIVOS, inicioDia, finDia)) {
            ocupado.computeIfAbsent(r.getEmpleado().getId(), k -> new ArrayList<>())
                    .add(aHoraLocal(r.getFechaHoraInicio(), r.getFechaHoraFin(), zona, fecha));
        }
        for (BloqueoAgenda b : bloqueoRepository.ocupacion(empleadoIds, inicioDia, finDia)) {
            ocupado.computeIfAbsent(b.getEmpleado().getId(), k -> new ArrayList<>())
                    .add(aHoraLocal(b.getFechaHoraInicio(), b.getFechaHoraFin(), zona, fecha));
        }

        // --- La jornada de ese día, por empleado ---
        Map<Long, List<Franja>> tramos = jornadaRepository
                .findByEmpleadoIdInAndDiaSemana(empleadoIds, fecha.getDayOfWeek()).stream()
                .collect(Collectors.groupingBy(
                        j -> j.getEmpleado().getId(),
                        Collectors.mapping(j -> new Franja(j.getHoraInicio(), j.getHoraFin()), Collectors.toList())));

        // --- Generar, filtrar y agrupar ---
        Map<Franja, List<Long>> porFranja = new TreeMap<>(
                Comparator.comparing(Franja::inicio).thenComparing(Franja::fin));

        for (Long empleadoId : empleadoIds) {
            List<Franja> jornada = tramos.getOrDefault(empleadoId, List.of());
            List<Franja> ocupadas = ocupado.getOrDefault(empleadoId, List.of());

            for (Franja f : GeneradorFranjas.generar(jornada, servicio.getDuracionMinutos(), tamanoFranja)) {
                boolean chocada = ocupadas.stream().anyMatch(o -> f.seSolapaCon(o.inicio(), o.fin()));
                if (chocada) continue;

                Instant inicioReal = ZonedDateTime.of(fecha, f.inicio(), zona).toInstant();
                if (inicioReal.isBefore(minimo)) continue;

                porFranja.computeIfAbsent(f, k -> new ArrayList<>()).add(empleadoId);
            }
        }

        return porFranja.entrySet().stream()
                .map(e -> new FranjaDisponibleResponse(e.getKey().inicio(), e.getKey().fin(), e.getValue()))
                .toList();
    }

    /** Un instante UTC pasado a la hora local del día consultado, recortado a ese día. */
    private Franja aHoraLocal(Instant inicio, Instant fin, ZoneId zona, LocalDate fecha) {
        LocalDateTime i = LocalDateTime.ofInstant(inicio, zona);
        LocalDateTime f = LocalDateTime.ofInstant(fin, zona);
        return new Franja(
                i.toLocalDate().isBefore(fecha) ? LocalTime.MIN : i.toLocalTime(),
                f.toLocalDate().isAfter(fecha) ? LocalTime.MAX : f.toLocalTime());
    }
}
