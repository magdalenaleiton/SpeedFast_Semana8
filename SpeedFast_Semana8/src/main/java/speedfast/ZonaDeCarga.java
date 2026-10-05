package speedfast;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Recurso compartido que contiene los pedidos pendientes.
 *
 * La operacion de retiro y cambio de estado se realiza de forma atomica
 * dentro del metodo sincronizado para impedir que dos hilos obtengan
 * el mismo pedido.
 */
public class ZonaDeCarga {

    private final BlockingQueue<Pedido> pedidos;

    public ZonaDeCarga() {
        pedidos = new LinkedBlockingQueue<>();
        System.out.println("[Zona de carga inicializada]");
    }

    /**
     * Agrega un pedido pendiente a la zona de carga.
     */
    public synchronized void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("No se puede agregar un pedido null.");
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedidos.offer(pedido);

        System.out.println("Pedido #" + pedido.getId()
                + " agregado. Destino: " + pedido.getDireccionEntrega());
    }

    /**
     * Retira exactamente un pedido y lo deja en EN_REPARTO dentro de la
     * misma seccion critica. Esto evita el retiro doble y la condicion de carrera.
     */
    public synchronized Pedido retirarPedido() {
        Pedido pedido = pedidos.poll();

        if (pedido != null && pedido.getEstado() == EstadoPedido.PENDIENTE) {
            pedido.setEstado(EstadoPedido.EN_REPARTO);
            return pedido;
        }

        return null;
    }

    public synchronized int cantidadPedidos() {
        return pedidos.size();
    }
}
