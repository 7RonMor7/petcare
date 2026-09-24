import { Navigate, Route, Routes } from "react-router-dom";
import RutaProtegida from "./auth/RutaProtegida";
import DisenoApp from "./components/DisenoApp";
import LoginPage from "./pages/LoginPage";
import RegistroPage from "./pages/RegistroPage";
import InicioPage from "./pages/InicioPage";
import MascotaNuevaPage from "./pages/MascotaNuevaPage";
import MascotasPage from "./pages/MascotasPage";
import MascotaEditarPage from "./pages/MascotaEditarPage";

export default function App() {
  return (
    <Routes>
      {/* Públicas  */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/registro" element={<RegistroPage />} />

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
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}