package com.petcare.agenda.service;

import com.petcare.agenda.domain.BloqueoAgenda;
import com.petcare.agenda.dto.BloqueoRequest;
import com.petcare.agenda.dto.BloqueoResponse;
import com.petcare.agenda.repository.BloqueoRepository;
import com.petcare.common.error.ConflictoException;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.error.ReglaNegocioException;
import com.petcare.common.parametros.ParametroService;
import com.petcare.reservas.domain.EstadoReserva;
import com.petcare.reservas.domain.Reserva;
import com.petcare.reservas.repository.ReservaRepository;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloqueoService {

    private final BloqueoRepository bloqueoRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParametroService parametros;

    @Transactional(readOnly = true)
    public List<BloqueoResponse> listar(Long empleadoId, LocalDate desde, LocalDate hasta) {
        ZoneId zona = zona();
        return bloqueoRepository.deEmpleado(empleadoId,
                desde.atStartOfDay(zona).toInstant(),
                hasta.plusDays(1).atStartOfDay(zona).toInstant())
                .stream().map(b -> BloqueoResponse.desde(b, zona)).toList();
    }

    @Transactional
    public BloqueoResponse crear(Long empleadoId, BloqueoRequest datos) {
        if (!datos.horaFin().isAfter(datos.horaInicio())) {
            throw new ReglaNegocioException("BLOQUEO_INVALIDO",
                    "La hora de fin debe ser posterior a la de inicio");
        }

        ZoneId zona = zona();
        Instant inicio = ZonedDateTime.of(datos.fecha(), datos.horaInicio(), zona).toInstant();
        Instant fin = ZonedDateTime.of(datos.fecha(), datos.horaFin(), zona).toInstant();

        // RB13
        List<Reserva> chocan = reservaRepository.ocupacion(
                List.of(empleadoId), EstadoReserva.ACTIVOS, inicio, fin);
        if (!chocan.isEmpty()) {
            String horas = chocan.stream()
                    .map(r -> LocalDateTime.ofInstant(r.getFechaHoraInicio(), zona).toLocalTime()
                            + "-" + LocalDateTime.ofInstant(r.getFechaHoraFin(), zona).toLocalTime())
                    .collect(Collectors.joining(", "));
            throw new ConflictoException("BLOQUEO_CON_RESERVAS",
                    "No se puede bloquear: hay " + chocan.size() + " reserva(s) en " + horas);
        }

        Usuario empleado = usuarioRepository.getReferenceById(empleadoId);
        BloqueoAgenda bloqueo = bloqueoRepository.save(
                new BloqueoAgenda(empleado, inicio, fin, datos.motivo()));

        return BloqueoResponse.desde(bloqueo, zona);
    }

    @Transactional
    public void eliminar(Long id, Long empleadoId) {
        BloqueoAgenda bloqueo = bloqueoRepository.findByIdAndEmpleadoId(id, empleadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El bloqueo no existe"));
        bloqueoRepository.delete(bloqueo);
    }

    private ZoneId zona() {
        return ZoneId.of(parametros.texto(ParametroService.ZONA_HORARIA, "America/Bogota"));
    }
}
