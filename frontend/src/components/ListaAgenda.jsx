import InsigniaReserva from './InsigniaReserva';

const ESPECIES = { PERRO: 'Perro', GATO: 'Gato', OTRO: 'Otro' };

const FECHA_LARGA = new Intl.DateTimeFormat('es-CO', {
  weekday: 'long', day: 'numeric', month: 'long',
});

// El backend envia 'YYYY-MM-DD'. new Date('2026-10-20') lo interpreta como UTC
// y en Colombia se corre un dia hacia atras, asi que la fecha se arma a mano.
function textoFecha(iso) {
  const [anio, mes, dia] = iso.split('-').map(Number);
  const texto = FECHA_LARGA.format(new Date(anio, mes - 1, dia));
  return texto.charAt(0).toUpperCase() + texto.slice(1);
}

function agruparPorFecha(reservas) {
  const grupos = new Map();
  for (const r of reservas) {
    if (!grupos.has(r.fecha)) grupos.set(r.fecha, []);
    grupos.get(r.fecha).push(r);
  }
  return [...grupos.entries()];
}

// `acciones` es opcional: recibe la reserva y devuelve los botones que la
// pantalla quiera poner debajo. Asi esta lista no sabe nada de HU-026.
export default function ListaAgenda({ reservas, acciones }) {
  if (reservas.length === 0) {
    return <p className="text-neutral-600">No tienes reservas en este rango.</p>;
  }

  return (
    <div className="space-y-6">
      {agruparPorFecha(reservas).map(([fecha, delDia]) => (
        <div key={fecha}>
          <h3 className="mb-2 text-sm font-bold uppercase tracking-wide text-neutral-500">
            {textoFecha(fecha)}
          </h3>
          <ul className="space-y-2">
            {delDia.map((r) => (
              <li key={r.id} className="rounded-lg border border-neutral-200 bg-white p-4">
                <div className="flex flex-wrap items-center gap-x-3 gap-y-1">
                  <span className="font-mono text-sm font-semibold text-neutral-900">
                    {r.horaInicio.slice(0, 5)} – {r.horaFin.slice(0, 5)}
                  </span>
                  <span className="font-semibold text-neutral-900">{r.servicio}</span>
                  <InsigniaReserva estado={r.estado} />
                </div>

                <p className="mt-2 text-neutral-800">
                  <span className="font-semibold">{r.mascota}</span>{' '}
                  <span className="text-neutral-600">
                    · {ESPECIES[r.especie] ?? r.especie}
                    {r.raza ? ` · ${r.raza}` : ''}
                  </span>
                </p>
                <p className="text-sm text-neutral-600">Cliente: {r.cliente}</p>

                {r.observaciones && (
                  <p className="mt-2 rounded border-l-2 border-amber-400 bg-amber-50 px-3 py-2 text-sm text-neutral-800">
                    {r.observaciones}
                  </p>
                )}

                {acciones && <div className="mt-3">{acciones(r)}</div>}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}
