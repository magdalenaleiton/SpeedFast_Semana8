package speedfast;

public class PedidoComida extends Pedido {

    public PedidoComida(int numero, String direccion, double distanciaKm) {
        super(numero, direccion, distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        if (cancelado) {
            System.out.println("No se puede asignar repartidor: pedido cancelado.");
            return;
        }
        repartidorAsignado = distanciaKm <= 5
                ? "Luis Díaz"
                : "Camila Rojas";
        System.out.println("Asignación automática (Comida): " + repartidorAsignado);
    }

    @Override
    public int calcularTiempoEntrega() {
        return 20 + (int) Math.ceil(distanciaKm * 3);
    }
}
