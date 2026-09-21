package com.petcare.auth.controller;

import com.petcare.auth.dto.*;
import com.petcare.auth.service.AutenticacionService;
import com.petcare.auth.service.RegistroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegistroService registroService;
    private final AutenticacionService autenticacionService;

    public AuthController(RegistroService registroService,
                          AutenticacionService autenticacionService) {

        this.registroService = registroService;
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse registrar(@Valid @RequestBody RegistroClienteRequest peticion){
        return registroService.registrarCliente(peticion);
    }

    @PostMapping("/login")
    public TokenResponse iniciarSesion(@Valid @RequestBody LoginRequest peticion){
        return autenticacionService.iniciarSesion(peticion);
    }

    @PostMapping("/refresh")
    public TokenResponse renovar(@Valid @RequestBody RefreshRequest peticion){
        return autenticacionService.renovar(peticion.refreshToken());
    }

    @GetMapping("/yo")
    public Map<String, Object> yo(Authentication autenticacion) {
        return Map.of(
                "usuarioId", autenticacion.getPrincipal(),
                "permisos", autenticacion.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).sorted().toList()
        );
    }
}
