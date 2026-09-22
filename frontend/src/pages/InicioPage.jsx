import { useAuth } from '../auth/AuthContext';

export default function InicioPage() {
  const { usuario } = useAuth();

  return (
    <div>
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">
        Hola, {usuario.nombre}
      </h1>
      <p className="mt-1 text-neutral-600">
        {usuario.correo} · {usuario.roles.join(', ')}
      </p>

      <section className="mt-8 rounded-lg border border-neutral-200 bg-white p-4">
        <h2 className="text-sm font-semibold text-neutral-900">
          Permisos de tu sesión ({usuario.permisos.length})
        </h2>
        <ul className="mt-3 flex flex-wrap gap-2">
          {usuario.permisos.map((p) => (
            <li key={p} className="rounded-full bg-neutral-100 px-3 py-1 text-xs text-neutral-700">
              {p}
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
}
