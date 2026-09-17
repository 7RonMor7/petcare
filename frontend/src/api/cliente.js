import axios from 'axios'

/**
 * Cliente HTTP unico de la aplicacion.
 *
 * Todo el frontend habla con el backend a traves de este objeto, nunca
 * llamando a axios directamente. Asi, cuando en el Sprint 1 anadamos el token
 * JWT, lo hacemos en un solo sitio y no en cada pantalla.
 */
const cliente = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api/v1',
  headers: { 'Content-Type': 'application/json' },
})

export default cliente
