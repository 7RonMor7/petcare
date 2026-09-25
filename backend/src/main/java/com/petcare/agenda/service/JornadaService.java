package com.petcare.agenda.service;

import com.petcare.agenda.domain.JornadaLaboral;
import com.petcare.agenda.dto.TramoRequest;
import com.petcare.agenda.dto.TramoResponse;
import com.petcare.agenda.repository.JornadaRepository;
import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.common.error.ReglaNegocioException;
import com.petcare.common.parametros.ParametroService;
import com.petcare.usuarios.repository.UsuarioRepository;
import com.petcare.usuarios.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JornadaService {

    private static final String ROL_EMPLEADO = "EMPLEADO";

    private final JornadaRepository jornadaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParametroService parametros;

    @Transactional(readOnly = true)
    public List<TramoResponse> consultar(Long empleadoId) {
        buscarEmpleado(empleadoId);
        return jornadaRepository.findByEmpleadoIdOrderByDiaSemanaAscHoraInicioAsc(empleadoId)
                .stream().map(TramoResponse::desde).toList();
    }

    @Transactional
    public List<TramoResponse> reemplazar(Long empleadoId, List<TramoRequest> tramos) {
        Usuario empleado = buscarEmpleado(empleadoId);
        validar(tramos);

        jornadaRepository.deleteByEmpleadoId(empleadoId);
        jornadaRepository.flush();     // el DELETE va antes que los INSERT

        List<JornadaLaboral> nuevos = tramos.stream()
                .map(t -> new JornadaLaboral(empleado, t.diaSemana(), t.horaInicio(), t.horaFin()))
                .toList();

        return jornadaRepository.saveAll(nuevos).stream().map(TramoResponse::desde).toList();
    }

    private void validar(List<TramoRequest> tramos) {
        int franja = parametros.entero(ParametroService.TAMANO_FRANJA, 30);
        LocalTime apertura = parametros.hora(ParametroService.HORA_APERTURA, LocalTime.of(8, 0));
        LocalTime cierre = parametros.hora(ParametroService.HORA_CIERRE, LocalTime.of(18, 0));

        for (TramoRequest t : tramos) {
            if (!t.horaFin().isAfter(t.horaInicio())) {
                throw new ReglaNegocioException("JORNADA_INVALIDA",
                        "La hora de fin debe ser posterior a la de inicio");
            }
            if (t.horaInicio().isBefore(apertura) || t.horaFin().isAfter(cierre)) {
                throw new ReglaNegocioException("JORNADA_FUERA_DE_HORARIO",
                        "La jornade debe estar entre " + apertura + " y " + cierre);
            }
            if (!alineada(t.horaInicio(), franja) || !alineada(t.horaFin(), franja)) {
                throw new ReglaNegocioException("JORNADA_DESALINEADA",
                        "Las horas deben ir en múltiplos de " + franja + " minutos");
            }
        }

        // Solapas: se comparan solo los tramos del mismo día
        Map<DayOfWeek, List<TramoRequest>> porDia = tramos.stream()
                .collect(Collectors.groupingBy(TramoRequest::diaSemana));

        for (List<TramoRequest> delDia : porDia.values()) {
            List<TramoRequest> ordenados = delDia.stream()
                    .sorted(Comparator.comparing(TramoRequest::horaInicio))
                    .toList();
            for (int i = 1; i < ordenados.size(); i++) {
                if (ordenados.get(i).horaInicio().isBefore(ordenados.get(i - 1).horaFin())) {
                    throw new ReglaNegocioException("JORNADA_SOLAPADA",
                            "Hay tramos que se solapan el mismo día");
                }
            }
        }
    }

    private boolean alineada(LocalTime hora, int franja) {
        return hora.getSecond() == 0 && hora.getMinute() % franja == 0;
    }

    private Usuario buscarEmpleado(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getRoles().stream().anyMatch(r -> ROL_EMPLEADO.equals(r.getNombre())))
                .orElseThrow(() -> new RecursoNoEncontradoException("El empleado no existe"));
    }
}
