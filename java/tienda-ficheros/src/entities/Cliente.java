package entities;

import java.util.HashMap;
import java.util.Map;



/**
 * Representa a un usuario con rol de cliente.
 * <p>
 * Además de las credenciales heredadas de {@link Usuario}, el cliente
 * dispone de un carrito de compra que asocia cada {@link Producto} con la
 * cantidad deseada. El carrito se persiste junto con el objeto cliente, por lo
 * que sobrevive entre sesiones. La lógica de compra se gestiona desde
 * {@link Programa} y se muestra a través de {@link MenuCliente}.
 * </p>
 *
 * @version 1.0
 * @see Usuario
 * @see MenuCliente
 */
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    /**
     * Carrito de compra: asocia cada producto con su cantidad.
     * Se inicializa vacío al crear el cliente y se mantiene entre sesiones
     * gracias a la serialización.
     */
    private Map<Producto, Integer> carrito;

    /**
     * Construye un cliente con las credenciales indicadas y un carrito vacío.
     *
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del cliente.
     */
    public Cliente(String username, String contrasena) {
        super(username, contrasena);
        this.carrito = new HashMap<>();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Devuelve el carrito de compra del cliente.
     *
     * @return mapa de {@link Producto} a cantidad.
     */
    public Map<Producto, Integer> getCarrito() {
        return carrito;
    }

    // -------------------------------------------------------------------------
    // Métodos de Object
    // -------------------------------------------------------------------------

    /**
     * Representación textual del cliente, incluyendo su nombre de usuario
     * y el contenido actual del carrito.
     *
     * @return cadena descriptiva del cliente.
     */
    @Override
    public String toString() {
        return "Cliente [username=" + getUsername() + ", carrito=" + carrito + "]";
    }
}