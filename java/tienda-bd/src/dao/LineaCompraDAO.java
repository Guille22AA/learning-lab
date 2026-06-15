package dao;

import java.util.List;
import entities.LineaCompra;

/**
 * Interfaz de acceso a datos para las líneas de compra del carrito.
 * <p>
 * Cada {@link LineaCompra} representa una fila en la tabla {@code LINEA_COMPRA},
 * que relaciona un usuario con un producto y una cantidad.
 * </p>
 *
 * @version 1.0
 */
public interface LineaCompraDAO {

    /**
     * Inserta una nueva línea de compra en la base de datos.
     *
     * @param lc línea de compra a insertar.
     */
    void insertar(LineaCompra lc);

    /**
     * Devuelve todas las líneas de compra de un usuario, es decir, su carrito completo.
     *
     * @param idUsuario identificador del usuario.
     * @return lista de {@link LineaCompra}, vacía si el carrito está vacío.
     */
    List<LineaCompra> obtenerPorUsuario(int idUsuario);

    /**
     * Elimina todas las líneas de compra de un usuario, vaciando su carrito.
     * Se llama al finalizar una compra.
     *
     * @param idUsuario identificador del usuario cuyo carrito se vacía.
     */
    void vaciar(int idUsuario);
}