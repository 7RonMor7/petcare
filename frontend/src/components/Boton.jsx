// Boton de ancho completo con estado de carga. Mientras carga queda
// deshabilitado: evita el doble clic que enviaria dos logins o dos registros.

const VARIANTES = {
  primario: 'bg-neutral-900 text-white hover:bg-neutral-700',
  secundario: 'border border-neutral-900 bg-white text-neutral-900 hover:bg-neutral-100',
};

export default function Boton({
  children, cargando = false, variante = 'primario', type = 'submit', disabled, className = '', ...resto
}) {
  return (
    <button
      type={type}
      disabled={disabled || cargando}
      aria-busy={cargando || undefined}
      className={[
        'inline-flex min-h-12 w-full items-center justify-center gap-2 rounded-md px-4',
        'text-base font-semibold transition',
        'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-teal-600 focus-visible:ring-offset-2',
        'disabled:cursor-not-allowed disabled:opacity-60',
        VARIANTES[variante],
        className,
      ].join(' ')}
      {...resto}
    >
      {cargando && (
        <span className="h-4 w-4 animate-spin rounded-full border-2 border-current border-t-transparent" aria-hidden="true" />
      )}
      {children}
    </button>
  );
}
