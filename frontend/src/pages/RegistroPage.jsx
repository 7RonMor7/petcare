import { useState } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import { codigoError, erroresPorCampo, mensajeError } from '../api/errores';
import DisenoAuth from '../components/DisenoAuth';
import CampoTexto from '../components/CampoTexto';
import Boton from '../components/Boton';
import Alerta from '../components/Alerta';

const INICIAL = {
    nombre: '', apellido: '', correo: '', telefono: '', 
    contrasena: '', confirmar: '', aceptaPoliticaDatos: false,
}

export default function RegistroPage() {
    const { registrar, iniciarSesion, estaAutenticado, cargando } = useAuth();
    const [form, setForm] = useState(INICIAL);
    const [errores, setErrores] = useState({});
    const [errorGeneral, setErrorGeneral] = useState('');
    const [enviando, setEnviando] = useState(false);

    if (cargando) return null;
    if (estaAutenticado) return <Navigate to="/" replace />;

    const cambiar = (e) => {
        const { name, value, type, checked } = e.target;
        setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
    };

    async function enviar(e) {
        e.preventDefault();
        setErrores({});
        setErrorGeneral('');
        
        if (form.contrasena !== form.confirmar) {
            setErrores({ confirmar: 'Las contraseñas no coinciden' });
            return;
        }

        const { confirmar, ...datos } = form;            // "confirmar" no se envía al backend
        datos.correo = datos.correo.trim();
        datos.telefono = datos.telefono.trim() || null;

        setEnviando(true);
        try {
            await registrar(datos);
            await iniciarSesion(datos.correo, datos.contrasena);
        } catch (error) {
            if (codigoError(error) === 'CORREO_YA_REGISTRADO') {
                setErrores({ correo: 'Ya existe una cuenta con este correo' });
            } else {
                const campos = erroresPorCampo(error);
                setErrores(campos);
                setErrorGeneral(Object.keys(campos).length ? '' : mensajeError(error));
            }
        } finally {
            setEnviando(false);
        }
    }

    return (
        <DisenoAuth
          titulo="Crea tu cuenta"
          subtitulo={<>¿Ya tienes cuenta? <Link to="/login" className="font-medium underline">Inicia sesión</Link></>}
        >
          <Alerta>{errorGeneral}</Alerta>

         <form onSubmit={enviar} noValidate className="space-y-5">
           <div className="grid gap-5 sm:grid-cols-2">
             <CampoTexto etiqueta="Nombre" name="nombre" autoComplete="given-name"
               value={form.nombre} onChange={cambiar} error={errores.nombre} />
             <CampoTexto etiqueta="Apellido" name="apellido" autoComplete="family-name"
              value={form.apellido} onChange={cambiar} error={errores.apellido} />
           </div>
           <CampoTexto etiqueta="Correo" name="correo" tipo="email" autoComplete="email"
             value={form.correo} onChange={cambiar} error={errores.correo} />
           <CampoTexto etiqueta="Teléfono (opcional)" name="telefono" tipo="tel" autoComplete="tel"
             value={form.telefono} onChange={cambiar} error={errores.telefono} />
           <CampoTexto etiqueta="Contraseña" name="contrasena" tipo="password" autoComplete="new-password"
             value={form.contrasena} onChange={cambiar} error={errores.contrasena} />
           <CampoTexto etiqueta="Confirmar contraseña" name="confirmar" tipo="password" autoComplete="new-password"
             value={form.confirmar} onChange={cambiar} error={errores.confirmar} />

           <div>
             <label className="flex items-start gap-3 text-sm text-neutral-800">
               <input type="checkbox" name="aceptaPoliticaDatos" checked={form.aceptaPoliticaDatos}
                 onChange={cambiar} className="mt-0.5 h-5 w-5 accent-teal-600" />
               Acepto la política de tratamiento de datos personales
             </label>
             {errores.aceptaPoliticaDatos && (
               <p className="mt-1 text-sm text-red-700">{errores.aceptaPoliticaDatos}</p>
             )}
           </div>

           <Boton cargando={enviando}>Crear cuenta</Boton>
         </form>
       </DisenoAuth>
    );
}