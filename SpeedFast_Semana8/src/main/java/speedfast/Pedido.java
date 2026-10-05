package speedfast;

/**
 * Clase abstracta base para todos los tipos de pedido de SpeedFast.
 * Mantiene la funcionalidad de las semanas anteriores y agrega el estado
 * controlado por enum requerido para la simulacion concurrente.
 */
public abstract class Pedido implements Despachable, Cancelable, Rastreable {

    protected int numero;
    protected String direccion;
    protected double distanciaKm;
    protected String repartidorAsignado;
    protected boolean reservado;
    protected boolean despachado;
    protected boolean cancelado;

    // Atributos requeridos para la actividad de Semana 5.
    private EstadoPedido estado;

    public Pedido(int numero, String direccion, double distanciaKm) {
        this.numero = numero;
        this.direccion = direccion;
        this.distanciaKm = distanciaKm;
        this.repartidorAsignado = "Sin asignar";
        this.reservado = false;
        this.despachado = false;
        this.cancelado = false;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getNumero() {
        return numero;
    }

    /** Alias solicitado por la actividad de Semana 5. */
    public int getId() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    /** Alias solicitado por la actividad de Semana 5. */
    public void setId(int id) {
        this.numero = id;
    }

    public String getDireccion() {
        return direccion;
    }

    /** Alias solicitado por la actividad de Semana 5. */
    public String getDireccionEntrega() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** Alias solicitado por la actividad de Semana 5. */
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccion = direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public void setRepartidorAsignado(String repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }

    public boolean isReservado() {
        return reservado;
    }

    public boolean isDespachado() {
        return despachado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser null.");
        }
        this.estado = nuevoEstado;
    }

    /**
     * Metodo solicitado por la pauta. Convierte el texto a un valor del enum.
     */
    public void setEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El estado no puede ser vacio.");
        }
        try {
            this.estado = EstadoPedido.valueOf(nuevoEstado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Estado no valido: " + nuevoEstado, ex);
        }
    }

    /**
     * Sobrecarga: asignacion manual de repartidor.
     */
    public void asignarRepartidor(String nombre) {
        if (cancelado) {
            System.out.println("No se puede asignar un repartidor a un pedido cancelado.");
            return;
        }
        repartidorAsignado = nombre;
        System.out.println("Repartidor asignado manualmente: " + nombre);
    }

    /** Asignacion automatica, sobrescrita por cada subclase. */
    public abstract void asignarRepartidor();

    /** Cada subclase calcula el tiempo segun su propia logica. */
    public abstract int calcularTiempoEntrega();

    /**
     * Resumen reutilizable del pedido.
     */
    public void mostrarResumen() {
        System.out.println("Pedido #" + numero);
        System.out.println("Tipo: " + getClass().getSimpleName());
        System.out.println("Direccion: " + direccion);
        System.out.printf("Distancia: %.1f km%n", distanciaKm);
        System.out.println("Repartidor asignado: " + repartidorAsignado);
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
        System.out.println("Estado: " + obtenerEstadoLegible());
    }

    public void reservar() {
        if (cancelado) {
            System.out.println("No se puede reservar un pedido cancelado.");
            return;
        }
        reservado = true;
        System.out.println("Pedido #" + numero + " reservado correctamente.");
    }

    private String obtenerEstadoLegible() {
        if (cancelado) return "Cancelado";
        if (estado == EstadoPedido.ENTREGADO) return "ENTREGADO";
        if (estado == EstadoPedido.EN_REPARTO) return "EN_REPARTO";
        if (despachado) return "Despachado";
        if (reservado) return "Reservado";
        return "PENDIENTE";
    }

    @Override
    public void despachar() {
        if (cancelado) {
            System.out.println("No se puede despachar el pedido #" + numero + " porque esta cancelado.");
            return;
        }
        if ("Sin asignar".equals(repartidorAsignado)) {
            System.out.println("No se puede despachar el pedido #" + numero + " sin repartidor asignado.");
            return;
        }
        despachado = true;
        System.out.println("Pedido #" + numero + " despachado correctamente.");
    }

    @Override
    public void cancelar() {
        if (despachado) {
            System.out.println("El pedido #" + numero + " ya fue despachado y no puede cancelarse.");
            return;
        }
        if (cancelado) {
            System.out.println("El pedido #" + numero + " ya estaba cancelado.");
            return;
        }
        cancelado = true;
        System.out.println("Pedido #" + numero + " cancelado exitosamente.");
    }

    @Override
    public void verHistorial() {
        System.out.println("Pedido #" + numero + " - " + getClass().getSimpleName()
                + " - repartidor: " + repartidorAsignado);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + numero +
                ", direccionEntrega='" + direccion + '\'' +
                ", estado=" + estado +
                ", tipo='" + getClass().getSimpleName() + '\'' +
                '}';
    }
}
