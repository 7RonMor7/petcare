import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "./AuthContext";
import SinPermisosPage from "../pages/SinPermisoPage";

export default function RutaProtegida({ permiso }) {
    const { estaAutenticado, cargando, tienePermiso } = useAuth();
    const location = useLocation();

    if (cargando){
        return <p className="p-6 text-neutral-600">Cargando...</p>;
    }

    if (!estaAutenticado) {
        return <Navigate to="/login" replace state={{ desde: location.pathname }}  />;
    }

    if (permiso && !tienePermiso(permiso)) {
        return <SinPermisosPage />;
    }

    return <Outlet />;
}