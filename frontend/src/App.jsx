import { useEffect, useState } from 'react'
import cliente from './api/cliente'

/**
 * Pantalla temporal de verificacion.
 *
 * No es parte del producto: su unico trabajo es demostrar que la cadena
 * React -> Axios -> Spring Boot -> MySQL funciona de extremo a extremo.
 * Se reemplaza en cuanto empecemos con el registro de usuarios (HU-007).
 */
export default function App() {
  const [backend, setBackend] = useState({ estado: 'consultando' })
  const [baseDatos, setBaseDatos] = useState({ estado: 'consultando' })

  useEffect(() => {
    cliente.get('/ping')
      .then((r) => setBackend({ estado: 'ok', datos: r.data }))
      .catch((e) => setBackend({ estado: 'error', mensaje: e.message }))

    cliente.get('/ping/db')
      .then((r) => setBaseDatos({ estado: 'ok', datos: r.data }))
      .catch((e) => setBaseDatos({ estado: 'error', mensaje: e.message }))
  }, [])

  return (
    <main className="min-h-screen bg-slate-50 px-4 py-10">
      <div className="mx-auto w-full max-w-xl">
        <h1 className="text-2xl font-semibold text-slate-900">PetCare</h1>
        <p className="mt-1 text-sm text-slate-500">
          Verificacion del entorno de desarrollo
        </p>

        <div className="mt-6 space-y-3">
          <Tarjeta titulo="Frontend — React + Vite" resultado={{ estado: 'ok' }} />
          <Tarjeta titulo="Backend — Spring Boot" resultado={backend} />
          <Tarjeta titulo="Base de datos — MySQL" resultado={baseDatos} />
        </div>
      </div>
    </main>
  )
}

function Tarjeta({ titulo, resultado }) {
  const estilos = {
    ok: 'border-emerald-200 bg-emerald-50 text-emerald-800',
    error: 'border-red-200 bg-red-50 text-red-800',
    consultando: 'border-slate-200 bg-white text-slate-500',
  }[resultado.estado]

  const icono = { ok: '✓', error: '✕', consultando: '…' }[resultado.estado]

  return (
    <div className={`rounded-lg border p-4 ${estilos}`}>
      <div className="flex items-center justify-between gap-3">
        <span className="font-medium">{titulo}</span>
        <span className="text-lg" aria-hidden="true">{icono}</span>
      </div>

      {resultado.datos && (
        <pre className="mt-2 overflow-x-auto text-xs opacity-80">
          {JSON.stringify(resultado.datos, null, 2)}
        </pre>
      )}
      {resultado.mensaje && (
        <p className="mt-2 text-xs opacity-80">{resultado.mensaje}</p>
      )}
    </div>
  )
}
