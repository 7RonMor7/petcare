import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import {
  cliente, guardarTokens, limpiarTokens, obtenerRefresh, registrarAlExpirarSesion,
} from '../api/cliente';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null);
  const [cargando, setCargando] = useState(true);

  // Al arrancar: ¿hay una sesión que recuperar?
  useEffect(() => {
    registrarAlExpirarSesion(() => setUsuario(null));

    if (!obtenerRefresh()) {
      setCargando(false);
      return;
    }
    // Sin access token → 401 → el interceptor renueva → reintenta → 200
    cliente.get('/auth/yo')
      .then(({ data }) => setUsuario(data))
      .catch(() => setUsuario(null))
      .finally(() => setCargando(false));
  }, []);

  const iniciarSesion = useCallback(async (correo, contrasena) => {
    const { data } = await cliente.post('/auth/login', { correo, contrasena });
    guardarTokens(data);
    const { data: yo } = await cliente.get('/auth/yo');
    setUsuario(yo);
    return yo;
  }, []);

  const registrar = useCallback(async (datos) => {
    const { data } = await cliente.post('/auth/registro', datos);
    return data;
  }, []);

  const cerrarSesion = useCallback(async () => {
    try {
      const refreshToken = obtenerRefresh();
      if (refreshToken) await cliente.post('/auth/logout', { refreshToken });
    } catch {
      // Si el backend falla, igual cerramos la sesión en este navegador
    } finally {
      limpiarTokens();
      setUsuario(null);
    }
  }, []);

  const tienePermiso = useCallback(
    (permiso) => usuario?.permisos?.includes(permiso) ?? false,
    [usuario]
  );

  const valor = useMemo(() => ({
    usuario,
    cargando,
    estaAutenticado: usuario !== null,
    iniciarSesion,
    registrar,
    cerrarSesion,
    tienePermiso,
  }), [usuario, cargando, iniciarSesion, registrar, cerrarSesion, tienePermiso]);

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const contexto = useContext(AuthContext);
  if (!contexto) throw new Error('useAuth debe usarse dentro de <AuthProvider>');
  return contexto;
}