// Diseno comun para las pantallas de autenticacion (login y registro).
// Mobile-first: columna de ancho completo con margen de 16 px en movil,
// que se limita a 420 px y se centra a partir de pantallas medianas.

function LogoPetCare() {
  return (
    <span className="inline-flex items-center gap-2 text-neutral-900">
      <svg viewBox="0 0 24 24" className="h-7 w-7 text-teal-600" fill="currentColor" aria-hidden="true">
        <circle cx="5.5" cy="9" r="2.2" />
        <circle cx="9.5" cy="5" r="2.2" />
        <circle cx="14.5" cy="5" r="2.2" />
        <circle cx="18.5" cy="9" r="2.2" />
        <path d="M12 10.5c-3.2 0-6 3.6-6 6.3 0 1.9 1.5 2.7 3.2 2.7 1.2 0 1.9-.6 2.8-.6s1.6.6 2.8.6c1.7 0 3.2-.8 3.2-2.7 0-2.7-2.8-6.3-6-6.3z" />
      </svg>
      <span className="text-xl font-extrabold tracking-tight">PetCare</span>
    </span>
  );
}

export default function DisenoAuth({ titulo, subtitulo, children }) {
  return (
    <div className="flex min-h-dvh flex-col bg-white">
      <header className="border-b border-neutral-200 px-4 py-5 sm:px-8 sm:py-7">
        <LogoPetCare />
      </header>

      <main className="flex flex-1 justify-center px-4 py-10 sm:py-16">
        <div className="w-full max-w-[420px]">
          <h1 className="text-2xl font-extrabold tracking-tight text-neutral-900 sm:text-3xl">
            {titulo}
          </h1>
          {subtitulo && <p className="mt-1 text-neutral-700">{subtitulo}</p>}
          <div className="mt-8">{children}</div>
        </div>
      </main>

      <footer className="px-4 py-6 text-center text-sm text-neutral-500">
        © {new Date().getFullYear()} PetCare
      </footer>
    </div>
  );
}
