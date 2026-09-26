import { useState } from 'react';
import Boton from './Boton';
import { formatearPrecio, textoUnidad } from '../api/formato';

/**
 * Casillas con el catalogo activo. Guarda el conjunto completo de ids, porque
 * el endpoint tambien es un reemplazo.
 */
export default function SelectorServicios({
  catalogo = [], seleccionInicial = [], guardando = false, onGuardar,
}) {
  const [seleccion, setSeleccion] = useState(() => new Set(seleccionInicial));

  function alternar(id) {
    const copia = new Set(seleccion);
    if (copia.has(id)) copia.delete(id);
    else copia.add(id);
    setSeleccion(copia);
  }

  if (catalogo.length === 0) {
    return <p className="text-neutral-600">No hay servicios activos en el catálogo.</p>;
  }

  return (
    <div className="space-y-3">
      <ul className="space-y-2">
        {catalogo.map((s) => (
          <li key={s.id}>
            <label className="flex items-start gap-3 rounded-lg border border-neutral-200 bg-white p-4">
              <input
                type="checkbox"
                checked={seleccion.has(s.id)}
                onChange={() => alternar(s.id)}
                className="mt-1 h-5 w-5 accent-teal-600"
              />
              <span>
                <span className="font-semibold text-neutral-900">{s.nombre}</span>
                <span className="block text-sm text-neutral-600">
                  {formatearPrecio(s.precio)} {textoUnidad(s.unidadCobro)}
                </span>
              </span>
            </label>
          </li>
        ))}
      </ul>

      <div className="sm:w-56">
        <Boton type="button" cargando={guardando} onClick={() => onGuardar([...seleccion])}>
          Guardar servicios
        </Boton>
      </div>
    </div>
  );
}
