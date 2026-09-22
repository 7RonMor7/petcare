import { Link } from 'react-router-dom';

export default function SinPermisoPage() {
  return (
    <div className="mx-auto max-w-md py-10 text-center">
      <p className="text-5xl font-extrabold text-neutral-300">403</p>
      <h1 className="mt-4 text-xl font-bold text-neutral-900">No tienes acceso a esta página</h1>
      <p className="mt-2 text-neutral-600">
        Tu cuenta no cuenta con el permiso necesario. Si crees que es un error, contacta al administrador.
      </p>
      <Link to="/" className="mt-6 inline-block font-medium underline">Volver al inicio</Link>
    </div>
  );
}
