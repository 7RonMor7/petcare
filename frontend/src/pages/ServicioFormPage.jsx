import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError, separarErrores } from '../api/errores';
import FormularioServicio, { CAMPOS_SERVICIO, desdeServicio, SERVICIO_VACIO } from '../components/FormularioServicio';
import Alerta from '../components/Alerta';

export default function ServicioFormPage() {
  const { id } = useParams();
  const esEdicion = Boolean(id);
  const navigate = useNavigate();

  const [valorInicial, setValorInicial] = useState(SERVICIO_VACIO);
  const [cargando, setCargando] = useState(esEdicion);
  const [errores, setErrores] = useState({});
  const [errorGeneral, setErrorGeneral] = useState('');
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    if (!esEdicion) return;
    cliente.get('/servicios/gestion')
      .then(({ data }) => {
        const servicio = data.find((s) => String(s.id) === id);
        if (servicio) setValorInicial(desdeServicio(servicio));
        else setErrorGeneral('El servicio no existe');
      })
      .catch((error) => setErrorGeneral(mensajeError(error)))
      .finally(() => setCargando(false));
  }, [id, esEdicion]);

  async function guardar(datos) {
    setErrores({});
    setErrorGeneral('');
    setEnviando(true);
    try {
      if (esEdicion) await cliente.put(`/servicios/${id}`, datos);
      else await cliente.post('/servicios', datos);
      navigate('/admin/servicios');
    } catch (error) {
      const { campos, general } = separarErrores(error, CAMPOS_SERVICIO);
      setErrores(campos);
      setErrorGeneral(general);
    } finally {
      setEnviando(false);
    }
  }

  if (cargando) return <p className="text-neutral-600">Cargando…</p>;

  return (
    <div className="mx-auto max-w-2xl">
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">
        {esEdicion ? 'Editar servicio' : 'Nuevo servicio'}
      </h1>

      <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

      <FormularioServicio
        valorInicial={valorInicial}
        errores={errores}
        enviando={enviando}
        textoBoton={esEdicion ? 'Guardar cambios' : 'Crear servicio'}
        onEnviar={guardar}
      >
        <Link to="/admin/servicios" className="inline-flex min-h-12 items-center justify-center rounded-md border border-neutral-300 px-4 font-semibold">
          Cancelar
        </Link>
      </FormularioServicio>
    </div>
  );
}