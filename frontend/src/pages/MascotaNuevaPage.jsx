import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import cliente from '../api/cliente';
import { erroresPorCampo, mensajeError } from '../api/errores';
import FormularioMascota from '../components/FormularioMascota';
import Alerta from '../components/Alerta';

export default function MascotaNuevaPage() {
  const navigate = useNavigate();
  const [errores, setErrores] = useState({});
  const [errorGeneral, setErrorGeneral] = useState('');
  const [enviando, setEnviando] = useState(false);

  async function registrar(datos) {
    setErrores({});
    setErrorGeneral('');
    setEnviando(true);
    try {
      await cliente.post('/mascotas', datos);
      navigate('/mascotas');
    } catch (error) {
      const campos = erroresPorCampo(error);
      setErrores(campos);
      setErrorGeneral(Object.keys(campos).length ? '' : mensajeError(error));
    } finally {
      setEnviando(false);
    }
  }

  return (
    <div className="mx-auto max-w-2xl">
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Registrar mascota</h1>

      <div className="mt-6">
        <Alerta>{errorGeneral}</Alerta>
      </div>

      <FormularioMascota
        errores={errores}
        enviando={enviando}
        textoBoton="Registrar mascota"
        onEnviar={registrar}
      >
        <Link to="/mascotas" className="inline-flex min-h-12 items-center justify-center rounded-md border border-neutral-300 px-4 font-semibold">
          Cancelar
        </Link>
      </FormularioMascota>
    </div>
  );
}