import axios from 'axios'

const URL_API = import.meta.env.VITE_API_URL;
const CLAVE_REFRESH = 'petcare.refresh';
const RUTAS_SIN_RENOVACION = ['/auth/login', '/auth/registro', '/auth/refresh'];

let accessToken = null;            // solo en memoria
let renovacionEnCurso = null;      // promesa compartida 
let alExpirarSesion = () => {};    // lo registrará el AuthContext

export const cliente = axios.create({
  baseURL: URL_API,
  headers: {'Content-Type': 'application/json'},
});

// ---- Gestión de tokens ----
export function guardarTokens({ accessToken: access, refreshToken }) {
  accessToken = access;
  try { localStorage.setItem(CLAVE_REFRESH, refreshToken); } catch { /* modo privado */ }
}

export function limpiarTokens() {
  accessToken = null;
  try { localStorage.removeItem(CLAVE_REFRESH); } catch { /* nada */ }
}

export function obtenerRefresh() {
  try { return localStorage.getItem(CLAVE_REFRESH); } catch { return null; }
}

export function registrarAlExpirarSesion(fn) {
  alExpirarSesion = fn;
}

// ---- Petición: añade el token ----
cliente.interceptors.request.use((config) => {
  if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
  return config;
});

// ---- Respuesta: si hay error 401, renueva una vez y reintenta ----
async function renovar() {
  const refreshToken = obtenerRefresh();
  if (!refreshToken) throw new Error('Sin refresh token');
  // axios "puro", no `cliente`: así esta llamada no pasa por el interceptor
  const { data } = await axios.post(`${URL_API}/auth/refresh`, { refreshToken });
  guardarTokens(data);
  return data.accessToken;
}

cliente.interceptors.response.use(
  (respuesta) => respuesta,
  async (error) => {
    const original = error.config;
    const esRutaAuth = RUTAS_SIN_RENOVACION.some((r) => original?.url?.includes(r));

    if (error.response?.status !== 401 || original._reintendado || esRutaAuth) {
      return Promise.reject(error);
    }
    original._reintendado = true;

    try {
      renovacionEnCurso ??= renovar().finally(() => { renovacionEnCurso = null; });
      const nuevoToken = await renovacionEnCurso;
      original.headers.Authorization = `Bearer ${nuevoToken}`;
      return cliente(original);
    } catch {
      limpiarTokens();
      alExpirarSesion();
      return Promise.reject(error);
    }
  }
);

export default cliente;