import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { useAuth } from '../auth/AuthContext';
import { textoEspecie, textoSexo } from '../components/FormularioMascota';
import DialogoConfirmacion from '../components/DialogoConfirmacion';
import Alerta from '../components/Alerta';

export default function MascotasPage() {
    const { tienePermiso } = useAuth();
    const [mascotas, setMascotas] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [errorGeneral, setErrorGeneral] = useState('');
    const [aEliminar, setAEliminar] = useState(null);
    const [eliminando, setEliminando] = useState(false);

    useEffect (() => {
        let vigente = true;
        cliente.get('/mascotas')
          .then(({ data }) => { if (vigente) setMascotas(data); })
          .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
          .finally(() => { if (vigente) setCargando(false); })
        return () => { vigente = false; };
    }, []);

    async function confirmarEliminacion() {
        setEliminando(true);
        try {
          await cliente.delete(`/mascotas/${aEliminar.id}`);
          setMascotas((actuales) => actuales.filter((m) => m.id !== aEliminar.id));
          setAEliminar(null);
        } catch (error) {
          setErrorGeneral(mensajeError(error));
        } finally {
            setEliminando(false);
        }
    }

    if (cargando) return <p className="text-neutral-600">Cargando tus mascotas...</p>;

    return (
      <div>
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Mis mascotas</h1>
          {tienePermiso('MASCOTA_CREAR') && (
            <Link to="/mascotas/nueva" className="rounded-md bg-neutral-900 px-4 py-2 font-semibold text-white">
              Registrar
            </Link>
          )}
        </div>

        <div className="mt-6">
          <Alerta>{errorGeneral}</Alerta>
        </div>

        {mascotas.length === 0 ? (
          <p className="rounded-lg border border-dashed border-neutral-300 p-8 text-center text-neutral-600">
            Todavía no tienes mascotas registradas.
          </p>
        ) : (
          <ul className="grid gap-4 sm:grid-cols-2">
            {mascotas.map((m) => (
              <li key={m.id} className="rounded-lg border border-neutral-200 bg-white p-4">
                <h2 className="font-bold text-neutral-900">{m.nombre}</h2>
                <p className="mt-1 text-sm text-neutral-600">
                  {textoEspecie(m.especie)} · {textoSexo(m.sexo)} · {m.pesoKg} kg
                  {m.raza ? ` · ${m.raza}` : ''}
                </p>
                <p className="text-sm text-neutral-500">Nacimiento: {m.fechaNacimiento}</p>

                <div className="mt-4 flex gap-3 text-sm font-semibold">
                  {tienePermiso('MASCOTA_EDITAR_PROPIA') && (
                    <Link to={`/mascotas/${m.id}/editar`} className="underline">Editar</Link>
                  )}
                  {tienePermiso('MASCOTA_ELIMINAR_PROPIA') && (
                    <button onClick={() => setAEliminar(m)} className="text-red-700 underline">Eliminar</button>
                  )}
                </div>
              </li>
            ))}
          </ul>
        )}

        <DialogoConfirmacion
          abierto={aEliminar !== null}
          titulo={`¿Eliminar a ${aEliminar?.nombre}?`}
          mensaje="Dejará de aparecer en tu lista. Su historial de reservas se conserva."
          procesando={eliminando}
          onConfirmar={confirmarEliminacion}
          onCancelar={() => setAEliminar(null)}
        />
      </div>
    );
}