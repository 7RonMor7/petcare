import { useId } from 'react';

// Igual que CampoTexto pero con una lista de opciones.
// opciones = [{ valor: 'PERRO', texto: 'Perro' }, ...]
export default function CampoSelect({ etiqueta, error, opciones = [], className = '', ...resto }) {
  const id = useId();
  const idError = `${id}-error`;

  return (
    <div className={className}>
      <label htmlFor={id} className="mb-1 block text-sm font-medium text-neutral-800">
        {etiqueta}
      </label>
      <select
        id={id}
        aria-invalid={error ? 'true' : undefined}
        aria-describedby={error ? idError : undefined}
        className={[
          'block min-h-12 w-full rounded-md border bg-white px-3 text-base text-neutral-900',
          'outline-none transition focus:border-teal-600 focus:ring-2 focus:ring-teal-600/30',
          error ? 'border-red-600' : 'border-neutral-400',
        ].join(' ')}
        {...resto}
      >
        <option value="">Selecciona…</option>
        {opciones.map((o) => (
          <option key={o.valor} value={o.valor}>{o.texto}</option>
        ))}
      </select>
      {error && <p id={idError} className="mt-1 text-sm text-red-700">{error}</p>}
    </div>
  );
}
