package speedfast;

public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int numero, String direccion, double distanciaKm) {
        super(numero, direccion, distanciaKm);
    }

    @Override
    public void asignarRepartidor() {
        if (cancelado) {
            System.out.println("No se puede asignar repartidor: pedido cancelado.");
            return;
        }
        repartidorAsignado = distanciaKm <= 8
                ? "Daniela Tapia"
                : "Jorge Muñoz";
        System.out.println("Asignación automática (Encomienda): " + repartidorAsignado);
    }

    @Override
    public int calcularTiempoEntrega() {
        return 25 + (int) Math.ceil(distanciaKm * 2);
    }
}
