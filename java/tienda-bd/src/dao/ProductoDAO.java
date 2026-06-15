package dao;

import java.util.List;
import entities.Producto;

/**
 * Interfaz de acceso a datos para los productos del catálogo.
 *
 * @version 1.0
 */
public interface ProductoDAO {

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param p producto a insertar.
     * @return el mismo producto con el {@code id} asignado por la BBDD,
     *         o {@code null} si falla.
     */
    Producto insertar(Producto p);

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto.
     * @return el {@link Producto} encontrado, o {@code null} si no existe.
     */
    Producto buscarPorId(int id);

    /**
     * Devuelve todos los productos del catálogo.
     *
     * @return lista de {@link Producto}, vacía si no hay ninguno.
     */
    List<Producto> listarTodos();

    /**
     * Actualiza el stock de un producto.
     *
     * @param id         identificador del producto.
     * @param nuevoStock nuevo valor de stock.
     * @return {@code true} si se actualizó correctamente, {@code false} si no.
     */
    boolean actualizarStock(int id, int nuevoStock);

    /**
     * Elimina un producto del catálogo por su identificador.
     * <p>
     * Si el producto tiene líneas de compra activas en el carrito de algún
     * cliente, no se eliminará y el método devolverá {@code false}.
     * </p>
     *
     * @param id identificador del producto a eliminar.
     * @return {@code true} si se eliminó correctamente, {@code false} si no
     *         existe o si hay clientes que lo tienen en su carrito.
     */
    boolean eliminar(int id);
}