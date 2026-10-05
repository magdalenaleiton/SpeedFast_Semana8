package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana principal para gestionar repartidores, pedidos y entregas.
 */
public class VentanaPrincipal extends JFrame {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    private final DefaultTableModel modeloRep =
            model("ID", "Nombre");

    private final DefaultTableModel modeloPed =
            model("ID", "Direccion", "Tipo", "Estado");

    private final DefaultTableModel modeloEnt =
            model("ID", "Pedido", "Repartidor", "Fecha", "Hora");

    private final JTable tablaRep = new JTable(modeloRep);
    private final JTable tablaPed = new JTable(modeloPed);
    private final JTable tablaEnt = new JTable(modeloEnt);

    private final JTextField txtRepId = new JTextField();
    private final JTextField txtRepNombre = new JTextField();

    private final JTextField txtPedId = new JTextField();
    private final JTextField txtDireccion = new JTextField();

    private final JComboBox<String> cmbTipo =
            new JComboBox<>(new String[]{
                    "COMIDA",
                    "ENCOMIENDA",
                    "EXPRESS"
            });

    private final JComboBox<String> cmbEstado =
            new JComboBox<>(new String[]{
                    "PENDIENTE",
                    "EN_REPARTO",
                    "ENTREGADO"
            });

    private final JTextField txtEntId = new JTextField();

    private final JComboBox<Repartidor> cmbRepartidor =
            new JComboBox<>();

    private final JComboBox<PedidoItem> cmbPedido =
            new JComboBox<>();

    private final JTextField txtFecha =
            new JTextField(LocalDate.now().toString());

    private final JTextField txtHora =
            new JTextField(LocalTime.now().withNano(0).toString());

    public VentanaPrincipal() {

        setTitle("SpeedFast - Gestion de pedidos y entregas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "SPEEDFAST - Gestion CRUD",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        );

        add(titulo, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Repartidores", panelRepartidores());
        tabs.addTab("Pedidos", panelPedidos());
        tabs.addTab("Entregas", panelEntregas());

        add(tabs, BorderLayout.CENTER);

        txtRepId.setEditable(false);
        txtPedId.setEditable(false);
        txtEntId.setEditable(false);

        refrescarTodo();
    }

    private JPanel panelRepartidores() {

        JPanel root = new JPanel(new BorderLayout(8, 8));

        root.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JPanel form = new JPanel(
                new GridLayout(2, 4, 8, 8)
        );

        form.add(new JLabel("ID:"));
        form.add(txtRepId);

        form.add(new JLabel("Nombre:"));
        form.add(txtRepNombre);

        JButton crear = new JButton("Crear");
        JButton actualizar = new JButton("Actualizar");
        JButton eliminar = new JButton("Eliminar");
        JButton limpiar = new JButton("Limpiar");

        form.add(crear);
        form.add(actualizar);
        form.add(eliminar);
        form.add(limpiar);

        root.add(form, BorderLayout.NORTH);
        root.add(
                new JScrollPane(tablaRep),
                BorderLayout.CENTER
        );

        crear.addActionListener(e -> crearRepartidor());
        actualizar.addActionListener(e -> actualizarRepartidor());
        eliminar.addActionListener(e -> eliminarRepartidor());
        limpiar.addActionListener(e -> limpiarRep());

        tablaRep.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaRep.getSelectedRow() >= 0) {

                int fila = tablaRep.getSelectedRow();

                txtRepId.setText(
                        modeloRep.getValueAt(fila, 0).toString()
                );

                txtRepNombre.setText(
                        modeloRep.getValueAt(fila, 1).toString()
                );
            }
        });

