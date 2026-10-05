package speedfast;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Punto de entrada de la simulacion concurrente de SpeedFast.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("============================================================");
        System.out.println("       SPEEDFAST - SEMANA 5: PROCESOS CONCURRENTES");
        System.out.println("============================================================");
        System.out.println("Recurso compartido: Zona de carga");
        System.out.println("Repartidores concurrentes: Juan, Camila y Pedro");

        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        // Se utilizan las subclases de la Semana 3 para mantener la estructura POO.
        List<Pedido> pedidos = Arrays.asList(
                new PedidoComida(1, "Santiago Centro", 4.0),
                new PedidoEncomienda(2, "Providencia", 7.0),
                new PedidoExpress(3, "Nunoa", 3.0),
                new PedidoComida(4, "Recoleta", 5.0),
                new PedidoEncomienda(5, "Las Condes", 8.0)
        );

        System.out.println("\n--- CARGA DE PEDIDOS ---");
        for (Pedido pedido : pedidos) {
            zonaDeCarga.agregarPedido(pedido);
        }

        System.out.println("\n--- ESTADO INICIAL ---");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("\n--- INICIO DE REPARTIDORES ---");
        executor.submit(new Repartidor("Juan", zonaDeCarga));
        executor.submit(new Repartidor("Camila", zonaDeCarga));
        executor.submit(new Repartidor("Pedro", zonaDeCarga));

        executor.shutdown();

        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.out.println("El proceso supero el tiempo maximo de espera.");
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
            System.out.println("La ejecucion principal fue interrumpida.");
            return;
        }

        System.out.println("\n--- VALIDACION FINAL ---");
        boolean todosEntregados = true;

        for (Pedido pedido : pedidos) {
            System.out.println("Pedido #" + pedido.getId()
                    + " -> " + pedido.getEstado()
                    + " | Repartidor: " + pedido.getRepartidorAsignado());

            if (pedido.getEstado() != EstadoPedido.ENTREGADO) {
                todosEntregados = false;
            }
        }

        boolean zonaVacia = zonaDeCarga.cantidadPedidos() == 0;

        System.out.println("Pedidos pendientes en zona de carga: "
                + zonaDeCarga.cantidadPedidos());

        System.out.println("\n============================================================");
        if (todosEntregados && zonaVacia) {
            System.out.println("Todos los pedidos han sido entregados correctamente");
        } else {
            System.out.println("ERROR: La validacion final detecto pedidos no entregados.");
        }
        System.out.println("============================================================");
    }
}
