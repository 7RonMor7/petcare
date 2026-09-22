// Traduce los errores de axios al formato RespuestaError del backend:
// { estado, codigo, mensaje, ruta, detalles: [{ campo, mensaje }] }

export function codigoError(error) {
  return error?.response?.data?.codigo;
}

// [{campo:'correo', mensaje:'...'}]  ->  { correo: '...' }
export function erroresPorCampo(error) {
  const detalles = error?.response?.data?.detalles ?? [];
  return Object.fromEntries(detalles.map((d) => [d.campo, d.mensaje]));
}

export function mensajeError(error, porDefecto = 'Ocurrió un error inesperado. Intenta de nuevo.') {
  if (!error?.response) return 'No se pudo conectar con el servidor. Revisa tu conexión.';
  return error.response.data?.mensaje ?? porDefecto;
}
