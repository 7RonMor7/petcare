package com.petcare.usuarios.repository;

import com.petcare.usuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreoIgnoreCase(String correo);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    List<Usuario> findByRoles_NombreOrderByApellidoAscNombreAsc(String nombre);
}
