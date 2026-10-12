import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { formatearPrecio } from '../api/formato';
import InsigniaReserva from '../components/InsigniaReserva';
import Alerta from '../components/Alerta';
import DialogoConfirmacion from '../components/DialogoConfirmacion';

export default function MisReservasPage() {
  const [reservas, setReservas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [pagandoId, setPagandoId] = useState(null);
  const [aCancelar, setACancelar] = useState(null);
  const [cancelando, setCancelando] = useState(false);
  const [errorGeneral, setErrorGeneral] = useState('');

    useEffect(() => {
      let vigente = true;
      cliente.get('/reservas')
        .then(({ data }) => { if (vigente) setReservas(data); })
        .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
        .finally(() => { if (vigente) setCargando(false); });
      return () => { vigente = false; };
    }, []);

    async function pagar(id) {
      setPagandoId(id);
      setErrorGeneral('');
      try {
        const { data } = await cliente.post(`/reservas/${id}/pago/checkout`);
        window.location.assign(data.urlCheckout);
      } catch (error) {
        setErrorGeneral(mensajeError(error));
        setPagandoId(null);
      }
    }

    async function confirmarCancelacion () {
      setCancelando(true);
      setErrorGeneral('');
      try {
        const { data } = await cliente.post(`/reservas/${aCancelar.id}/cancelar`);
        setReservas((actuales) => actuales.map((r) => (r.id === data.id ? data : r)));
        setACancelar(null);
      } catch (error) {
        setErrorGeneral(mensajeError(error));
        setACancelar(null);
      } finally {
        setCancelando(false);
      }
    }

    if (cargando) return <p className="text-neutral-600">Cargando tus reservas…</p>;

    return (
      <div>
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Mis reservas</h1>
          <Link to="/reservar" className="rounded-md bg-neutral-900 px-4 py-2 font-semibold text-white">
            Reservar
          </Link>
        </div>

        <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

        {reservas.length === 0 ? (
          <p className="rounded-lg border border-dashed border-neutral-300 p-8 text-center text-neutral-600">
            Todavía no tienes reservas.
          </p>
        ) : (
          <ul className="space-y-3">
            {reservas.map((r) => (
              <li key={r.id} className="flex flex-wrap items-center gap-x-4 gap-y-2 rounded-lg border border-neutral-200 bg-white p-4">
                <div className="min-w-56 flex-1">
                  <div className="flex items-center gap-2">
                    <h2 className="font-bold text-neutral-900">{r.servicio}</h2>
                    <InsigniaReserva estado={r.estado} />
                  </div>
                  <p className="text-sm text-neutral-600">
                    {r.mascota} · {r.fecha} · {r.horaInicio.slice(0, 5)}–{r.horaFin.slice(0, 5)} · {r.empleado}
                  </p>
                </div>

                <p className="font-semibold text-neutral-900">{formatearPrecio(r.total)}</p>

                {r.estado === 'PENDIENTE_PAGO' && (
                  <button onClick={() => pagar(r.id)} disabled={pagandoId === r.id}
                    className="min-h-11 rounded-md bg-neutral-900 px-4 text-sm font-semibold text-white disabled:opacity-60">
                    {pagandoId === r.id ? 'Abriendo…' : 'Pagar'}
                  </button>
                )}
                {(r.estado === 'PENDIENTE_PAGO' || r.estado === 'CONFIRMADA') && (
                  <button onClick={() => setACancelar(r)} className="text-sm font-semibold text-red-700 underline">
                    Cancelar
                  </button>
                )}
              </li>
            ))}
          </ul>
        )}
        <DialogoConfirmacion
          abierto={aCancelar !== null}
          titulo="¿Cancelar la reserva?"
          mensaje={aCancelar?.estado === 'CONFIRMADA'
            ? 'Tu pago quedará pendiente de reembolso, que gestionará PetCare.'
            : 'Se liberará el horario para otros clientes.'}
          textoConfirmar="Sí, cancelar"
          procesando={cancelando}
          onConfirmar={confirmarCancelacion}
          onCancelar={() => setACancelar(null)}
        />
      </div>
    );
}