        return root;
    }

    private JPanel panelPedidos() {

        JPanel root = new JPanel(new BorderLayout(8, 8));

        root.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JPanel form = new JPanel(
                new GridLayout(3, 4, 8, 8)
        );

        form.add(new JLabel("ID:"));
        form.add(txtPedId);

        form.add(new JLabel("Direccion:"));
        form.add(txtDireccion);

        form.add(new JLabel("Tipo:"));
        form.add(cmbTipo);

        form.add(new JLabel("Estado:"));
        form.add(cmbEstado);

        JButton crear = new JButton("Crear");
        JButton actualizar = new JButton("Actualizar");
        JButton eliminar = new JButton("Eliminar");
        JButton limpiar = new JButton("Limpiar");

        form.add(crear);
        form.add(actualizar);
        form.add(eliminar);
        form.add(limpiar);

        root.add(form, BorderLayout.NORTH);

        root.add(
                new JScrollPane(tablaPed),
                BorderLayout.CENTER
        );

        JPanel filtros = new JPanel();

        JComboBox<String> fEstado =
                new JComboBox<>(new String[]{
                        "Todos",
                        "PENDIENTE",
                        "EN_REPARTO",
                        "ENTREGADO"
                });

        JComboBox<String> fTipo =
                new JComboBox<>(new String[]{
                        "Todos",
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                });

        JButton filtrar = new JButton("Filtrar");

        filtros.add(new JLabel("Estado:"));
        filtros.add(fEstado);

        filtros.add(new JLabel("Tipo:"));
        filtros.add(fTipo);

        filtros.add(filtrar);

        root.add(filtros, BorderLayout.SOUTH);

        crear.addActionListener(e -> crearPedido());
        actualizar.addActionListener(e -> actualizarPedido());
        eliminar.addActionListener(e -> eliminarPedido());
        limpiar.addActionListener(e -> limpiarPed());

        filtrar.addActionListener(e -> {

            String estado = "Todos".equals(
                    fEstado.getSelectedItem()
            )
                    ? null
                    : (String) fEstado.getSelectedItem();

            String tipo = "Todos".equals(
                    fTipo.getSelectedItem()
            )
                    ? null
                    : (String) fTipo.getSelectedItem();

            cargarPedidos(estado, tipo);
        });

        tablaPed.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaPed.getSelectedRow() >= 0) {

                int fila = tablaPed.getSelectedRow();

                txtPedId.setText(
                        modeloPed.getValueAt(fila, 0).toString()
                );

                txtDireccion.setText(
                        modeloPed.getValueAt(fila, 1).toString()
                );

                cmbTipo.setSelectedItem(
                        modeloPed.getValueAt(fila, 2)
                );

                cmbEstado.setSelectedItem(
                        modeloPed.getValueAt(fila, 3)
                );
            }
        });

        return root;
    }

    private JPanel panelEntregas() {

        JPanel root = new JPanel(new BorderLayout(8, 8));

        root.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JPanel form = new JPanel(
                new GridLayout(3, 4, 8, 8)
        );

        form.add(new JLabel("ID:"));
        form.add(txtEntId);

        form.add(new JLabel("Pedido:"));
        form.add(cmbPedido);

        form.add(new JLabel("Repartidor:"));
        form.add(cmbRepartidor);

        form.add(new JLabel("Fecha (AAAA-MM-DD):"));
        form.add(txtFecha);

        form.add(new JLabel("Hora (HH:MM:SS):"));
        form.add(txtHora);

        JButton crear = new JButton("Crear");
        JButton actualizar = new JButton("Actualizar");
        JButton eliminar = new JButton("Eliminar");
        JButton limpiar = new JButton("Limpiar");

        form.add(crear);
        form.add(actualizar);
        form.add(eliminar);
        form.add(limpiar);

        root.add(form, BorderLayout.NORTH);

        root.add(
                new JScrollPane(tablaEnt),
                BorderLayout.CENTER
        );

        crear.addActionListener(e -> crearEntrega());
        actualizar.addActionListener(e -> actualizarEntrega());
        eliminar.addActionListener(e -> eliminarEntrega());
        limpiar.addActionListener(e -> limpiarEnt());

        tablaEnt.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaEnt.getSelectedRow() >= 0) {

                int fila = tablaEnt.getSelectedRow();

                txtEntId.setText(
                        modeloEnt.getValueAt(fila, 0).toString()
                );

                seleccionarPedido(
                        Integer.parseInt(
                                modeloEnt.getValueAt(fila, 1).toString()
                        )
                );

                seleccionarRepartidor(
                        Integer.parseInt(
                                modeloEnt.getValueAt(fila, 2).toString()
                        )
                );

                txtFecha.setText(
                        modeloEnt.getValueAt(fila, 3).toString()
                );

                txtHora.setText(
                        modeloEnt.getValueAt(fila, 4).toString()
                );
            }
        });

        return root;
    }

    private static DefaultTableModel model(String... columnas) {

        return new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void crearRepartidor() {

        try {

            String nombre = txtRepNombre.getText().trim();

            if (nombre.isEmpty()) {
                error("El nombre es obligatorio.");
                return;
            }

            repartidorDAO.create(
                    new Repartidor(nombre)
            );

            ok("Repartidor creado correctamente.");

            limpiarRep();
            refrescarTodo();

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void actualizarRepartidor() {

        try {

            if (txtRepId.getText().isEmpty()) {
                error("Seleccione un repartidor.");
                return;
            }

            String nombre = txtRepNombre.getText().trim();

            if (nombre.isEmpty()) {
                error("El nombre es obligatorio.");
                return;
            }

            repartidorDAO.update(
                    new Repartidor(
                            Integer.parseInt(txtRepId.getText()),
                            nombre
                    )
            );

            ok("Repartidor actualizado.");

            refrescarTodo();

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void eliminarRepartidor() {

        try {

            if (txtRepId.getText().isEmpty()) {
                error("Seleccione un repartidor.");
                return;
            }

            if (!confirmarEliminacion(
                    "¿Eliminar el repartidor seleccionado?"
            )) {
                return;
            }

            repartidorDAO.delete(
                    Integer.parseInt(txtRepId.getText())
            );

            ok("Repartidor eliminado.");

            limpiarRep();
            refrescarTodo();

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void crearPedido() {

        try {

            String direccion =
                    txtDireccion.getText().trim();

            if (direccion.isEmpty()) {
                error("La direccion es obligatoria.");
                return;
            }

            pedidoDAO.create(
                    new Pedido(
                            direccion,
                            (String) cmbTipo.getSelectedItem(),
                            (String) cmbEstado.getSelectedItem()
                    )
            );

            ok("Pedido creado.");

            limpiarPed();
            refrescarTodo();

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void actualizarPedido() {

        try {

            if (txtPedId.getText().isEmpty()) {
                error("Seleccione un pedido.");
                return;
            }

            String direccion =
                    txtDireccion.getText().trim();

            if (direccion.isEmpty()) {
                error("La direccion es obligatoria.");
                return;
            }

            pedidoDAO.update(
                    new Pedido(
                            Integer.parseInt(txtPedId.getText()),
                            direccion,
                            (String) cmbTipo.getSelectedItem(),
                            (String) cmbEstado.getSelectedItem()
                    )
            );

            ok("Pedido actualizado.");

            refrescarTodo();

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void eliminarPedido() {

        try {

            if (txtPedId.getText().isEmpty()) {
                error("Seleccione un pedido.");
                return;
            }

            if (!confirmarEliminacion(
                    "¿Eliminar el pedido seleccionado?"
            )) {
                return;
            }

            pedidoDAO.delete(
                    Integer.parseInt(txtPedId.getText())
            );

            ok("Pedido eliminado.");

            limpiarPed();
            refrescarTodo();

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void crearEntrega() {

        try {

            Entrega entrega = leerEntrega();

            entregaDAO.create(entrega);

            ok("Entrega creada.");

            limpiarEnt();
            refrescarTodo();

        } catch (Exception ex) {
            manejar(ex);
        }
    }

    private void actualizarEntrega() {

        try {

            if (txtEntId.getText().isEmpty()) {
                error("Seleccione una entrega.");
                return;
            }

            Entrega entrega = leerEntrega();

            entrega.setId(
                    Integer.parseInt(txtEntId.getText())
            );

            entregaDAO.update(entrega);

            ok("Entrega actualizada.");

            refrescarTodo();

        } catch (Exception ex) {
            manejar(ex);
        }
    }

    private void eliminarEntrega() {

        try {

            if (txtEntId.getText().isEmpty()) {
                error("Seleccione una entrega.");
                return;
            }

            if (!confirmarEliminacion(
                    "¿Eliminar la entrega seleccionada?"
            )) {
                return;
            }

            entregaDAO.delete(
                    Integer.parseInt(txtEntId.getText())
            );

            ok("Entrega eliminada.");

            limpiarEnt();
            refrescarTodo();

        } catch (Exception ex) {
            manejar(ex);
        }
    }

    private boolean confirmarEliminacion(String mensaje) {

        Object[] opciones = {"Si", "No"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                mensaje,
                "Confirmar eliminacion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]
        );

        return respuesta == 0;
    }

    private Entrega leerEntrega() {

        PedidoItem pedido =
                (PedidoItem) cmbPedido.getSelectedItem();

        Repartidor repartidor =
                (Repartidor) cmbRepartidor.getSelectedItem();

        if (pedido == null || repartidor == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar pedido y repartidor."
            );
        }

        try {

            return new Entrega(
                    pedido.id,
                    repartidor.getId(),
                    LocalDate.parse(
                            txtFecha.getText().trim()
                    ),
                    LocalTime.parse(
                            txtHora.getText().trim()
                    )
            );

        } catch (DateTimeParseException ex) {

            throw new IllegalArgumentException(
                    "Fecha u hora no tienen formato valido."
            );
        }
    }

    private void refrescarTodo() {

        cargarRepartidores();
        cargarPedidos(null, null);
        cargarEntregas();
    }

    private void cargarRepartidores() {

        try {

            List<Repartidor> lista =
                    repartidorDAO.readAll();

            modeloRep.setRowCount(0);
            cmbRepartidor.removeAllItems();

            for (Repartidor repartidor : lista) {

                modeloRep.addRow(
                        new Object[]{
                                repartidor.getId(),
                                repartidor.getNombre()
                        }
                );

                cmbRepartidor.addItem(repartidor);
            }

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void cargarPedidos(
            String estado,
            String tipo
    ) {

        try {

            List<Pedido> lista =
                    pedidoDAO.readFiltered(
                            estado,
                            tipo
                    );

            modeloPed.setRowCount(0);
            cmbPedido.removeAllItems();

            for (Pedido pedido : lista) {

                modeloPed.addRow(
                        new Object[]{
                                pedido.getId(),
                                pedido.getDireccion(),
                                pedido.getTipo(),
                                pedido.getEstado()
                        }
                );

                cmbPedido.addItem(
                        new PedidoItem(
                                pedido.getId(),
                                pedido.getDireccion()
                        )
                );
            }

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void cargarEntregas() {

        try {

            List<Entrega> lista =
                    entregaDAO.readAll();

            modeloEnt.setRowCount(0);

            for (Entrega entrega : lista) {

                modeloEnt.addRow(
                        new Object[]{
                                entrega.getId(),
                                entrega.getIdPedido(),
                                entrega.getIdRepartidor(),
                                entrega.getFecha(),
                                entrega.getHora()
                        }
                );
            }

        } catch (SQLException ex) {
            sqlError(ex);
        }
    }

    private void seleccionarPedido(int id) {

        for (int i = 0;
             i < cmbPedido.getItemCount();
             i++) {

            if (cmbPedido.getItemAt(i).id == id) {

                cmbPedido.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarRepartidor(int id) {

        for (int i = 0;
             i < cmbRepartidor.getItemCount();
             i++) {

            if (cmbRepartidor
                    .getItemAt(i)
                    .getId() == id) {

                cmbRepartidor.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarRep() {

        txtRepId.setText("");
        txtRepNombre.setText("");
        tablaRep.clearSelection();
    }

    private void limpiarPed() {

        txtPedId.setText("");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        tablaPed.clearSelection();
    }

    private void limpiarEnt() {

        txtEntId.setText("");
        txtFecha.setText(
                LocalDate.now().toString()
        );
        txtHora.setText(
                LocalTime.now()
                        .withNano(0)
                        .toString()
        );

        tablaEnt.clearSelection();
    }

    private void ok(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "SpeedFast",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void error(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validacion",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void sqlError(SQLException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error de base de datos: "
                        + ex.getMessage(),
                "Error SQL",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void manejar(Exception ex) {

        if (ex instanceof SQLException) {

            sqlError((SQLException) ex);

        } else {

            error(ex.getMessage());
        }
    }

    private record PedidoItem(
            int id,
            String direccion
    ) {

        @Override
        public String toString() {

            return id + " - " + direccion;
        }
    }
}