import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

// Barra superior de la aplicacion: marca, navegacion y cierre de sesion.
//
// En movil los enlaces bajan a una segunda fila que se desplaza en horizontal
// DENTRO de su propia caja (overflow-x-auto). Antes iban en la misma fila que
// el logo y el boton: como los elementos flex no se encogen por debajo de su
// contenido, empujaban el ancho y aparecia scroll horizontal en toda la pagina.
const ENLACES = [
  { a: '/', texto: 'Inicio', permiso: null },
  { a: '/mascotas', texto: 'Mis mascotas', permiso: 'MASCOTA_LEER_PROPIA' },
  { a: '/servicios', texto: 'Servicios', permiso: null },
  { a: '/admin/servicios', texto: 'Servicios (admin)', permiso: 'SERVICIO_GESTIONAR' },
  { a: '/admin/empleados', texto: 'Empleados', permiso: 'EMPLEADO_GESTIONAR' },
];

export default function BarraSuperior() {
  const { usuario, cerrarSesion, tienePermiso } = useAuth();
  const visibles = ENLACES.filter((e) => !e.permiso || tienePermiso(e.permiso));

  return (
    <header className="border-b border-neutral-200 bg-white">
      <div className="mx-auto max-w-5xl px-4">

        <div className="flex items-center gap-4 py-3">
          <Link to="/" className="flex shrink-0 items-center gap-2 font-extrabold tracking-tight text-neutral-900">
            <svg viewBox="0 0 24 24" className="h-6 w-6 text-teal-600" fill="currentColor" aria-hidden="true">
              <circle cx="5.5" cy="9" r="2.2" /><circle cx="9.5" cy="5" r="2.2" />
              <circle cx="14.5" cy="5" r="2.2" /><circle cx="18.5" cy="9" r="2.2" />
              <path d="M12 10.5c-3.2 0-6 3.6-6 6.3 0 1.9 1.5 2.7 3.2 2.7 1.2 0 1.9-.6 2.8-.6s1.6.6 2.8.6c1.7 0 3.2-.8 3.2-2.7 0-2.7-2.8-6.3-6-6.3z" />
            </svg>
            PetCare
          </Link>

          <span className="ml-auto hidden truncate text-sm text-neutral-600 sm:inline">
            {usuario?.nombre} {usuario?.apellido}
          </span>

          <button
            onClick={cerrarSesion}
            className="ml-auto shrink-0 rounded-md border border-neutral-300 px-3 py-2 text-sm font-semibold text-neutral-900 hover:bg-neutral-100 sm:ml-0"
          >
            Cerrar sesión
          </button>
        </div>

        <nav className="-mx-1 flex gap-1 overflow-x-auto pb-2 text-sm [scrollbar-width:none] [&::-webkit-scrollbar]:hidden">
          {visibles.map((e) => (
            <NavLink
              key={e.a}
              to={e.a}
              end
              className={({ isActive }) =>
                `shrink-0 whitespace-nowrap rounded-md px-3 py-2 font-medium ${
                  isActive ? 'bg-neutral-100 text-neutral-900' : 'text-neutral-600 hover:text-neutral-900'
                }`
              }
            >
              {e.texto}
            </NavLink>
          ))}
        </nav>

      </div>
    </header>
  );
}
