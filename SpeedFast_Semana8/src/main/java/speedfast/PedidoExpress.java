package speedfast;

public class PedidoExpress extends Pedido {

    public PedidoExpress(int numero, String direccion, double distanciaKm) {
        super(numero, direccion, distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        if (cancelado) {
            System.out.println("No se puede asignar repartidor: pedido cancelado.");
            return;
        }
        repartidorAsignado = distanciaKm <= 4
                ? "Sofía Pérez"
                : "Matías Silva";
        System.out.println("Asignación automática (Express): " + repartidorAsignado);
    }

    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) Math.ceil(distanciaKm * 2);
    }
}
