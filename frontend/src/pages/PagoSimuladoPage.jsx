import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import cliente from '../api/cliente';
import { mensajeError } from '../api/errores';
import { formatearPrecio } from '../api/formato';
import Boton from '../components/Boton';
import Alerta from '../components/Alerta';

/**
 * Pantalla que HACE DE PASARELA. No es parte del producto: imita lo que el
 * cliente veria en Wompi para poder probar el ciclo completo sin credenciales.
 * Con PAGOS_PROVEEDOR=wompi el endpoint que usa no existe y esta pagina deja
 * de funcionar, que es justo lo que debe pasar.
 */
export default function PagoSimuladoPage() {
  const [params] = useSearchParams();
  const ref = params.get('ref');
  const monto = params.get('monto');
  const retorno = params.get('retorno') || '/reservas';

  const [enviando, setEnviando] = useState(null);   // 'APROBADO' | 'RECHAZADO'
  const [resultado, setResultado] = useState(null);
  const [errorGeneral, setErrorGeneral] = useState('');

  async function decidir(estado) {
    setEnviando(estado);
    setErrorGeneral('');
    try {
      await cliente.post('/webhooks/pagos/simular', { referencia: ref, estado });
      setResultado(estado);
    } catch (error) {
      setErrorGeneral(mensajeError(error));
    } finally {
      setEnviando(null);
    }
  }

  if (!ref) {
    return <p className="p-8 text-center text-neutral-600">Falta la referencia de pago en la URL.</p>;
  }

  return (
    <div className="flex min-h-dvh items-center justify-center bg-neutral-100 px-4">
      <div className="w-full max-w-md rounded-lg border border-neutral-300 bg-white p-6 shadow-sm">
        <p className="text-xs font-bold uppercase tracking-wide text-neutral-500">
          Pasarela de pagos · simulador
        </p>
        <h1 className="mt-2 text-2xl font-extrabold text-neutral-900">
          {monto ? formatearPrecio(monto) : 'Pago'}
        </h1>
        <p className="mt-1 break-all text-sm text-neutral-600">Referencia: {ref}</p>

        <div className="mt-6"><Alerta>{errorGeneral}</Alerta></div>

        {resultado ? (
          <div className="space-y-4">
            <Alerta tipo={resultado === 'APROBADO' ? 'exito' : 'error'}>
              {resultado === 'APROBADO'
                ? 'Pago aprobado. Tu reserva quedó confirmada.'
                : 'Pago rechazado. Puedes intentar de nuevo desde tus reservas.'}
            </Alerta>
            <a href={retorno}
               className="inline-flex min-h-12 w-full items-center justify-center rounded-md bg-neutral-900 px-4 font-semibold text-white">
              Volver a PetCare
            </a>
          </div>
        ) : (
          <div className="space-y-3">
            <Boton type="button" cargando={enviando === 'APROBADO'} onClick={() => decidir('APROBADO')}>
              Simular pago aprobado
            </Boton>
            <Boton type="button" variante="secundario" cargando={enviando === 'RECHAZADO'}
                   onClick={() => decidir('RECHAZADO')}>
              Simular pago rechazado
            </Boton>
          </div>
        )}
      </div>
    </div>
  );
}
