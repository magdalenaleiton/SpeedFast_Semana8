package speedfast;

/**
 * Representa a un repartidor que ejecuta su trabajo en un hilo independiente.
 */
public class Repartidor implements Runnable {

    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del repartidor es obligatorio.");
        }
        if (zonaDeCarga == null) {
            throw new IllegalArgumentException("La zona de carga es obligatoria.");
        }
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        while (true) {
            Pedido pedido = zonaDeCarga.retirarPedido();

            if (pedido == null) {
                break;
            }

            pedido.setRepartidorAsignado(nombre);

            System.out.println("[Repartidor - " + nombre + "] Retirando pedido #"
                    + pedido.getId() + "...");
            System.out.println("[Repartidor - " + nombre + "] Estado: "
                    + pedido.getEstado());
            System.out.println("[Repartidor - " + nombre + "] Entregando pedido #"
                    + pedido.getId() + "...");

            try {
                // Duraciones distintas ayudan a evidenciar la ejecucion concurrente.
                long espera = 700L + (pedido.getId() % 3) * 300L;
                Thread.sleep(espera);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.out.println("[Repartidor - " + nombre
                        + "] Hilo interrumpido durante la entrega del pedido #"
                        + pedido.getId() + ".");
                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);
            System.out.println("[Repartidor - " + nombre + "] Estado: "
                    + pedido.getEstado());
        }

        System.out.println("[Repartidor - " + nombre + "] No quedan pedidos pendientes.");
    }
}
