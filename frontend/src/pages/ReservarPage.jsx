import { useEffect, useState } from 'react';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { formatearPrecio, textoDuracion } from '../api/formato';
import CampoSelect from '../components/CampoSelect';
import CampoTexto from '../components/CampoTexto';
import RejillaFranjas from '../components/RejillaFranjas';
import Alerta from '../components/Alerta';

export default function ReservarPage() {
  const [servicios, setServicios] = useState([]);
  const [empleados, setEmpleados] = useState([]);
  const [servicioId, setServicioId] = useState('');
  const [empleadoId, setEmpleadoId] = useState('');
  const [fecha, setFecha] = useState('');
  const [franjas, setFranjas] = useState([]);
  const [seleccion, setSeleccion] = useState(null);
  const [asignado, setAsignado] = useState(null);
  const [cargando, setCargando] = useState(false);
  const [errorGeneral, setErrorGeneral] = useState('');

  const hoy = new Date().toLocaleDateString('sv');
  const servicio = servicios.find((s) => String(s.id) === servicioId);

  // Catálofo, una sola vez
  useEffect(() => {
    cliente.get('/servicios')
        .then(({ data }) => setServicios(data))
        .catch((error) => setErrorGeneral(mensajeError(error)));
  }, []);

  // Empleados del servicio elegido
  useEffect(() => {
    setEmpleadoId('');
    setEmpleados([]);
    if (!servicioId) return;
    cliente.get(`/servicios/${servicioId}/empleados`)
        .then(({ data }) => setEmpleados(data))
        .catch((error) => setEmpleados([]));
  }, [servicioId]);

  // Disponibilidad: cada vez que cambia servicio, fehcha o empleado
  useEffect(() => {
    setSeleccion(null);
    setAsignado(null);
    setFranjas([]);
    if (!servicioId || !fecha) return;

    let vigente = true;
    setCargando(true);
    setErrorGeneral('');
    cliente.get('/disponibilidad', {
        params: { servicioId, fecha, ...(empleadoId ? { empleadoId } : {}) },
    })
        .then(({ data }) => { if (vigente) setFranjas(data) })
        .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)) })
        .finally(() => { if (vigente) setCargando(false) });
    return () => { vigente = false };
  }, [servicioId, fecha, empleadoId]);

  async function elegirFranja(franja) {
    setSeleccion(franja.horaInicio);
    setAsignado(null);
    setErrorGeneral('');
    try {
      if (empleadoId) {
        setAsignado({ empleadoId: Number(empleadoId), horaInicio: franja.horaInicio });
      } else {
        const { data } = await cliente.get('/disponibilidad/asignacion', {
            params: { servicioId, fecha, horaInicio: franja.horaInicio },
        });
        setAsignado(data);
      }
    } catch (error) {
        setErrorGeneral(mensajeError(error));
        setSeleccion(null);
    }
  }

  const nombreDe = (id) => {
    const e = empleados.find((x) => x.id === id);
    return e ? `${e.nombre} ${e.apellidos}` : `empleado #${id}`;
  };

  return (
    <div className="mx-auto max-w-3xl">
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Reservar un servicio</h1>

      <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

      <div className="grid gap-5 rounded-lg border border-neutral-200 bg-white p-5 sm:grid-cols-3">
        <CampoSelect
          etiqueta="Servicio"
          value={servicioId}
          onChange={(e) => setServicioId(e.target.value)}
          opciones={servicios.map((s) => ({ valor: String(s.id), texto: s.nombre }))}
        />
        <CampoTexto
          etiqueta="Fecha"
          tipo="date"
          min={hoy}
          value={fecha}
          onChange={(e) => setFecha(e.target.value)}
        />
        <CampoSelect
          etiqueta="Profesional"
          value={empleadoId}
          onChange={(e) => setEmpleadoId(e.target.value)}
          opciones={empleados.map((e) => ({ valor: String(e.id), texto: `${e.nombre} ${e.apellido}` }))}
        />
      </div>

      {servicio && (
        <p className="mt-3 text-sm text-neutral-600">
          {formatearPrecio(servicio.precio)} · {textoDuracion(servicio.duracionMinutos)}
        </p>
      )}

      <div className="mt-8">
        {cargando ? (
          <p className="text-neutral-600">Buscando horarios…</p>
        ) : servicioId && fecha ? (
          <RejillaFranjas franjas={franjas} seleccion={seleccion} onElegir={elegirFranja} />
        ) : (
          <p className="text-neutral-600">Elige un servicio y una fecha para ver los horarios.</p>
        )}
      </div>

      {asignado && (
        <div className="mt-6 rounded-lg border border-teal-300 bg-teal-50 p-4">
          <p className="font-semibold text-teal-900">
            {seleccion.slice(0, 5)} con {nombreDe(asignado.empleadoId)}
          </p>
          <p className="mt-1 text-sm text-teal-800">
            La confirmación de la reserva llega en el Sprint 5.
          </p>
        </div>
      )}
    </div>
  );
}