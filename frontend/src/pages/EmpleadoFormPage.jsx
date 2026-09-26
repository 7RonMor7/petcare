import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import cliente from '../api/cliente';
import { erroresPorCampo, mensajeError } from '../api/errores';
import CampoTexto from '../components/CampoTexto';
import Boton from '../components/Boton';
import Alerta from '../components/Alerta';

const VACIO = { nombre: '', apellido: '', correo: '', telefono: '', contrasenaTemporal: '' };

export default function EmpleadoFormPage() {
  const { id } = useParams();
  const esEdicion = Boolean(id);
  const navigate = useNavigate();

  const [form, setForm] = useState(VACIO);
  const [cargando, setCargando] = useState(esEdicion);
  const [errores, setErrores] = useState({});
  const [errorGeneral, setErrorGeneral] = useState('');
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    if (!esEdicion) return;
    cliente.get('/empleados')
      .then(({ data }) => {
        const empleado = data.find((e) => String(e.id) === id);
        if (empleado) setForm({ ...VACIO, ...empleado, telefono: empleado.telefono ?? '' });
        else setErrorGeneral('El empleado no existe');
      })
      .catch((error) => setErrorGeneral(mensajeError(error)))
      .finally(() => setCargando(false));
  }, [id, esEdicion]);

  const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  async function guardar(e) {
    e.preventDefault();
    setErrores({});
    setErrorGeneral('');
    setEnviando(true);
    try {
      if (esEdicion) {
        await cliente.put(`/empleados/${id}`, {
          nombre: form.nombre.trim(),
          apellido: form.apellido.trim(),
          telefono: form.telefono.trim() || null,
        });
      } else {
        await cliente.post('/empleados', {
          nombre: form.nombre.trim(),
          apellido: form.apellido.trim(),
          correo: form.correo.trim(),
          telefono: form.telefono.trim() || null,
          contrasenaTemporal: form.contrasenaTemporal,
        });
      }
      navigate('/admin/empleados');
    } catch (error) {
      const campos = erroresPorCampo(error);
      setErrores(campos);
      setErrorGeneral(Object.keys(campos).length ? '' : mensajeError(error));
    } finally {
      setEnviando(false);
    }
  }

  if (cargando) return <p className="text-neutral-600">Cargando...</p>;

  return (
    <div className="mx-auto max-w-2xl">
      <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900">
        {esEdicion ? 'Editar empleado' : 'Nuevo empleado'}
      </h1>

      <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

      <form onSubmit={guardar} noValidate className="space-y-5 rounded-lg border border-neutral-200 bg-white p-5">
        <div className="grid gap-5 sm:grid-cols-2">
          <CampoTexto etiqueta="Nombre" name="nombre" value={form.nombre}
            onChange={cambiar} error={errores.nombre} />
          <CampoTexto etiqueta="Apellido" name="apellido" value={form.apellido}
            onChange={cambiar} error={errores.apellido} />
        </div>

        <CampoTexto etiqueta="Correo" name="correo" tipo="email" value={form.correo}
          onChange={cambiar} error={errores.correo} disabled={esEdicion} />

        <CampoTexto etiqueta="Teléfono (opcional)" name="telefono" tipo="tel" value={form.telefono}
          onChange={cambiar} error={errores.telefono} />

        {!esEdicion && (
          <CampoTexto etiqueta="Contraseña temporal" name="contrasenaTemporal" tipo="password"
            autoComplete="new-password" value={form.contrasenaTemporal}
            onChange={cambiar} error={errores.contrasenaTemporal} />
        )}

        <div className="flex flex-col gap-3 sm:flex-row-reverse">
          <Boton cargando={enviando}>{esEdicion ? 'Guardar cambios' : 'Crear empleado'}</Boton>
          <Link to="/admin/empleados" className="inline-flex min-h-12 items-center justify-center rounded-md border border-neutral-300 px-4 font-semibold">
            Cancelar
          </Link>
        </div>
      </form>
    </div>
  );
}