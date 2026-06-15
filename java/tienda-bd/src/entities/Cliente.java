package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un usuario con rol de cliente.
 * <p>
 * Además de las credenciales heredadas de {@link Usuario}, el cliente
 * dispone de un carrito de compra formado por una lista de {@link LineaCompra}.
 * El carrito se carga desde la base de datos al iniciar sesión y se persiste
 * en la tabla {@code LINEA_COMPRA}.
 * La lógica de compra se gestiona desde {@link Programa} a través de
 * {@link LineaCompraDAO}.
 * </p>
 *
 * @version 2.0
 * @see Usuario
 * @see LineaCompra
 */
public class Cliente extends Usuario {

    /**
     * Carrito de compra: lista de líneas de compra del cliente.
     * Se inicializa vacío y se rellena desde la BBDD al iniciar sesión.
     */
    private List<LineaCompra> carrito;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    /**
     * Construye un cliente nuevo antes de insertarlo en la BBDD.
     *
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del cliente.
     */
    public Cliente(String username, String contrasena) {
        super(username, contrasena);
        this.carrito = new ArrayList<>();
    }

    /**
     * Construye un cliente recuperado de la BBDD, incluyendo su {@code id}.
     *
     * @param id         identificador asignado por la base de datos.
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del cliente.
     */
    public Cliente(int id, String username, String contrasena) {
        super(id, username, contrasena);
        this.carrito = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Devuelve el carrito de compra del cliente.
     *
     * @return lista de {@link LineaCompra}.
     */
    public List<LineaCompra> getCarrito() {
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
        return "Cliente [id=" + getId() + ", username=" + getUsername() +
               ", carrito=" + carrito + "]";
    }
}