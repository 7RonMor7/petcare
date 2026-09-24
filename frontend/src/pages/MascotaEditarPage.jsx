import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import cliente from '../api/cliente';
import { erroresPorCampo, mensajeError } from '../api/errores';
import FormularioMascota, { desdeMascota } from '../components/FormularioMascota';
import Alerta from '../components/Alerta';

export default function MascotaEditarPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [mascota, setMascota] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [errores, setErrores] = useState({});
    const [errorGeneral, setErrorGeneral] = useState('');
    const [enviando, setEnviando] = useState(false);

    useEffect(() => {
        cliente.get(`/mascotas/${id}`)
          .then(({ data }) => setMascota(data))
          .catch((error) => setErrorGeneral(mensajeError(error)))
          .finally(() => setCargando(false));
    }, [id]);

    async function guardar(datos) {
        setErrores({});
        setErrorGeneral('');
        setEnviando(true);
        try {
            await cliente.put(`/mascotas/${id}`, datos);
            navigate('/mascotas');
        } catch (error) {
            const campos = erroresPorCampo(error);
            setErrores(campos);
            setErrorGeneral(Object.keys(campos).length ? '' : mensajeError(error));
        } finally {
            setEnviando(false);
        }
    }

    if (cargando) return <p className="text-neutral-600">Cargando...</p>;

    if (!mascota) {
        return (
          <div className="mx-auto max-w-2xl">
            <Alerta>{errorGeneral}</Alerta>
              <Link to="/mascotas" className="font-medium underline">Volver a mis mascotas</Link>
              </div>
          );
    }

    return (
      <div className="mx-auto max-w-2xl">
        <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">Editar a {mascota.nombre}</h1>

        <div className="mt-6">
          <Alerta>{errorGeneral}</Alerta>
        </div>

        <FormularioMascota
          valorInicial={desdeMascota(mascota)}
          errores={errores}
          enviando={enviando}
          textoBoton="Guardar cambios"
          onEnviar={guardar}
        >
          <Link to="/mascotas" className="inline-flex min-h-12 items-center justify-center rounded-md border border-neutral-300 px-4 font-semibold">
            Cancelar
          </Link>
        </FormularioMascota>
      </div>
    );

}