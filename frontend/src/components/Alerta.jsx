// Mensaje general del formulario (p. ej. "Correo o contrasena incorrectos").
// role="alert" hace que los lectores de pantalla lo anuncien al aparecer.

const ESTILOS = {
  error: 'border-red-300 bg-red-50 text-red-800',
  exito: 'border-teal-300 bg-teal-50 text-teal-800',
};

export default function Alerta({ tipo = 'error', children }) {
  if (!children) return null;
  return (
    <div role="alert" className={`mb-6 rounded-md border px-4 py-3 text-sm ${ESTILOS[tipo]}`}>
      {children}
    </div>
  );
}
