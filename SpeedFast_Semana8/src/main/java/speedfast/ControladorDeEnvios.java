package speedfast;

import java.util.ArrayList;
import java.util.List;

/**
 * Centraliza operaciones del sistema y mantiene un historial de entregas.
 * También implementa las interfaces para demostrar desacoplamiento
 * de responsabilidades.
 */
public class ControladorDeEnvios implements Despachable, Cancelable, Rastreable {

    private final List<Pedido> pedidos;
    private final List<Pedido> historialEntregas;

    public ControladorDeEnvios() {
        pedidos = new ArrayList<>();
        historialEntregas = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public void reservarPedido(Pedido pedido) {
        pedido.reservar();
    }

    public void despacharPedido(Pedido pedido) {
        pedido.despachar();
        if (pedido.isDespachado() && !historialEntregas.contains(pedido)) {
            historialEntregas.add(pedido);
        }
    }

    public void cancelarPedido(Pedido pedido) {
        pedido.cancelar();
    }

    @Override
    public void despachar() {
        for (Pedido pedido : pedidos) {
            if (!pedido.isCancelado() && !pedido.isDespachado()) {
                pedido.despachar();
                if (pedido.isDespachado() && !historialEntregas.contains(pedido)) {
                    historialEntregas.add(pedido);
                }
            }
        }
    }

    @Override
    public void cancelar() {
        for (Pedido pedido : pedidos) {
            if (!pedido.isDespachado() && !pedido.isCancelado()) {
                pedido.cancelar();
            }
        }
    }

    @Override
    public void verHistorial() {
        System.out.println("\n===== HISTORIAL DE ENTREGAS =====");
        if (historialEntregas.isEmpty()) {
            System.out.println("No existen entregas registradas.");
            return;
        }

        for (Pedido pedido : historialEntregas) {
            System.out.println("- " + pedido.getClass().getSimpleName()
                    + " #" + pedido.getNumero()
                    + " - entregado por " + pedido.getRepartidorAsignado());
        }
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
