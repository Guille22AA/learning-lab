package entities;

/**
 * Representa una línea de compra dentro del carrito de un cliente.
 * <p>
 * Cada instancia corresponde a una fila de la tabla {@code LINEA_COMPRA}
 * en la base de datos, asociando un usuario con un producto y la cantidad
 * deseada. La clave primaria en BBDD es la combinación de
 * {@code idUsuario} e {@code idProducto}.
 * </p>
 *
 * @version 1.0
 * @see entities.Cliente
 */
public class LineaCompra {

    private int idUsuario;
    private int idProducto;
    private int cantidad;

    /**
     * Construye una línea de compra con el usuario, producto y cantidad indicados.
     *
     * @param idUsuario  identificador del cliente propietario del carrito.
     * @param idProducto identificador del producto añadido.
     * @param cantidad   cantidad deseada del producto (debe ser mayor que 0).
     */
    public LineaCompra(int idUsuario, int idProducto, int cantidad) {
        this.idUsuario  = idUsuario;
        this.idProducto = idProducto;
        this.cantidad   = cantidad;
    }

    // -------------------------------------------------------------------------
    // Getters y setters
    // -------------------------------------------------------------------------

    /**
     * Devuelve el identificador del usuario propietario del carrito.
     *
     * @return id del usuario.
     */
    public int getIdUsuario() { return idUsuario; }

    /**
     * Devuelve el identificador del producto en esta línea de compra.
     *
     * @return id del producto.
     */
    public int getIdProducto() { return idProducto; }

    /**
     * Devuelve la cantidad del producto en esta línea de compra.
     *
     * @return cantidad.
     */
    public int getCantidad() { return cantidad; }

    /**
     * Actualiza la cantidad del producto en esta línea de compra.
     *
     * @param cantidad nueva cantidad (debe ser mayor que 0).
     */
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    // -------------------------------------------------------------------------
    // Métodos de Object
    // -------------------------------------------------------------------------

    /**
     * Representación textual de la línea de compra.
     *
     * @return cadena con el id de usuario, id de producto y cantidad.
     */
    @Override
    public String toString() {
        return "LineaCompra [idUsuario=" + idUsuario +
               ", idProducto=" + idProducto +
               ", cantidad=" + cantidad + "]";
    }
}