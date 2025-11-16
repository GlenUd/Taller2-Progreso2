public class Venta {


        private Producto producto;
        private String mes;
        private int cantidad;

        public Venta(Producto producto, String mes, int cantidad) {
            this.producto = producto;
            this.mes = mes;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public String getMes() {
            return mes;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getTotal() {
            return cantidad * producto.getPrecio();
        }



    @Override
    public String toString() {
        return mes + " - " + producto.getNombre()
                + " x" + cantidad
                + " = $" + String.format("%.2f", getTotal());
    }
}
