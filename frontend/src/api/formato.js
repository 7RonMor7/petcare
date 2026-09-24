// Formato de presentacion. El backend envia numeros; aqui se convierten a
// texto para el usuario colombiano.

const PESOS = new Intl.NumberFormat('es-CO', {
  style: 'currency', currency: 'COP', maximumFractionDigits: 0,
});

export const formatearPrecio = (valor) => PESOS.format(Number(valor ?? 0));

export const textoUnidad = (u) => (u === 'POR_DIA' ? 'por noche' : 'por servicio');

export function textoDuracion(minutos) {
  if (!minutos) return null;
  const horas = Math.floor(minutos / 60);
  const resto = minutos % 60;
  if (horas === 0) return `${resto} min`;
  return resto === 0 ? `${horas} h` : `${horas} h ${resto} min`;
}
