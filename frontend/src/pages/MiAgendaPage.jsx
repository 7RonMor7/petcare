import { useEffect, useState } from 'react';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { DIAS } from '../components/EditorJornada';
import CampoTexto from '../components/CampoTexto';
import Boton from '../components/Boton';
import Alerta from '../components/Alerta';

const VACIO = { fecha: '', horaInicio: '08:00', horaFin: '09:00', motivo: '' };

export default function MiAgendaPage() {
  const [jornada, setJornada] = useState([]);
  const [bloqueos, setBloqueos] = useState([]);
  const [form, setForm] = useState(VACIO);
  const [cargando, setCargando] = useState(true);
  const [enviando, setEnviando] = useState(false);
  const [errorGeneral, setErrorGeneral] = useState('');

  const hoy = new Date().toLocaleDateString('sv');
  const dentroDeUnMes = new Date(Date.now() + 30 * 86400000).toLocaleDateString('sv');

  async function cargarBloqueos() {
    const { data } = await cliente.get('/mi-agenda/bloqueos', {
        params: { desde: hoy, hasta: dentroDeUnMes },
    });
    setBloqueos(data);
  }

  useEffect(() => {
    Promise.all([cliente.get('/mi-agenda/jornada'), cargarBloqueos()])
      .then(([resJornada]) => setJornada(resJornada.data))
      .catch((error) => setErrorGeneral(mensajeError(error)))
      .finally(() => setCargando(false));
  }, []);

  const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  async function bloquear(e) {
    e.preventDefault();
    setErrorGeneral('');
    setEnviando(true);
    try {
      await cliente.post('/mi-agenda/bloqueos', { ...form, motivo: form.motivo.trim() || null });
      setForm(VACIO);
      await cargarBloqueos();
    } catch (error) {
      setErrorGeneral(mensajeError(error));
    } finally {
      setEnviando(false);
    }
  }

  async function eliminar(id) {
    setErrorGeneral('');
    try {
      await cliente.delete(`/mi-agenda/bloqueos/${id}`);
      setBloqueos((actuales) => actuales.filter((b) => b.id !== id));
    } catch (error) {
      setErrorGeneral(mensajeError(error));
    }
  }

  const nombreDia = (v) => DIAS.find((d) => d.valor === v)?.texto ?? v;

  if (cargando) return <p className="text-neutral-600">Cargando tu agenda…</p>;

  return (
    <div className="mx-auto max-w-3xl">
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Mi agenda</h1>

      <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

      <section>
        <h2 className="mb-3 text-lg font-bold text-neutral-900">Mi jornada</h2>
        {jornada.length === 0 ? (
          <p className="text-neutral-600">No tienes jornada asignada. Habla con administración.</p>
        ) : (
          <ul className="space-y-1 text-neutral-800">
            {jornada.map((t) => (
              <li key={t.id}>
                <span className="font-semibold">{nombreDia(t.diaSemana)}</span>{' '}
                {t.horaInicio.slice(0, 5)} – {t.horaFin.slice(0, 5)}
              </li>
            ))}
          </ul>
        )}
      </section>

      <section className="mt-10">
        <h2 className="mb-3 text-lg font-bold text-neutral-900">Bloquear un horario</h2>
        <form onSubmit={bloquear} noValidate className="space-y-5 rounded-lg border border-neutral-200 bg-white p-5">
          <div className="grid gap-5 sm:grid-cols-3">
            <CampoTexto etiqueta="Fecha" name="fecha" tipo="date" min={hoy}
              value={form.fecha} onChange={cambiar} />
            <CampoTexto etiqueta="Desde" name="horaInicio" tipo="time" step="1800"
              value={form.horaInicio} onChange={cambiar} />
            <CampoTexto etiqueta="Hasta" name="horaFin" tipo="time" step="1800"
              value={form.horaFin} onChange={cambiar} />
          </div>
          <CampoTexto etiqueta="Motivo (opcional)" name="motivo"
            value={form.motivo} onChange={cambiar} />
          <div className="sm:w-56"><Boton cargando={enviando}>Bloquear</Boton></div>
        </form>
      </section>

      <section className="mt-10">
        <h2 className="mb-3 text-lg font-bold text-neutral-900">Bloqueos próximos</h2>
        {bloqueos.length === 0 ? (
          <p className="text-neutral-600">Sin bloqueos en los próximos 30 días.</p>
        ) : (
          <ul className="space-y-2">
            {bloqueos.map((b) => (
              <li key={b.id} className="flex items-center gap-4 rounded-lg border border-neutral-200 bg-white p-4">
                <div className="flex-1">
                  <p className="font-semibold text-neutral-900">
                    {b.fecha} · {b.horaInicio.slice(0, 5)} – {b.horaFin.slice(0, 5)}
                  </p>
                  {b.motivo && <p className="text-sm text-neutral-600">{b.motivo}</p>}
                </div>
                <button onClick={() => eliminar(b.id)} className="text-sm font-semibold text-red-700 underline">
                  Quitar
                </button>
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}