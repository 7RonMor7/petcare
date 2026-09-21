package com.petcare.auth.service;

import com.petcare.auth.domain.RefreshToken;
import com.petcare.auth.dto.LoginRequest;
import com.petcare.auth.dto.TokenResponse;
import com.petcare.auth.dto.UsuarioResponse;
import com.petcare.common.error.CredencialesInvalidasException;
import com.petcare.common.error.TokenInvalidoException;
import com.petcare.usuarios.domain.Rol;
import com.petcare.usuarios.domain.Usuario;
import com.petcare.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import  java.util.stream.Collectors;

@Service
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder codificador;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AutenticacionService(UsuarioRepository usuarioRepository,
                                PasswordEncoder codificador,
                                JwtService jwtService,
                                RefreshTokenService refreshTokenService) {
        this.usuarioRepository = usuarioRepository;
        this.codificador = codificador;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public TokenResponse iniciarSesion(LoginRequest peticion) {

        Usuario usuario = usuarioRepository
                .findByCorreoIgnoreCase(peticion.correo().trim())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!codificador.matches(peticion.contrasena(), usuario.getContrasenaHash())){
            throw new CredencialesInvalidasException();
        }

        if (!usuario.isActivo()){
            throw new CredencialesInvalidasException();
        }

        return construirRespuesta(usuario, UUID.randomUUID().toString());
    }

    @Transactional(noRollbackFor = TokenInvalidoException.class)
    public TokenResponse renovar(String refreshTokenCrudo) {
        RefreshToken consumido = refreshTokenService.consumir(refreshTokenCrudo);
        Usuario usuario = consumido.getUsuario();

        return construirRespuesta(usuario, consumido.getFamiliaId());
    }

    private TokenResponse construirRespuesta(Usuario usuario, String familiaId){
        String accessToken = jwtService.generarAccessToken(usuario);
        String refreshToken = refreshTokenService.emitir(usuario, familiaId);

        Set<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());

        UsuarioResponse datos = new UsuarioResponse(
                usuario.getId(), usuario.getNombre(), usuario.getApellido(),
                usuario.getCorreo(), roles);

        return new TokenResponse(accessToken, refreshToken, jwtService.segundosDeVida(), datos);
    }

    public void cerrarSesion(String refreshToken, Long usuarioId) {
        refreshTokenService.cerrarSesion(refreshToken, usuarioId);
    }
}
