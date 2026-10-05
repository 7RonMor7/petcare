package com.petcare.reservas;

import com.petcare.agenda.domain.EmpleadoServicio;
import com.petcare.agenda.domain.JornadaLaboral;
import com.petcare.agenda.repository.EmpleadoServicioRepository;
import com.petcare.agenda.repository.JornadaRepository;
import com.petcare.mascotas.domain.Especie;
import com.petcare.mascotas.domain.Mascota;
import com.petcare.mascotas.domain.Sexo;
import com.petcare.mascotas.repository.MascotaRepository;
import com.petcare.reservas.dto.ReservaRequest;
import com.petcare.reservas.repository.OcupacionFranjaRepository;
import com.petcare.reservas.repository.ReservaRepository;
import com.petcare.reservas.service.ReservaService;
import com.petcare.servicios.domain.Servicio;
import com.petcare.servicios.domain.UnidadCobro;
import com.petcare.servicios.repository.ServicioRepository;
import com.petcare.usuarios.domain.Rol;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.RolRepository;
import com.petcare.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("RNF07-R: bajo peticiones concurrentes, solo una reserva gana la franja")
class ReservaConcurrenciaTest {

    private static final int HILOS = 20;

    @Autowired private ReservaService reservaService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private MascotaRepository mascotaRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private JornadaRepository jornadaRepository;
    @Autowired private EmpleadoServicioRepository empleadoServicioRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private OcupacionFranjaRepository ocupacionFranjaRepository;

    private Usuario cliente;
    private Usuario empleado;
    private Mascota mascota;
    private Servicio servicio;
    private LocalDate fecha;

    @BeforeEach
    void prepararEscenario() {
        String sufijo = UUID.randomUUID().toString().substring(0, 8);

        Rol rolCliente = rolRepository.findByNombre("CLIENTE").orElseThrow();
        Rol rolEmpleado = rolRepository.findByNombre("EMPLEADO").orElseThrow();

        cliente = new Usuario("Prueba", "Cliente", "cliente-" + sufijo + "@test.local",
                null, "$2a$10$noimporta", "1.0");
        cliente.asignarRol(rolCliente);
        cliente = usuarioRepository.save(cliente);

        empleado = new Usuario("Prueba", "Empleado", "empleado-" + sufijo + "@test.local",
                null, "$2a$10$noimporta", null);
        empleado.asignarRol(rolEmpleado);
        empleado = usuarioRepository.save(empleado);

        mascota = mascotaRepository.save(new Mascota(cliente, "Toby", Especie.PERRO, null,
                Sexo.MACHO, LocalDate.of(2022, 3, 15), new BigDecimal("12.50"), null));

        servicio = servicioRepository.save(new Servicio("Prueba" + sufijo,
                null, new BigDecimal("25000.00"), UnidadCobro.POR_SERVICIO, 60));

        // Una fecha futura dentro de la ventana de RB14, y la jornada del día
        // de la semana que le toque: así la prueba sirve cualquier día del año.
        fecha = LocalDate.now().plusDays(7);
        jornadaRepository.save(new JornadaLaboral(empleado, fecha.getDayOfWeek(),
                LocalTime.of(8, 0), LocalTime.of(18, 0)));
        empleadoServicioRepository.save(new EmpleadoServicio(empleado, servicio));
    }

    @Test
    void soloUnaDeVeinteReservasGanaLaFranja() throws Exception {
        ReservaRequest peticion = new ReservaRequest(
                mascota.getId(), servicio.getId(), fecha, LocalTime.of(10, 0), empleado.getId());

        ExecutorService pool = Executors.newFixedThreadPool(HILOS);
        CountDownLatch salida = new CountDownLatch(1);
        CountDownLatch terminados = new CountDownLatch(HILOS);

        AtomicInteger exitos = new AtomicInteger();
        AtomicInteger rechazos =  new AtomicInteger();

        for (int i = 0; i < HILOS; i++) {
            pool.submit(() -> {
                try {
                    salida.await();
                    reservaService.crear(cliente.getId(), peticion);
                    exitos.incrementAndGet();
                } catch (Exception e) {
                    rechazos.incrementAndGet();
                } finally {
                    terminados.countDown();
                }
            });
        }

        salida.countDown();
        assertTrue(terminados.await(30, TimeUnit.SECONDS), "Los hilos no terminaron a tiempo");
        pool.shutdown();

        assertEquals(1, exitos.get(), "Debe crearse exactamente una reserva");
        assertEquals(HILOS - 1, rechazos.get(), "Las demás deben ser rechazadas");

        // La base de datos tiene que contarlo igual
        assertEquals(1, reservaRepository.findByClienteIdOrderByFechaHoraInicioDesc(cliente.getId()).size());
        assertEquals(2, ocupacionFranjaRepository.count(), "60 minutos - dos franjas de 30");
    }

    @AfterEach
    void limpiar() {
        ocupacionFranjaRepository.deleteAll();
        reservaRepository.deleteAll();
        empleadoServicioRepository.deleteAll(
                empleadoServicioRepository.findByEmpleadoIdOrderByServicioNombreAsc(empleado.getId()));
        jornadaRepository.deleteAll(
                jornadaRepository.findByEmpleadoIdOrderByDiaSemanaAscHoraInicioAsc(empleado.getId()));
        mascotaRepository.delete(mascota);
        servicioRepository.delete(servicio);
        usuarioRepository.deleteAll(List.of(cliente, empleado));
    }
}
