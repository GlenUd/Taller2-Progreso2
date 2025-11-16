
import java.util.List;

public interface IBusquedaProducto {
    Producto buscarPorId(List<Producto> lista, int id);
    Producto buscarPorNombre(List<Producto> lista, String nombre);
}
