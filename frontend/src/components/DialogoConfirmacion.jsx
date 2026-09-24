import { useEffect } from 'react';

// Confirmacion para acciones destructivas. Se cierra con Escape y al pulsar
// fuera. El foco inicial va al boton seguro (Cancelar), no al peligroso.
export default function DialogoConfirmacion({
  abierto, titulo, mensaje, textoConfirmar = 'Eliminar',
  procesando = false, onConfirmar, onCancelar,
}) {
  useEffect(() => {
    if (!abierto) return;
    const alTeclear = (e) => { if (e.key === 'Escape') onCancelar(); };
    document.addEventListener('keydown', alTeclear);
    return () => document.removeEventListener('keydown', alTeclear);
  }, [abierto, onCancelar]);

  if (!abierto) return null;

  return (
    <div
      className="fixed inset-0 z-50 flex items-end justify-center bg-black/40 p-4 sm:items-center"
      onClick={onCancelar}
      role="presentation"
    >
      <div
        role="alertdialog"
        aria-modal="true"
        aria-labelledby="dialogo-titulo"
        className="w-full max-w-md rounded-lg bg-white p-5 shadow-lg"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 id="dialogo-titulo" className="text-lg font-bold text-neutral-900">{titulo}</h2>
        <p className="mt-2 text-neutral-700">{mensaje}</p>

        <div className="mt-6 flex flex-col gap-3 sm:flex-row-reverse">
          <button
            type="button"
            onClick={onConfirmar}
            disabled={procesando}
            className="min-h-12 rounded-md bg-red-700 px-4 font-semibold text-white hover:bg-red-800 disabled:opacity-60 sm:min-w-32"
          >
            {procesando ? 'Eliminando…' : textoConfirmar}
          </button>
          <button
            type="button"
            autoFocus
            onClick={onCancelar}
            className="min-h-12 rounded-md border border-neutral-300 px-4 font-semibold text-neutral-900 hover:bg-neutral-100 sm:min-w-32"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
