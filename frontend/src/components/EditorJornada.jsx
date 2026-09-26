import { useState } from 'react';
import Boton from './Boton';

// Los siete dias, con el nombre que entiende el backend (java.time.DayOfWeek)
// y el que lee el usuario.
export const DIAS = [
  { valor: 'MONDAY', texto: 'Lunes' },
  { valor: 'TUESDAY', texto: 'Martes' },
  { valor: 'WEDNESDAY', texto: 'Miércoles' },
  { valor: 'THURSDAY', texto: 'Jueves' },
  { valor: 'FRIDAY', texto: 'Viernes' },
  { valor: 'SATURDAY', texto: 'Sábado' },
  { valor: 'SUNDAY', texto: 'Domingo' },
];

// El backend devuelve "08:00:00"; <input type="time"> necesita "08:00".
const hhmm = (h) => (h ?? '').slice(0, 5);

/**
 * Edita la semana completa y la entrega en una sola lista, porque el endpoint
 * es un reemplazo (PUT). Cada dia puede tener varios tramos (p. ej. manana y
 * tarde, con el almuerzo en medio).
 */
export default function EditorJornada({ valorInicial = [], guardando = false, onGuardar }) {
  const [porDia, setPorDia] = useState(() => {
    const inicial = Object.fromEntries(DIAS.map((d) => [d.valor, []]));
    for (const t of valorInicial) {
      inicial[t.diaSemana]?.push({ horaInicio: hhmm(t.horaInicio), horaFin: hhmm(t.horaFin) });
    }
    return inicial;
  });

  const anadir = (dia) =>
    setPorDia({ ...porDia, [dia]: [...porDia[dia], { horaInicio: '08:00', horaFin: '12:00' }] });

  const quitar = (dia, i) =>
    setPorDia({ ...porDia, [dia]: porDia[dia].filter((_, j) => j !== i) });

  const cambiar = (dia, i, campo, valor) =>
    setPorDia({
      ...porDia,
      [dia]: porDia[dia].map((t, j) => (j === i ? { ...t, [campo]: valor } : t)),
    });

  function guardar() {
    const tramos = DIAS.flatMap((d) =>
      porDia[d.valor].map((t) => ({
        diaSemana: d.valor,
        horaInicio: t.horaInicio,
        horaFin: t.horaFin,
      }))
    );
    onGuardar(tramos);
  }

  return (
    <div className="space-y-3">
      {DIAS.map((d) => (
        <div key={d.valor} className="rounded-lg border border-neutral-200 bg-white p-4">
          <div className="flex items-center justify-between gap-3">
            <h3 className="font-semibold text-neutral-900">{d.texto}</h3>
            <button type="button" onClick={() => anadir(d.valor)}
              className="rounded-md border border-neutral-300 px-3 py-1.5 text-sm font-semibold hover:bg-neutral-100">
              Añadir tramo
            </button>
          </div>

          {porDia[d.valor].length === 0 ? (
            <p className="mt-2 text-sm text-neutral-500">No trabaja</p>
          ) : (
            <ul className="mt-3 space-y-2">
              {porDia[d.valor].map((t, i) => (
                <li key={i} className="flex flex-wrap items-center gap-2">
                  <input type="time" step="1800" value={t.horaInicio}
                    onChange={(e) => cambiar(d.valor, i, 'horaInicio', e.target.value)}
                    className="min-h-11 rounded-md border border-neutral-400 px-2 text-base" />
                  <span className="text-neutral-500">a</span>
                  <input type="time" step="1800" value={t.horaFin}
                    onChange={(e) => cambiar(d.valor, i, 'horaFin', e.target.value)}
                    className="min-h-11 rounded-md border border-neutral-400 px-2 text-base" />
                  <button type="button" onClick={() => quitar(d.valor, i)}
                    className="ml-auto text-sm font-semibold text-red-700 underline">
                    Quitar
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      ))}

      <div className="sm:w-56">
        <Boton type="button" cargando={guardando} onClick={guardar}>Guardar jornada</Boton>
      </div>
    </div>
  );
}
