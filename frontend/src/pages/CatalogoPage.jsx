import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { formatearPrecio, textoDuracion, textoUnidad } from '../api/formato';
import { useAuth } from '../auth/AuthContext';
import Alerta from '../components/Alerta';

// HU-028: catalogo publico. No exige sesion, asi que tambien lo ve un visitante.
export default function CatalogoPage() {
  const { estaAutenticado } = useAuth();
  const [servicios, setServicios] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [errorGeneral, setErrorGeneral] = useState('');

  useEffect(() => {
    let vigente = true;
    cliente.get('/servicios')
      .then(({ data }) => { if (vigente) setServicios(data); })
      .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
      .finally(() => { if (vigente) setCargando(false); });
    return () => { vigente = false; };
  }, []);

  return (
    <div className="mx-auto min-h-dvh max-w-5xl px-4 py-10">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Nuestros servicios</h1>
          <p className="mt-1 text-neutral-600">Precios vigentes. Reserva en línea con tu cuenta.</p>
        </div>
        <Link to={estaAutenticado ? '/' : '/login'} className="rounded-md bg-neutral-900 px-4 py-2 font-semibold text-white">
          {estaAutenticado ? 'Ir a mi cuenta' : 'Iniciar sesión'}
        </Link>
      </div>

      <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

      {cargando ? (
        <p className="text-neutral-600">Cargando catálogo…</p>
      ) : (
        <ul className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {servicios.map((s) => (
            <li key={s.id} className="flex flex-col rounded-lg border border-neutral-200 bg-white p-5">
              <h2 className="font-bold text-neutral-900">{s.nombre}</h2>
              {s.descripcion && <p className="mt-2 flex-1 text-sm text-neutral-600">{s.descripcion}</p>}
              <p className="mt-4 text-xl font-extrabold text-neutral-900">{formatearPrecio(s.precio)}</p>
              <p className="text-sm text-neutral-500">
                {textoUnidad(s.unidadCobro)}
                {textoDuracion(s.duracionMinutos) ? ` · ${textoDuracion(s.duracionMinutos)}` : ''}
              </p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
