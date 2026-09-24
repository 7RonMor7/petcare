import { Navigate, Route, Routes } from "react-router-dom";
import RutaProtegida from "./auth/RutaProtegida";
import DisenoApp from "./components/DisenoApp";
import LoginPage from "./pages/LoginPage";
import RegistroPage from "./pages/RegistroPage";
import InicioPage from "./pages/InicioPage";
import MascotaNuevaPage from "./pages/MascotaNuevaPage";
import MascotasPage from "./pages/MascotasPage";
import MascotaEditarPage from "./pages/MascotaEditarPage";
import CatalogoPage from "./pages/CatalogoPage";
import ServiciosAdminPage from "./pages/ServiciosAdminPage";
import ServicioFormPage from "./pages/ServicioFormPage";

export default function App() {
  return (
    <Routes>
      {/* Públicas  */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/registro" element={<RegistroPage />} />
      <Route path="/servicios" element={<CatalogoPage />} />

      {/* Privadas: primero el guardián, después el marco con la barra */}
      <Route element={<RutaProtegida />}>
        <Route element={<DisenoApp />}>
          <Route path="/" element={<InicioPage />} />
          <Route element={<RutaProtegida permiso="MASCOTA_LEER_PROPIA" />}>
            <Route path="/mascotas" element={<MascotasPage />} />
          </Route>
          <Route element={<RutaProtegida permiso="MASCOTA_CREAR" />}>
            <Route path="/mascotas/nueva" element={<MascotaNuevaPage />} />
          </Route>
          <Route element={<RutaProtegida permiso="MASCOTA_EDITAR_PROPIA" />}>
            <Route path="/mascotas/:id/editar" element={<MascotaEditarPage />} />
          </Route>
          <Route element={<RutaProtegida permiso="SERVICIO_GESTIONAR" />}>
            <Route path="/admin/servicios" element={<ServiciosAdminPage />} />
            <Route path="/admin/servicios/nuevo" element={<ServicioFormPage />} />
            <Route path="/admin/servicios/:id/editar" element={<ServicioFormPage />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}