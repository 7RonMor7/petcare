import { Navigate, Route, Routes } from "react-router-dom";
import RutaProtegida from "./components/RutaProtegida";
import DisenoApp from "./components/DisenoApp";
import LoginPage from "./pages/LoginPage";
import RegistroPage from "./pages/RegistroPage";
import InicioPage from "./pages/InicioPage";

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
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}