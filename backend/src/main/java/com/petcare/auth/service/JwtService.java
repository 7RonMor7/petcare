package com.petcare.auth.service;

import com.petcare.usuarios.domain.Permiso;
import com.petcare.usuarios.domain.Rol;
import com.petcare.usuarios.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMinutos;

    public JwtService(
            @Value("${petcare.jwt.secreto}") String secreto,
            @Value("${petcare.jwt.expiracion-access-minutos}") long expiracionMinutos){

        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarAccessToken(Usuario usuario){

        Instant ahora = Instant.now();
        Instant expira = ahora.plus(expiracionMinutos, ChronoUnit.MINUTES);

        List<String> roles = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .toList();

        List<String> permisos = usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .map(Permiso::getNombre)
                .distinct()
                .toList();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(usuario.getId().toString())
                .claim("correo", usuario.getCorreo())
                .claim("roles", roles)
                .claim("permisos", permisos)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expira))
                .signWith(clave)
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long segundosDeVida() {
        return expiracionMinutos * 60;
    }
}
