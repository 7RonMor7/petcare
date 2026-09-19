-- ---------------------------------------------------------------------------
-- V3 — Tokens de refresco                                  (HU-008, IRR-01)
--
-- El access token NO se guarda aqui ni en ningun sitio: es autocontenido,
-- viaja firmado y el servidor lo valida con la clave, sin consultar nada.
-- Esa es la gracia de JWT y tambien su limitacion: no se puede revocar.
--
-- El refresh token si se guarda, porque necesitamos poder invalidarlo
-- (cierre de sesion, deteccion de robo). Y se guarda HASHEADO: si alguien
-- se lleva esta tabla, no obtiene tokens utilizables.
-- ---------------------------------------------------------------------------

CREATE TABLE refresh_token (
    id         BIGINT    NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT    NOT NULL,

    -- SHA-256 en hexadecimal: siempre 64 caracteres.
    -- Aqui SHA-256 basta y BCrypt seria un error. El token es una cadena
    -- aleatoria de 256 bits: no hay diccionario que probar, asi que no
    -- necesitamos un hash lento. Y como se verifica en cada renovacion,
    -- gastar 100 ms de BCrypt en cada una si seria un problema real.
    token_hash CHAR(64)  NOT NULL,

    -- Todos los tokens que descienden de un mismo login comparten familia.
    -- Sirve para la deteccion de reuso: si aparece un token ya consumido,
    -- revocamos la familia entera de un golpe.
    familia_id CHAR(36)  NOT NULL,

    expira_en  TIMESTAMP NOT NULL,
    revocado   BOOLEAN   NOT NULL DEFAULT FALSE,
    creado_en  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uk_refresh_token_hash (token_hash),
    KEY idx_refresh_token_familia (familia_id),
    KEY idx_refresh_token_usuario (usuario_id),
    CONSTRAINT fk_refresh_token_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
