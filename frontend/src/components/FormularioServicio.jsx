import { useState } from 'react';
import CampoTexto from './CampoTexto';
import CampoSelect from './CampoSelect';
import CampoArea from './CampoArea';
import Boton from './Boton';

export const UNIDADES = [
  { valor: 'POR_SERVICIO', texto: 'Por servicio' },
  { valor: 'POR_DIA', texto: 'Por día (noche)' },
];

// Los campos que el formulario sabe mostrar. Se pasa a separarErrores() para
// que un mensaje con otro nombre no se pierda.
export const CAMPOS_SERVICIO = [
  'nombre', 'descripcion', 'precio', 'unidadCobro', 'duracionMinutos',
];

export const SERVICIO_VACIO = {
  nombre: '', descripcion: '', precio: '', unidadCobro: '', duracionMinutos: '',
};

export function desdeServicio(s) {
  return {
    nombre: s.nombre ?? '',
    descripcion: s.descripcion ?? '',
    precio: s.precio ?? '',
    unidadCobro: s.unidadCobro ?? '',
    duracionMinutos: s.duracionMinutos ?? '',
  };
}

export default function FormularioServicio({
  valorInicial = SERVICIO_VACIO, errores = {}, enviando = false,
  textoBoton = 'Guardar', onEnviar, children,
}) {
  const [form, setForm] = useState(valorInicial);
  const porDia = form.unidadCobro === 'POR_DIA';
  const cambiar = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  function enviar(e) {
    e.preventDefault();
    onEnviar({
      nombre: form.nombre.trim(),
      descripcion: form.descripcion.trim() || null,
      precio: form.precio === '' ? null : Number(form.precio),
      unidadCobro: form.unidadCobro || null,
      // Un servicio por dia no ocupa franjas: su duracion viaja como null
      // aunque el campo tuviera un valor de antes.
      duracionMinutos: porDia || form.duracionMinutos === '' ? null : Number(form.duracionMinutos),
    });
  }

  return (
    <form onSubmit={enviar} noValidate className="space-y-5 rounded-lg border border-neutral-200 bg-white p-5">
      <CampoTexto etiqueta="Nombre" name="nombre" value={form.nombre}
        onChange={cambiar} error={errores.nombre} />

      <CampoArea etiqueta="Descripción (opcional)" name="descripcion" maxLength={400}
        value={form.descripcion} onChange={cambiar} error={errores.descripcion} />

      <div className="grid gap-5 sm:grid-cols-2">
        <CampoTexto etiqueta="Precio (COP)" name="precio" tipo="number" step="1" min="0"
          inputMode="numeric" value={form.precio} onChange={cambiar} error={errores.precio} />
        <CampoSelect etiqueta="Unidad de cobro" name="unidadCobro" opciones={UNIDADES}
          value={form.unidadCobro} onChange={cambiar} error={errores.unidadCobro} />
      </div>

      {!porDia && (
        <CampoTexto etiqueta="Duración (minutos)" name="duracionMinutos" tipo="number"
          step="15" min="15" max="480" inputMode="numeric"
          value={form.duracionMinutos} onChange={cambiar} error={errores.duracionMinutos} />
      )}

      <div className="flex flex-col gap-3 sm:flex-row-reverse">
        <Boton cargando={enviando}>{textoBoton}</Boton>
        {children}
      </div>
    </form>
  );
}
