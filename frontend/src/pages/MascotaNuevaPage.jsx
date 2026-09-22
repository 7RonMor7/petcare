import { useState } from 'react';
import { useAuth } from '../auth/AuthContext';
import cliente from '../api/cliente';
import { erroresPorCampo, mensajeError } from '../api/errores';
import CampoTexto from '../components/CampoTexto';
import CampoSelect from '../components/CampoSelect';
import CampoArea from '../components/CampoArea';
import Boton from '../components/Boton';
import Alerta from '../components/Alerta';

const INICIAL = {
    nombre: '', especie: '', raza: '', sexo: '',
    fechaNacimiento: '', pesoKg: '', observaciones: '',
};

const ESPECIES = [
    { valor: 'PERRO', texto: 'Perro' },
    { valor: 'GATO', texto: 'Gato' },
    { valor: 'OTRO', texto: 'Otro' },
];

const SEXO = [
    { valor: 'MACHO', texto: 'Macho' },
    { valor: 'HEMBRA', texto: 'Hembra' },
];

export default function MascotaNuevaPage() {
    const { usuario } = useAuth();
    const [form, setForm] = useState(INICIAL);
    const [errores, setErrores] = useState({});
    const [errorGeneral, setErrorGeneral] = useState('');
    const [registrada, setRegistrada] = useState(null);
    const [enviando, setEnviando] = useState(false);

    const hoy = new Date().toISOString().slice(0, 10);
    const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    async function enviar(e) {
        e.preventDefault();
        setErrores({});
        setErrorGeneral('');
        setRegistrada(null);
        setEnviando(true);

        const datos = {
            nombre: form.nombre.trim(),
            especie: form.especie || null,
            raza: form.raza.trim() || null,
            sexo: form.sexo || null,
            fechaNacimiento: form.fechaNacimiento || null,
            pesoKg: form.pesoKg === '' ? null : Number(form.pesoKg),
            observaciones: form.observaciones.trim() || null,
        };

        try {
            const { data } = await cliente.post('/mascotas', datos);
            setRegistrada(data);
            setForm(INICIAL);
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
      <p className="mt-1 text-neutral-600">Quedará asociada a tu cuenta ({usuario.correo}).</p>

      <div className="mt-6">
        <Alerta>{errorGeneral}</Alerta>
        {registrada && (
          <Alerta tipo="exito">
            {registrada.nombre} quedó registrada (id {registrada.id}).
          </Alerta>
        )}
      </div>

      <form onSubmit={enviar} noValidate className="space-y-5 rounded-lg border border-neutral-200 bg-white p-5">
        <CampoTexto etiqueta="Nombre" name="nombre" value={form.nombre}
          onChange={cambiar} error={errores.nombre} />

        <div className="grid gap-5 sm:grid-cols-2">
          <CampoSelect etiqueta="Especie" name="especie" opciones={ESPECIES}
            value={form.especie} onChange={cambiar} error={errores.especie} />
          <CampoSelect etiqueta="Sexo" name="sexo" opciones={SEXOS}
            value={form.sexo} onChange={cambiar} error={errores.sexo} />
        </div>

        <div className="grid gap-5 sm:grid-cols-2">
          <CampoTexto etiqueta="Raza (opcional)" name="raza" value={form.raza}
            onChange={cambiar} error={errores.raza} />
          <CampoTexto etiqueta="Fecha de nacimiento" name="fechaNacimiento" tipo="date" max={hoy}
            value={form.fechaNacimiento} onChange={cambiar} error={errores.fechaNacimiento} />
        </div>

        <CampoTexto etiqueta="Peso (kg)" name="pesoKg" tipo="number" step="0.01" min="0.01" max="200"
          inputMode="decimal" value={form.pesoKg} onChange={cambiar} error={errores.pesoKg} />

        <CampoArea etiqueta="Observaciones (opcional)" name="observaciones"
          value={form.observaciones} onChange={cambiar} error={errores.observaciones} />

        <Boton cargando={enviando}>Registrar mascota</Boton>
      </form>
    </div>
  );
}