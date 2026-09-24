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

/**
 * Reparte los errores de validacion entre los campos del formulario y el
 * resto. Hace falta porque las validaciones que cruzan dos campos
 * (@AssertTrue) llegan con el nombre del metodo, p. ej.
 * "duracionCoherenteConLaUnidad", que no corresponde a ningun campo: sin esto
 * el mensaje no se mostraria en ninguna parte.
 */
export function separarErrores(error, camposDelFormulario) {
  const todos = erroresPorCampo(error);
  const campos = {};
  const sueltos = [];

  for (const [campo, mensaje] of Object.entries(todos)) {
    if (camposDelFormulario.includes(campo)) campos[campo] = mensaje;
    else sueltos.push(mensaje);
  }

  return {
    campos,
    general: sueltos.length ? sueltos.join(' ') : (Object.keys(campos).length ? '' : mensajeError(error)),
  };
}
