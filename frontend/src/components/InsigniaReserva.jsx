const ESTILOS = {
  PENDIENTE_PAGO: 'bg-amber-100 text-amber-800',
  CONFIRMADA:     'bg-teal-100 text-teal-800',
  EN_PROCESO:     'bg-blue-100 text-blue-800',
  COMPLETADA:     'bg-neutral-200 text-neutral-700',
  CANCELADA:      'bg-red-100 text-red-800',
  EXPIRADA:       'bg-neutral-200 text-neutral-600',
  NO_ASISTIO:     'bg-red-100 text-red-800',
};

const TEXTOS = {
  PENDIENTE_PAGO: 'Pendiente de pago',
  CONFIRMADA: 'Confirmada',
  EN_PROCESO: 'En proceso',
  COMPLETADA: 'Completada',
  CANCELADA: 'Cancelada',
  EXPIRADA: 'Expirada',
  NO_ASISTIO: 'No asistió',
};

export default function InsigniaReserva({ estado }) {
  return (
    <span className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${ESTILOS[estado] ?? ESTILOS.COMPLETADA}`}>
      {TEXTOS[estado] ?? estado}
    </span>
  );
}
