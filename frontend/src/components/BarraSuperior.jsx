import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

// Barra superior de la aplicacion: marca, navegacion y cierre de sesion.
// En movil el nombre del usuario se oculta para que quepa el boton.

const ENLACES = [
  { a: '/', texto: 'Inicio', permiso: null },
  { a: '/mascotas/nueva', texto: 'Registrar mascota', permiso: 'MASCOTA_CREAR' },
];

export default function BarraSuperior() {
  const { usuario, cerrarSesion, tienePermiso } = useAuth();

  return (
    <header className="border-b border-neutral-200 bg-white">
      <div className="mx-auto flex max-w-5xl items-center gap-4 px-4 py-3">
        <Link to="/" className="flex items-center gap-2 font-extrabold tracking-tight text-neutral-900">
          <svg viewBox="0 0 24 24" className="h-6 w-6 text-teal-600" fill="currentColor" aria-hidden="true">
            <circle cx="5.5" cy="9" r="2.2" /><circle cx="9.5" cy="5" r="2.2" />
            <circle cx="14.5" cy="5" r="2.2" /><circle cx="18.5" cy="9" r="2.2" />
            <path d="M12 10.5c-3.2 0-6 3.6-6 6.3 0 1.9 1.5 2.7 3.2 2.7 1.2 0 1.9-.6 2.8-.6s1.6.6 2.8.6c1.7 0 3.2-.8 3.2-2.7 0-2.7-2.8-6.3-6-6.3z" />
          </svg>
          PetCare
        </Link>

        <nav className="flex items-center gap-1 text-sm">
          {ENLACES.filter((e) => !e.permiso || tienePermiso(e.permiso)).map((e) => (
            <NavLink
              key={e.a}
              to={e.a}
              end
              className={({ isActive }) =>
                `rounded-md px-3 py-2 font-medium ${isActive ? 'bg-neutral-100 text-neutral-900' : 'text-neutral-600 hover:text-neutral-900'}`
              }
            >
              {e.texto}
            </NavLink>
          ))}
        </nav>

        <div className="ml-auto flex items-center gap-3">
          <span className="hidden text-sm text-neutral-600 sm:inline">
            {usuario?.nombre} {usuario?.apellido}
          </span>
          <button
            onClick={cerrarSesion}
            className="rounded-md border border-neutral-300 px-3 py-2 text-sm font-semibold text-neutral-900 hover:bg-neutral-100"
          >
            Cerrar sesión
          </button>
        </div>
      </div>
    </header>
  );
}
