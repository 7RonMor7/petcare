package com.petcare.auth.service;

import com.petcare.auth.domain.RefreshToken;
import com.petcare.auth.repository.RefreshTokenRepository;
import com.petcare.common.error.TokenInvalidoException;
import com.petcare.usuarios.domain.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repositorio;
    private final long expiracionDias;
    private final SecureRandom aleatorio = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository repositorio,
                               @Value("${petcare.jwt.expiracion-refresh-dias}") long expiracionDias) {
        this.repositorio = repositorio;
        this.expiracionDias = expiracionDias;
    }

    @Transactional
    public String emitir(Usuario usuario, String familiaId){
        String tokenCrudo = generarTokenAleatorio();

        RefreshToken token = new RefreshToken(
                usuario,
                hashear(tokenCrudo),
                familiaId,
                Instant.now().plus(expiracionDias, ChronoUnit.DAYS)
        );
        repositorio.save(token);

        return tokenCrudo;
    }

    @Transactional
    public RefreshToken consumir(String tokenCrudo) {
        RefreshToken token = repositorio.findByTokenHash(hashear(tokenCrudo))
                .orElseThrow(TokenInvalidoException::new);

        if (token.isRevocado()) {
            // Alguien esta usando un token ya consumido. O es un cliente con un
            // fallo, o alguien robo el token. En ambos casos la respuesta segura
            // es la misma: invalidar toda la cadena y obligar a iniciar sesion.
            repositorio.revocarFamilia(token.getFamiliaId());
            throw new TokenInvalidoException();
        }

        if (!token.esUtilizable()) {
            throw new TokenInvalidoException();
        }

        token.revocar();
        return token;
    }

    @Transactional
    public void revocarFamilia(String familiaId) {
        repositorio.revocarFamilia(familiaId);
    }

    private String generarTokenAleatorio() {
        byte[] bytes = new byte[32];
        aleatorio.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashear(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(valor.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en esta JVM", e);
        }
    }

}
