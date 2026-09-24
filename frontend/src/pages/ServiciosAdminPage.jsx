import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import cliente from "../api/cliente";
import { mensajeError } from '../api/errores';
import { formatearPrecio, textoDuracion, textoUnidad } from '../api/formato';
import Insignia from '../components/Insignia';
import Alerta from '../components/Alerta';

export default function ServiciosAdminPage() {
    const [servicios, setServicios] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [errorGeneral, setErrorGeneral] = useState('');
    const [cambiandoId, setCambiandoId] = useState(null);

    useEffect(() => {
        let vigente = true;
        cliente.get('/servicios/gestion')
          .then(( {data}) => { if (vigente) setServicios(data); })
          .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
          .finally(() => { if (vigente) setCargando(false); });
        return () => { vigente = false; };
    }, []);

    async function cambiarEstado(servicio) {
        setCambiandoId(servicio.id);
        setErrorGeneral('');
        try {
            const { data } = await cliente.patch(`/servicios/${servicio.id}/estado`, {
                activo: !servicio.activo,
            });
            setServicios((actuales) => actuales.map((s) => (s.id === data.id ? data : s)));
        } catch (error) {
            setErrorGeneral(mensajeError(error));
        } finally {
            setCambiandoId(null);
        }
    }

    if (cargando) return <p className="text-neutral-600">Cargando servicios...</p>;

    return (
      <div>
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Gestión de servicios</h1>
          <Link to="/admin/servicios/nuevo" className="rounded-md bg-neutral-900 px-4 py-2 font-semibold text-white">
            Nuevo servicio
          </Link>
        </div>

        <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

        <ul className="space-y-3">
          {servicios.map((s) => (
            <li key={s.id} className="flex flex-wrap items-center gap-x-4 gap-y-2 rounded-lg border border-neutral-200 bg-white p-4">
              <div className="min-w-48 flex-1">
                <div className="flex items-center gap-2">
                  <h2 className="font-bold text-neutral-900">{s.nombre}</h2>
                  <Insignia activo={s.activo} />
                </div>
                <p className="text-sm text-neutral-600">
                  {formatearPrecio(s.precio)} {textoUnidad(s.unidadCobro)}
                  {textoDuracion(s.duracionMinutos) ? ` · ${textoDuracion(s.duracionMinutos)}` : ''}
                </p>
              </div>

              <div className="flex items-center gap-3 text-sm font-semibold">
                <Link to={`/admin/servicios/${s.id}/editar`} className="underline">Editar</Link>
                <button
                  onClick={() => cambiarEstado(s)}
                  disabled={cambiandoId === s.id}
                  className="rounded-md border border-neutral-300 px-3 py-2 disabled:opacity-60"
                >
                  {s.activo ? 'Desactivar' : 'Activar'}
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    );
}