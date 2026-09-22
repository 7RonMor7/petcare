import { useId, useState } from 'react';

// Campo de formulario accesible: etiqueta asociada, mensaje de error
// enlazado con aria-describedby y boton para mostrar la contrasena.
// text-base (16 px) evita que iOS haga zoom al enfocar el campo.

export default function CampoTexto({ etiqueta, error, tipo = 'text', className = '', ...resto }) {
  const id = useId();
  const idError = `${id}-error`;
  const [mostrar, setMostrar] = useState(false);
  const esClave = tipo === 'password';

  return (
    <div className={className}>
      <label htmlFor={id} className="mb-1 block text-sm font-medium text-neutral-800">
        {etiqueta}
      </label>

      <div className="relative">
        <input
          id={id}
          type={esClave && mostrar ? 'text' : tipo}
          aria-invalid={error ? 'true' : undefined}
          aria-describedby={error ? idError : undefined}
          className={[
            'block min-h-12 w-full rounded-md border bg-white px-3 text-base text-neutral-900',
            'outline-none transition placeholder:text-neutral-400',
            'focus:border-teal-600 focus:ring-2 focus:ring-teal-600/30',
            'disabled:cursor-not-allowed disabled:bg-neutral-100',
            error ? 'border-red-600' : 'border-neutral-400',
            esClave ? 'pr-20' : '',
          ].join(' ')}
          {...resto}
        />

        {esClave && (
          <button
            type="button"
            onClick={() => setMostrar((v) => !v)}
            className="absolute inset-y-0 right-0 px-3 text-sm font-medium text-neutral-600 hover:text-neutral-900"
            aria-label={mostrar ? 'Ocultar contraseña' : 'Mostrar contraseña'}
          >
            {mostrar ? 'Ocultar' : 'Mostrar'}
          </button>
        )}
      </div>

      {error && (
        <p id={idError} className="mt-1 text-sm text-red-700">
          {error}
        </p>
      )}
    </div>
  );
}
