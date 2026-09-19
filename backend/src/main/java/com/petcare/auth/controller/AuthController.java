package com.petcare.auth.controller;

import com.petcare.auth.dto.*;
import com.petcare.auth.service.AutenticacionService;
import com.petcare.auth.service.RegistroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegistroService registroService;
    private final AutenticacionService autenticacionService;

    public AuthController(RegistroService registroService) {
        this.registroService = registroService;
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
        return autenticacionService.iniciarSesion(peticion.refreshToken());
    }
}
