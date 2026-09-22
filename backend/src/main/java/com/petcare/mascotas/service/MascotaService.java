package com.petcare.mascotas.service;

import com.petcare.common.error.RecursoNoEncontradoException;
import com.petcare.mascotas.domain.Mascota;
import com.petcare.mascotas.dto.MascotaRequest;
import com.petcare.mascotas.dto.MascotaResponse;
import com.petcare.mascotas.repository.MascotaRepository;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MascotaResponse registrar(MascotaRequest datos, Long clienteId) {
        Usuario cliente = usuarioRepository.getReferenceById(clienteId);

        Mascota mascota = new Mascota(
                cliente, datos.nombre().trim(), datos.especie(), datos.raza(),
                datos.sexo(), datos.fechaNacimiento(), datos.pesoKg(),
                datos.observaciones());

        return MascotaResponse.desde(mascotaRepository.save(mascota));
    }

    @Transactional(readOnly = true)
    public List<MascotaResponse> listar(Long clienteId) {
        return mascotaRepository.findByClienteIdAndActivoTrueOrderByNombreAsc(clienteId)
                .stream()
                .map(MascotaResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public MascotaResponse obtener(Long id, Long clienteId) {
        return MascotaResponse.desde(buscarPropia(id, clienteId));
    }

    @Transactional
    public MascotaResponse actualizar(Long id, MascotaResponse datos, Long clienteId) {
        Mascota mascota = buscarPropia(id, clienteId);
        mascota.actualizarDatos(datos.nombre().trim(), datos.especie(), datos.raza(),
                datos.sexo(), datos.fechaNacimiento(), datos.pesoKg(), datos.observaciones());
        return MascotaResponse.desde(mascota);   // sin save();
    }

    @Transactional
    public void eliminar(Long id, Long clienteId) {
        buscarPropia(id, clienteId).desactivar();
    }

    private Mascota buscarPropia(Long id, Long clienteId) {
        return mascotaRepository.findByIdAndClienteIdAndActivoTrue(id, clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La mascota no existe"));
    }
}
