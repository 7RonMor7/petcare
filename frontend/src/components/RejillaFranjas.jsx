// Franjas disponibles agrupadas en mañana y tarde. Cada franja es un boton:
// al pulsarlo, la pagina decide que hacer (hoy, mostrar la asignacion; en el
// Sprint 5, crear la reserva).
const esManana = (hora) => Number(hora.slice(0, 2)) < 12;

function Grupo({ titulo, franjas, seleccion, onElegir }) {
  if (franjas.length === 0) return null;
  return (
    <div>
      <h3 className="mb-2 text-sm font-semibold text-neutral-700">{titulo}</h3>
      <ul className="flex flex-wrap gap-2">
        {franjas.map((f) => {
          const activa = seleccion === f.horaInicio;
          return (
            <li key={f.horaInicio}>
              <button
                type="button"
                onClick={() => onElegir(f)}
                aria-pressed={activa}
                className={[
                  'min-h-11 rounded-md border px-3 text-sm font-semibold transition',
                  activa
                    ? 'border-neutral-900 bg-neutral-900 text-white'
                    : 'border-neutral-300 bg-white text-neutral-900 hover:border-neutral-900',
                ].join(' ')}
                title={`${f.horaInicio.slice(0, 5)} a ${f.horaFin.slice(0, 5)} · ${f.empleadoIds.length} disponible(s)`}
              >
                {f.horaInicio.slice(0, 5)}
              </button>
            </li>
          );
        })}
      </ul>
    </div>
  );
}

export default function RejillaFranjas({ franjas = [], seleccion, onElegir }) {
  if (franjas.length === 0) {
    return (
      <p className="rounded-lg border border-dashed border-neutral-300 p-8 text-center text-neutral-600">
        No hay horarios disponibles para esa fecha.
      </p>
    );
  }

  return (
    <div className="space-y-5">
      <Grupo titulo="Mañana" franjas={franjas.filter((f) => esManana(f.horaInicio))}
        seleccion={seleccion} onElegir={onElegir} />
      <Grupo titulo="Tarde" franjas={franjas.filter((f) => !esManana(f.horaInicio))}
        seleccion={seleccion} onElegir={onElegir} />
    </div>
  );
}
