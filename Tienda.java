import java.util.ArrayList;
import java.util.List;

public class Tienda {

    private List<Producto> productos;
    private List<Venta> ventas;

    public Tienda() {
        productos = new ArrayList<>();
        ventas = new ArrayList<>();
    }


    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public List<Producto> getProductos() {
        return productos;
    }


    public int generarNuevoId() {
        int max = 1000;
        for (Producto p : productos) {
            if (p.getId() > max) {
                max = p.getId();
            }
        }
        return max + 1;
    }

    public void actualizarPrecio(int idProducto, double nuevoPrecio) {
        for (Producto p : productos) {
            if (p.getId() == idProducto) {
                p.setPrecio(nuevoPrecio);
                break;
            }
        }
    }


    public Venta registrarVenta(Producto producto, String mes, int cantidad) {
        Venta v = new Venta(producto, mes, cantidad);
        ventas.add(v);
        return v;
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}
