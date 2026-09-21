package com.petcare.mascotas.service;

import com.petcare.mascotas.domain.Mascota;
import com.petcare.mascotas.dto.MascotaRequest;
import com.petcare.mascotas.dto.MascotaResponse;
import com.petcare.mascotas.repository.MascotaRepository;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
