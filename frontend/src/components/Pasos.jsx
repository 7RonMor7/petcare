// Indicador de progreso del asistente de reserva.
export default function Pasos({ pasos, actual }) {
  return (
    <ol className="flex flex-wrap items-center gap-x-2 gap-y-1 text-sm">
      {pasos.map((texto, i) => {
        const estado = i < actual ? 'hecho' : i === actual ? 'activo' : 'pendiente';
        return (
          <li key={texto} className="flex items-center gap-2">
            <span
              className={[
                'flex h-6 w-6 shrink-0 items-center justify-center rounded-full text-xs font-bold',
                estado === 'hecho' ? 'bg-teal-600 text-white'
                  : estado === 'activo' ? 'bg-neutral-900 text-white'
                  : 'bg-neutral-200 text-neutral-600',
              ].join(' ')}
              aria-current={estado === 'activo' ? 'step' : undefined}
            >
              {estado === 'hecho' ? '✓' : i + 1}
            </span>
            <span className={estado === 'pendiente' ? 'text-neutral-500' : 'font-medium text-neutral-900'}>
              {texto}
            </span>
            {i < pasos.length - 1 && <span className="text-neutral-300" aria-hidden="true">—</span>}
          </li>
        );
      })}
    </ol>
  );
}
