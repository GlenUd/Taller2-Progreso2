import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ventana extends JFrame {

    private JPanel Principal;
    private JTabbedPane tdpTienda;
    private JTabbedPane tdpAgregar;
    private JPanel inventario;
    private JLabel lblProducto;
    private JLabel lblCantidad;
    private JLabel lblMes;
    private JComboBox<String> cboProductos;
    private JSpinner spiCantidad;
    private JComboBox<String> cboMes;
    private JButton btnAgregar;
    private JButton btnLimpiar;
    private JPanel PRODUCTOS;
    private JList<String> list1;


    private JPanel BUSCADOR;
    private JList<String> list2;
    private JButton btnBuscar;
    private JComboBox<String> cboBusqueda;
    private JComboBox<String> cboBusquedaMes;
    private JLabel lblSelectMes;


    private Tienda tienda;
    private IBusquedaProducto buscador;
    private DefaultListModel<String> modeloListaVentas;
    private DefaultListModel<String> modeloListaBusqueda;



    private static final String[] MESES = {
            "1.Enero", "2.Febrero", "3.Marzo", "4.Abril",
            "5.Mayo", "6.Junio", "7.Julio", "8.Agosto",
            "9.Septiembre", "10.Octubre", "11.Noviembre", "12.Diciembre"
    };


    private void limpiarInventario() {
        tienda.getVentas().clear();
        modeloListaVentas.clear();
    }

    public Ventana() {


        setContentPane(Principal);

        setTitle("Tienda en línea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);


        tienda = new Tienda();
        buscador = new BusquedaLineal();


        modeloListaVentas = new DefaultListModel<>();
        list1.setModel(modeloListaVentas);

        modeloListaBusqueda = new DefaultListModel<>();
        list2.setModel(modeloListaBusqueda);


        tienda.agregarProducto(new Producto(1001, "Teclado", 50.0));
        tienda.agregarProducto(new Producto(1002, "Mouse", 15.0));
        tienda.agregarProducto(new Producto(1003, "Audífonos", 35.0));

        cargarProductosEnCombo();


        btnAgregar.addActionListener(e -> agregarVenta());
        btnBuscar.addActionListener(e -> realizarBusqueda());

        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            limpiarInventario();
        });
    }


    private void agregarVenta() {
        String nombre = (String) cboProductos.getSelectedItem();
        String mes = (String) cboMes.getSelectedItem();
        int cantidad = (Integer) spiCantidad.getValue();

        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
            return;
        }

        Producto producto = buscador.buscarPorNombre(tienda.getProductos(), nombre);

        if (producto == null) {
            JOptionPane.showMessageDialog(this, "El producto no existe en la tienda.");
            return;
        }


        tienda.registrarVenta(producto, mes, cantidad);
        actualizarListaVentas();
        limpiarCampos();
    }


    private void actualizarListaVentas() {
        modeloListaVentas.clear();

        for (Venta v : tienda.getVentas()) {
            Producto p = v.getProducto();
            int cantidad = v.getCantidad();
            double precioUnitario = p.getPrecio();
            double total = precioUnitario * cantidad;

            String linea = "ID: " + p.getId()
                    + " | Producto: " + p.getNombre()
                    + " | Mes: " + v.getMes()
                    + " | Cantidad: " + cantidad
                    + " | Precio unitario: $" + precioUnitario
                    + " | Total: $" + total;

            modeloListaVentas.addElement(linea);
        }
    }


    private void cargarProductosEnCombo() {
        cboProductos.removeAllItems();
        for (Producto p : tienda.getProductos()) {
            cboProductos.addItem(p.getNombre());
        }
    }

    private void limpiarCampos() {
        spiCantidad.setValue(0);

        if (cboMes.getItemCount() > 0) {
            cboMes.setSelectedIndex(0);
        }

        if (cboProductos.getItemCount() > 0) {
            cboProductos.setSelectedIndex(0);
        }
    }


    private void realizarBusqueda() {
        modeloListaBusqueda.clear();

        String metodo = (String) cboBusqueda.getSelectedItem();
        String mesSeleccionado = (String) cboBusquedaMes.getSelectedItem();

        if (metodo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un método de búsqueda.");
            return;
        }
        if (mesSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un mes.");
            return;
        }


        Set<String> mesesValidos = obtenerMesesRango(mesSeleccionado);


        List<Venta> resultados = new ArrayList<>();

        if (metodo.startsWith("1")) { // 1.ID
            String valor = JOptionPane.showInputDialog(this, "Ingresa el ID del producto:");
            if (valor == null || valor.trim().isEmpty()) return;

            try {
                int idBuscado = Integer.parseInt(valor.trim());

                for (Venta v : tienda.getVentas()) {
                    Producto p = v.getProducto();


                    if (p.getId() == idBuscado && mesesValidos.contains(v.getMes())) {
                        resultados.add(v);
                    }
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico.");
                return;
            }

        } else {
            String nombreBuscado = JOptionPane.showInputDialog(this, "Ingresa el nombre del producto:");
            if (nombreBuscado == null || nombreBuscado.trim().isEmpty()) return;

            nombreBuscado = nombreBuscado.trim();

            for (Venta v : tienda.getVentas()) {
                Producto p = v.getProducto();


                if (p.getNombre().equalsIgnoreCase(nombreBuscado)
                        && mesesValidos.contains(v.getMes())) {
                    resultados.add(v);
                }
            }
        }

        if (resultados.isEmpty()) {
            modeloListaBusqueda.addElement(
                    "No hay producto en ese rango de meses a partir de " + mesSeleccionado + "."
            );
            return;
        }

        Collections.sort(resultados, (v1, v2) -> {
            int m1 = indiceMes(v1.getMes());
            int m2 = indiceMes(v2.getMes());
            return Integer.compare(m1, m2);
        });

        for (Venta v : resultados) {
            Producto p = v.getProducto();
            int cantidad = v.getCantidad();
            double precio = p.getPrecio();
            double total = cantidad * precio;

            String linea = "ID: " + p.getId()
                    + " | Producto: " + p.getNombre()
                    + " | Mes: " + v.getMes()
                    + " | Cantidad: " + cantidad
                    + " | Precio: $" + precio
                    + " | Total: $" + total;

            modeloListaBusqueda.addElement(linea);
        }
    }

    private Set<String> obtenerMesesRango(String mesSeleccionado) {
        Set<String> conjunto = new HashSet<>();

        int indiceSeleccionado = indiceMes(mesSeleccionado);
        if (indiceSeleccionado == -1) {
            return conjunto;
        }

        for (int i = 0; i < 3; i++) {
            int idx = (indiceSeleccionado - i + 12) % 12;
            conjunto.add(MESES[idx]);
        }

        return conjunto;
    }



    private int indiceMes(String mes) {
        for (int i = 0; i < MESES.length; i++) {
            if (MESES[i].equals(mes)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ventana().setVisible(true));
    }
}
