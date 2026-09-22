import { useState } from "react";
import { Link, Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { codigoError, erroresPorCampo, mensajeError} from '../api/errores';
import DisenoAuth from "../components/DisenoAuth";
import CampoTexto from "../components/CampoTexto";
import Boton from "../components/Boton";
import Alerta from "../components/Alerta";

export default function LoginPage() {
    const { iniciarSesion, estaAutenticado, cargando } = useAuth();
    const location = useLocation();
    const destino = location.state?.desde ?? '/';

    const [form, setForm] = useState({ correo: '', contrasena: '' });
    const [errores, setErrores] = useState({});
    const [errorGeneral, setErrorGeneral] = useState('');
    const [enviado, setEnviado] = useState(false);

    if (cargando) return null;
    if (estaAutenticado) return <Navigate to={destino} replace />;

    const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    async function enviar(e) {
        e.preventDefault();
        setErrores({});
        setErrorGeneral('');
        setEnviado(true);
        try {
            await iniciarSesion(form.correo.trim(), form.contrasena);
        } catch (error) {
            if (codigoError(error) === 'CREDENCIALES_INVALIDAS') {
                setErrorGeneral('Correo o contraseña incorrectos');
            } else {
                const campos = erroresPorCampo(error);
                setErrores(campos);
                setErrorGeneral(Object.keys(campos).length ? '' : mensajeError(error));
            }
        } finally {
            setEnviado(false);
        }
    }

    return (
      <DisenoAuth
        titulo="Inicia sesión"
        subtitulo={<>¿No tienes cuenta? <Link to="/registro" className="font-medium underline">Regístrate</Link></>}
      >
        <Alerta>{errorGeneral}</Alerta>

        <form onSubmit={enviar} noValidate className="space-y-5">
          <CampoTexto etiqueta="Correo" name="correo" tipo="email" autoComplete="email"
            value={form.correo} onChange={cambiar} error={errores.correo} />
          <CampoTexto etiqueta="Contraseña" name="contrasena" tipo="password" autoComplete="current-password"
            value={form.contrasena} onChange={cambiar} error={errores.contrasena} />
          <Boton cargando={enviando}>Iniciar sesión</Boton>
        </form>
      </DisenoAuth>
    );
}