import { useState } from 'react';
import CampoTexto from './CampoTexto';
import CampoSelect from './CampoSelect';
import CampoArea from './CampoArea';
import Boton from './Boton';

export const ESPECIES = [
  { valor: 'PERRO', texto: 'Perro' },
  { valor: 'GATO', texto: 'Gato' },
  { valor: 'OTRO', texto: 'Otro' },
];

export const SEXOS = [
  { valor: 'MACHO', texto: 'Macho' },
  { valor: 'HEMBRA', texto: 'Hembra' },
];

export const textoEspecie = (v) => ESPECIES.find((o) => o.valor === v)?.texto ?? v;
export const textoSexo = (v) => SEXOS.find((o) => o.valor === v)?.texto ?? v;

export const FORMULARIO_VACIO = {
  nombre: '', especie: '', raza: '', sexo: '',
  fechaNacimiento: '', pesoKg: '', observaciones: '',
};

// MascotaResponse -> estado del formulario (null se convierte en cadena vacia,
// porque un <input> controlado con value={null} avisa en consola).
export function desdeMascota(m) {
  return {
    nombre: m.nombre ?? '',
    especie: m.especie ?? '',
    raza: m.raza ?? '',
    sexo: m.sexo ?? '',
    fechaNacimiento: m.fechaNacimiento ?? '',
    pesoKg: m.pesoKg ?? '',
    observaciones: m.observaciones ?? '',
  };
}

/**
 * Campos compartidos por "registrar" y "editar".
 * El componente no sabe nada de HTTP: recibe los valores iniciales y entrega
 * el cuerpo ya listo (numeros como numeros, vacios como null) a onEnviar.
 */
export default function FormularioMascota({
  valorInicial = FORMULARIO_VACIO,
  errores = {},
  enviando = false,
  textoBoton = 'Guardar',
  onEnviar,
  children,
}) {
  const [form, setForm] = useState(valorInicial);
  const hoy = new Date().toISOString().slice(0, 10);
  const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  function enviar(e) {
    e.preventDefault();
    onEnviar({
      nombre: form.nombre.trim(),
      especie: form.especie || null,
      raza: form.raza.trim() || null,
      sexo: form.sexo || null,
      fechaNacimiento: form.fechaNacimiento || null,
      pesoKg: form.pesoKg === '' ? null : Number(form.pesoKg),
      observaciones: form.observaciones.trim() || null,
    });
  }

  return (
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

      <div className="flex flex-col gap-3 sm:flex-row-reverse">
        <Boton cargando={enviando}>{textoBoton}</Boton>
        {children}
      </div>
    </form>
  );
}
