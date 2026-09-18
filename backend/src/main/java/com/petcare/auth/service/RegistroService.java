package com.petcare.auth.service;

import com.petcare.auth.dto.RegistroClienteRequest;
import com.petcare.auth.dto.UsuarioResponse;
import com.petcare.common.error.CorreoYaRegistradoException;
import com.petcare.usuarios.domain.Rol;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.RolRepository;
import com.petcare.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistroService {

    private static final String ROL_CLIENTE = "CLIENTE";
    private static final String VERSION_POLITICA = "1.0";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder codificador;

    public RegistroService(UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           PasswordEncoder codificador) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.codificador = codificador;
    }

    @Transactional
    public UsuarioResponse registrarCliente(RegistroClienteRequest peticion){

        String correo = peticion.correo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreoIgnoreCase(correo)){
            throw new CorreoYaRegistradoException(correo);
        }

        Rol rolCliente = rolRepository.findByNombre(ROL_CLIENTE)
                .orElseThrow(() -> new IllegalStateException(
                        "El rol CLIENTE no existe. ¿Se aplicó la migración V?"));

        Usuario usuario = new Usuario(
                peticion.nombre().trim(),
                peticion.apellido().trim(),
                correo,
                peticion.telefono(),
                codificador.encode(peticion.contrasena()),
                VERSION_POLITICA
        );
        usuario.asignarRol(rolCliente);

        Usuario guardado = usuarioRepository.save(usuario);

        Set<String> nombresRoles = guardado.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());

        return new UsuarioResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getCorreo(),
                nombresRoles
        );
    }
}
