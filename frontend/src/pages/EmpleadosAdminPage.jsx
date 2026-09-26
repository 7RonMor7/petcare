import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import Insignia from '../components/Insignia';
import Alerta from '../components/Alerta';

export default function EmpleadosAdminPage() {
    const [empleados, setEmpleados] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [errorGeneral, setErrorGeneral] = useState('');
    const [cambiandoId, setCambiandoId] = useState(null);

    useEffect(() => {
      let vigente = true;
      cliente.get('/empleados')
        .then(({ data }) => { if (vigente) setEmpleados(data); })
        .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
        .finally(() => { if (vigente) setCargando(false); });
      return () => { vigente = false; };
    }, []);

    async function cambiarEstado(empleado) {
      setCambiandoId(empleado.id);
      setErrorGeneral('');
      try {
        const { data } = await cliente.patch(`/empleados/${empleado.id}/estado`, {
          activo: !empleado.activo,
        });
        setEmpleados((actuales) => actuales.map((e) => (e.id === data.id ? data : e)));
      } catch (error) {
        setErrorGeneral(mensajeError(error));
      } finally {
        setCambiandoId(null);
      }
    }

    if (cargando) return <p className="text-neutral-600">Cargando empleados...</p>

    return (
      <div>
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Empleados</h1>
          <Link to="/admin/empleados/nuevo" className="rounded-md bg-neutral-900 px-4 py-2 font-semibold text-white">
            Nuevo empleado
          </Link>
        </div>

        <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

        {empleados.length === 0 ? (
          <p className="rounded-lg border border-dashed border-neutral-300 p-8 text-center text-neutral-600">
            Todavía no hay empleados registrados.
          </p>
        ) : (
          <ul className="space-y-3">
            {empleados.map((e) => (
              <li key={e.id} className="flex flex-wrap items-center gap-x-4 gap-y-2 rounded-lg border border-neutral-200 bg-white p-4">
                <div className="min-w-48 flex-1">
                  <div className="flex items-center gap-2">
                    <h2 className="font-bold text-neutral-900">{e.nombre} {e.apellido}</h2>
                    <Insignia activo={e.activo} />
                  </div>
                  <p className="text-sm text-neutral-600">{e.correo}{e.telefono ? ` · ${e.telefono}` : ''}</p>
                </div>

                <div className="flex items-center gap-3 text-sm font-semibold">
                  <Link to={`/admin/empleados/${e.id}`} className="underline">Jornada y servicios</Link>
                  <Link to={`/admin/empleados/${e.id}/editar`} className="underline">Editar</Link>
                  <button onClick={() => cambiarEstado(e)} disabled={cambiandoId === e.id}
                    className="rounded-md border border-neutral-300 px-3 py-2 disabled:opacity-60">
                    {e.activo ? 'Desactivar' : 'Activar'}
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    );
}