import { Link } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

// Pagina de inicio TEMPORAL. En el paso 5 se protege con RutaProtegida
// y gana la barra superior con el boton de cerrar sesion.
export default function InicioPage() {
  const { usuario, cargando, cerrarSesion } = useAuth();

  if (cargando) return <p className="p-6 text-neutral-600">Comprobando sesión…</p>;

  return (
    <main className="mx-auto max-w-xl px-4 py-10">
      {usuario ? (
        <>
          <h1 className="text-2xl font-extrabold">Hola, {usuario.nombre}</h1>
          <p className="mt-1 text-neutral-600">{usuario.correo} · {usuario.roles.join(', ')}</p>
          <button onClick={cerrarSesion} className="mt-6 rounded-md border border-neutral-900 px-4 py-2 font-semibold">
            Cerrar sesión
          </button>
        </>
      ) : (
        <p>
          No has iniciado sesión. <Link to="/login" className="font-medium underline">Inicia sesión</Link>
        </p>
      )}
    </main>
  );
}
