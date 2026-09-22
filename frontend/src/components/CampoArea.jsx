import { useId } from 'react';

// Campo de texto largo (observaciones). Muestra cuantos caracteres quedan.
export default function CampoArea({ etiqueta, error, maxLength = 500, className = '', value = '', ...resto }) {
  const id = useId();
  const idError = `${id}-error`;

  return (
    <div className={className}>
      <label htmlFor={id} className="mb-1 block text-sm font-medium text-neutral-800">
        {etiqueta}
      </label>
      <textarea
        id={id}
        rows={3}
        value={value}
        maxLength={maxLength}
        aria-invalid={error ? 'true' : undefined}
        aria-describedby={error ? idError : undefined}
        className={[
          'block w-full rounded-md border bg-white p-3 text-base text-neutral-900',
          'outline-none transition focus:border-teal-600 focus:ring-2 focus:ring-teal-600/30',
          error ? 'border-red-600' : 'border-neutral-400',
        ].join(' ')}
        {...resto}
      />
      <div className="mt-1 flex justify-between gap-4">
        {error ? <p id={idError} className="text-sm text-red-700">{error}</p> : <span />}
        <span className="text-xs text-neutral-500">{value.length}/{maxLength}</span>
      </div>
    </div>
  );
}
