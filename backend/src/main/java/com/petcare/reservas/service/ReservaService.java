package com.petcare.reservas.service;

import com.petcare.agenda.dto.FranjaDisponibleResponse;
import com.petcare.agenda.service.DisponibilidadService;
import com.petcare.common.error.ConflictoException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.error.ReglaNegocioException;
import com.petcare.common.parametros.ParametroService;
import com.petcare.mascotas.domain.Mascota;
import com.petcare.mascotas.repository.MascotaRepository;
import com.petcare.reservas.domain.OcupacionFranja;
import com.petcare.reservas.domain.Reserva;
import com.petcare.reservas.dto.ReservaRequest;
import com.petcare.reservas.dto.ReservaResponse;
import com.petcare.reservas.repository.OcupacionFranjaRepository;
import com.petcare.reservas.repository.ReservaRepository;
import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.domain.UnidadCobro;
import com.petcare.servicios.repository.ServicioRepository;
import com.petcare.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final OcupacionFranjaRepository ocupacionRepository;
    private final MascotaRepository mascotaRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DisponibilidadService disponibilidadService;
    private final ParametroService parametros;

    @Transactional
    public ReservaResponse crear(Long clienteId, ReservaRequest datos) {

        // RB01: la mascota es del cliente autenticado
        Mascota mascota = mascotaRepository
                .findByIdAndClienteIdAndActivoTrue(datos.mascotaId(), clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La mascota no existe"));

        Servicio servicio = servicioRepository.findByIdAndActivoTrue(datos.servicioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El servicio no existe"));

        if (servicio.getUnidadCobro() != UnidadCobro.POR_SERVICIO) {
            throw new ReglaNegocioException("SERVICIO_SIN_AGENDA",
                    "Este servicio no se reserva por franjas horarias");
        }

        // RB02, RB11, RB14 y bloqueos: el motor es la fuente de la verdad
        FranjaDisponibleResponse franja = disponibilidadService
                .consultar(datos.servicioId(), datos.fecha(), datos.empleadoId()).stream()
                .filter(f -> f.horaInicio().equals(datos.horaInicio()))
                .findFirst()
                .orElseThrow(() -> new ConflictoException("FRANJA_NO_DISPONIBLE",
                        "Ese horario ya no está disponible"));

        Long empleadoId = datos.empleadoId() != null
                ? datos.empleadoId()
                : disponibilidadService.asignar(datos.servicioId(), datos.fecha(), datos.horaInicio())
                .empleadoId();

        ZoneId zona = ZoneId.of(parametros.texto(ParametroService.ZONA_HORARIA, "America/Bogota"));
        int tamanoFranja = parametros.entero(ParametroService.TAMANO_FRANJA, 30);

        Instant inicio = ZonedDateTime.of(datos.fecha(), franja.horaInicio(), zona).toInstant();
        Instant fin = ZonedDateTime.of(datos.fecha(), franja.horaFin(), zona).toInstant();

        Reserva reserva = new Reserva(
                usuarioRepository.getReferenceById(clienteId),
                mascota,
                servicio,
                usuarioRepository.getReferenceById(empleadoId),
                inicio, fin,
                servicio.getPrecio());              // CN-01: total congelado

        reservaRepository.save(reserva);

        // RB11: una fila por cada franja que ocupa
        List<OcupacionFranja> ocupaciones = new ArrayList<>();
        for (Instant t = inicio; t.isBefore(fin); t = t.plus(tamanoFranja, ChronoUnit.MINUTES)) {
            ocupaciones.add(new OcupacionFranja(reserva, t));
        }

        try {
            ocupacionRepository.saveAll(ocupaciones);
            ocupacionRepository.flush();              // el choque tiene que saltar AQUÍ
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictoException("FRANJA_NO_DISPONIBLE",
                    "Ese horario acaba de ser tomado por otra reserva");
        }

        return ReservaResponse.desde(reserva, zona);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarPropias(Long clienteId) {
        ZoneId zona = ZoneId.of(parametros.texto(ParametroService.ZONA_HORARIA, "America/Bogota"));
        return reservaRepository.findByClienteIdOrderByFechaHoraInicioDesc(clienteId)
                .stream().map(r -> ReservaResponse.desde(r, zona)).toList();
    }

    @Transactional(readOnly = true)
    public ReservaResponse obtenerPropia(Long id, Long clienteId) {
        ZoneId zona = ZoneId.of(parametros.texto(ParametroService.ZONA_HORARIA, "America/Bogota"));
        return reservaRepository.findByIdAndClienteId(id, clienteId)
                .map(r -> ReservaResponse.desde(r, zona))
                .orElseThrow(() -> new RecursoNoEncontradoException("La reserva no existe"));
    }
}
