const ESTILOS = {
  activo: 'bg-teal-100 text-teal-800',
  inactivo: 'bg-neutral-200 text-neutral-700',
};

export default function Insignia({ activo }) {
  return (
    <span className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${activo ? ESTILOS.activo : ESTILOS.inactivo}`}>
      {activo ? 'Activo' : 'Inactivo'}
    </span>
  );
}
