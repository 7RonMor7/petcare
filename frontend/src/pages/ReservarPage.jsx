import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { formatearPrecio, textoDuracion } from '../api/formato';
import Pasos from '../components/Pasos';
import CampoSelect from '../components/CampoSelect';
import CampoTexto from '../components/CampoTexto';
import RejillaFranjas from '../components/RejillaFranjas';
import Boton from '../components/Boton';
import Alerta from '../components/Alerta';

const PASOS = ['Servicio', 'Mascota', 'Fecha y hora', 'Resumen'];

export default function ReservarPage() {
  const [paso, setPaso] = useState(0);

  const [servicios, setServicios] = useState([]);
  const [mascotas, setMascotas] = useState([]);
  const [empleados, setEmpleados] = useState([]);

  const [servicioId, setServicioId] = useState('');
  const [mascotaId, setMascotaId] = useState('');
  const [empleadoId, setEmpleadoId] = useState('');
  const [fecha, setFecha] = useState('');
  const [franja, setFranja] = useState(null);

  const [franjas, setFranjas] = useState([]);
  const [cargando, setCargando] = useState(false);
  const [enviando, setEnviando] = useState(false);
  const [errorGeneral, setErrorGeneral] = useState('');

  const hoy = new Date().toLocaleDateString('sv');
  const servicio = servicios.find((s) => String(s.id) === servicioId);
  const mascota = mascotas.find((m) => String(m.id) === mascotaId);

  useEffect(() => {
    Promise.all([cliente.get('/servicios'), cliente.get('/mascotas')])
      .then(([resServicios, resMascotas]) => {
        setServicios(resServicios.data);
        setMascotas(resMascotas.data);
      })
      .catch((error) => setErrorGeneral(mensajeError(error)));
  }, []);

  useEffect(() => {
    setEmpleadoId('');
    if (!servicioId) return setEmpleados([]);
    cliente.get(`/servicios/${servicioId}/empleados`)
      .then(({ data }) => setEmpleados(data))
      .catch(() => setEmpleados([]));
  }, [servicioId]);

  useEffect(() => {
    setFranja(null);
    setFranjas([]);
    if (!servicioId || !fecha) return;

    let vigente = true;
    setCargando(true);
    cliente.get('/disponibilidad', {
      params: { servicioId, fecha, ...(empleadoId ? { empleadoId } : {}) },
    })
      .then(({ data }) => { if (vigente) setFranjas(data); })
      .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
      .finally(() => { if (vigente) setCargando(false); });
    return () => { vigente = false; };
  }, [servicioId, fecha, empleadoId]);

  async function confirmar() {
    setEnviando(true);
    setErrorGeneral('');
    try {
      const { data: reserva } = await cliente.post('/reservas', {
        mascotaId: Number(mascotaId),
        servicioId: Number(servicioId),
        fecha,
        horaInicio: franja.horaInicio,
        empleadoId: empleadoId ? Number(empleadoId) : null,
      });

      const { data: checkout } = await cliente.post(`/reservas/${reserva.id}/pago/checkout`);
      window.location.assign(checkout.urlCheckout);      // adiós, nos vamos a la pasarela
    } catch (error) {
      setErrorGeneral(mensajeError(error));
      setEnviando(false);
      if (error.response?.status === 409) setPaso(2);    // la franja se ocupó: vuelve a elegir hora
    }
  }

  const puedeAvanzar = [Boolean(servicioId), Boolean(mascotaId), Boolean(franja), true][paso];

  return (
    <div className="mx-auto max-w-2xl">
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Reservar un servicio</h1>
      <div className="mt-4"><Pasos pasos={PASOS} actual={paso} /></div>

      <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

      <div className="rounded-lg border border-neutral-200 bg-white p-5">
        {paso === 0 && (
          <CampoSelect etiqueta="¿Qué servicio necesitas?" value={servicioId}
            onChange={(e) => setServicioId(e.target.value)}
            opciones={servicios.map((s) => ({
              valor: String(s.id),
              texto: `${s.nombre} — ${formatearPrecio(s.precio)}`,
            }))} />
        )}

        {paso === 1 && (
          mascotas.length === 0 ? (
            <p className="text-neutral-600">
              No tienes mascotas registradas.{' '}
              <Link to="/mascotas/nueva" className="font-medium underline">Registra una</Link> para continuar.
            </p>
          ) : (
            <CampoSelect etiqueta="¿Para cuál de tus mascotas?" value={mascotaId}
              onChange={(e) => setMascotaId(e.target.value)}
              opciones={mascotas.map((m) => ({ valor: String(m.id), texto: m.nombre }))} />
          )
        )}

        {paso === 2 && (
          <div className="space-y-5">
            <div className="grid gap-5 sm:grid-cols-2">
              <CampoTexto etiqueta="Fecha" tipo="date" min={hoy}
                value={fecha} onChange={(e) => setFecha(e.target.value)} />
              <CampoSelect etiqueta="Profesional (opcional)" value={empleadoId}
                onChange={(e) => setEmpleadoId(e.target.value)}
                opciones={empleados.map((e) => ({ valor: String(e.id), texto: `${e.nombre} ${e.apellido}` }))} />
            </div>

            {cargando ? (
              <p className="text-neutral-600">Buscando horarios…</p>
            ) : fecha ? (
              <RejillaFranjas franjas={franjas} seleccion={franja?.horaInicio} onElegir={setFranja} />
            ) : (
              <p className="text-neutral-600">Elige una fecha para ver los horarios.</p>
            )}
          </div>
        )}

        {paso === 3 && (
          <dl className="space-y-2 text-neutral-800">
            <div className="flex justify-between"><dt>Servicio</dt><dd className="font-semibold">{servicio?.nombre}</dd></div>
            <div className="flex justify-between"><dt>Mascota</dt><dd className="font-semibold">{mascota?.nombre}</dd></div>
            <div className="flex justify-between"><dt>Fecha</dt><dd className="font-semibold">{fecha}</dd></div>
            <div className="flex justify-between">
              <dt>Hora</dt>
              <dd className="font-semibold">
                {franja?.horaInicio.slice(0, 5)} – {franja?.horaFin.slice(0, 5)}
                {servicio && ` (${textoDuracion(servicio.duracionMinutos)})`}
              </dd>
            </div>
            <div className="flex justify-between">
              <dt>Profesional</dt>
              <dd className="font-semibold">
                {empleadoId
                  ? empleados.find((e) => String(e.id) === empleadoId)?.nombre
                  : 'Sin preferencia'}
              </dd>
            </div>
            <div className="flex justify-between border-t border-neutral-200 pt-3 text-lg">
              <dt className="font-bold">Total</dt>
              <dd className="font-extrabold">{servicio && formatearPrecio(servicio.precio)}</dd>
            </div>
            <p className="pt-2 text-sm text-neutral-600">
              Al continuar irás a la pasarela de pago. Tienes 15 minutos para completarlo.
            </p>
          </dl>
        )}

        <div className="mt-6 flex flex-col gap-3 sm:flex-row-reverse">
          {paso < 3 ? (
            <Boton type="button" disabled={!puedeAvanzar} onClick={() => setPaso(paso + 1)}>
              Continuar
            </Boton>
          ) : (
            <Boton type="button" cargando={enviando} onClick={confirmar}>
              Confirmar y pagar
            </Boton>
          )}

          {paso > 0 && (
            <button type="button" onClick={() => setPaso(paso - 1)}
              className="min-h-12 rounded-md border border-neutral-300 px-4 font-semibold">
              Atrás
            </button>
          )}
        </div>
      </div>
    </div>
  );
}