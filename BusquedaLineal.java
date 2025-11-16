
import java.util.List;
public class BusquedaLineal implements IBusquedaProducto {
    @Override
    public Producto buscarPorId(List<Producto> lista, int id) {
        for (Producto p : lista) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Producto buscarPorNombre(List<Producto> lista, String nombre) {
        for (Producto p : lista) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}
