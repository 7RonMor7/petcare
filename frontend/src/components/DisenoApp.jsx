import { Outlet } from 'react-router-dom';
import BarraSuperior from './BarraSuperior';

// Marco comun de las pantallas con sesion iniciada. <Outlet /> es el hueco
// donde React Router dibuja la pagina de la ruta activa.
export default function DisenoApp() {
  return (
    <div className="flex min-h-dvh flex-col bg-neutral-50">
      <BarraSuperior />
      <main className="mx-auto w-full max-w-5xl flex-1 px-4 py-8">
        <Outlet />
      </main>
    </div>
  );
}
