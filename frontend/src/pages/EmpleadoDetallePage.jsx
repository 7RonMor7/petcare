import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import EditorJornada from '../components/EditorJornada';
import SelectorServicios from '../components/SelectorServicios';
import Alerta from '../components/Alerta';

export default function EmpleadoDetallePage() {
  const { id } = useParams();
  const [empleado, setEmpleado] = useState(null);
  const [jornada, setJornada] = useState([]);
  const [catalogo, setCatalogo] = useState([]);
  const [asignados, setAsignados] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [errorGeneral, setErrorGeneral] = useState('');
  const [aviso, setAviso] = useState('');
  const [guardando, setGuardando] = useState(null);   // 'jornada' | 'servicios' | null

  useEffect(() => {
    let vigente = true;
    Promise.all([
      cliente.get('/empleados'),
      cliente.get(`/empleados/${id}/jornada`),
      cliente.get('/servicios'),
      cliente.get(`/empleados/${id}/servicios`),
    ])
      .then(([resEmpleados, resJornada, resCatalogo, resAsignados]) => {
        if (!vigente) return;
        setEmpleado(resEmpleados.data.find((e) => String(e.id) === id) ?? null);
        setJornada(resJornada.data);
        setCatalogo(resCatalogo.data);
        setAsignados(resAsignados.data.map((s) => s.id));
      })
      .catch((error) => { if (vigente) setErrorGeneral(mensajeError(error)); })
      .finally(() => { if (vigente) setCargando(false); });
    return () => { vigente = false; };
  }, [id]);

  async function guardarJornada(tramos) {
    setGuardando('jornada');
    setErrorGeneral('');
    setAviso('');
    try {
      const { data } = await cliente.put(`/empleados/${id}/jornada`, { tramos });
      setJornada(data);
      setAviso('Jornada guardada');
    } catch (error) {
      setErrorGeneral(mensajeError(error));
    } finally {
      setGuardando(null);
    }
  }

  async function guardarServicios(servicioIds) {
    setGuardando('servicios');
    setErrorGeneral('');
    setAviso('');
    try {
      const { data } = await cliente.put(`/empleados/${id}/servicios`, { servicioIds });
      setAsignados(data.map((s) => s.id));
      setAviso('Servicios guardados');
    } catch (error) {
      setErrorGeneral(mensajeError(error));
    } finally {
      setGuardando(null);
    }
  }

  if (cargando) return <p className="text-neutral-600">Cargando…</p>;

  return (
    <div className="mx-auto max-w-3xl">
      <Link to="/admin/empleados" className="text-sm font-medium underline">← Empleados</Link>

      <h1 className="mt-2 text-2xl font-extrabold tracking-tight text-neutral-900">
        {empleado ? `${empleado.nombre} ${empleado.apellido}` : 'Empleado'}
      </h1>
      {empleado && <p className="text-neutral-600">{empleado.correo}</p>}

      <div className="mt-6">
        <Alerta>{errorGeneral}</Alerta>
        <Alerta tipo="exito">{aviso}</Alerta>
      </div>

      <section className="mt-8">
        <h2 className="mb-3 text-lg font-bold text-neutral-900">Jornada semanal</h2>
        <EditorJornada
          key={JSON.stringify(jornada)}
          valorInicial={jornada}
          guardando={guardando === 'jornada'}
          onGuardar={guardarJornada}
        />
      </section>

      <section className="mt-10">
        <h2 className="mb-3 text-lg font-bold text-neutral-900">Servicios que presta</h2>
        <SelectorServicios
          key={asignados.join(',')}
          catalogo={catalogo}
          seleccionInicial={asignados}
          guardando={guardando === 'servicios'}
          onGuardar={guardarServicios}
        />
      </section>
    </div>
  );
}