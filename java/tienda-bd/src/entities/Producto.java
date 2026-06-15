package entities;

import java.util.Objects;

/**
 * Representa un producto del catálogo de la tienda.
 * <p>
 * El identificador {@code id} es generado automáticamente por la base de datos
 * al insertar el producto. No debe asignarse manualmente salvo al recuperar
 * un producto existente desde la BBDD.
 * </p>
 *
 * @version 2.0
 */
public class Producto {

    private int    id;
    private String descripcion;
    private int    stock;
    private double precio;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    /**
     * Constructor para crear un producto nuevo antes de insertarlo en la BBDD.
     * El {@code id} se asignará automáticamente tras el INSERT.
     *
     * @param descripcion descripción del producto.
     * @param stock       cantidad disponible en stock (debe ser >= 0).
     * @param precio      precio unitario (debe ser >= 0).
     */
    public Producto(String descripcion, int stock, double precio) {
        this.descripcion = descripcion;
        this.stock       = stock;
        this.precio      = precio;
    }

    /**
     * Constructor para reconstruir un producto recuperado de la BBDD,
     * incluyendo su {@code id}.
     *
     * @param id          identificador asignado por la base de datos.
     * @param descripcion descripción del producto.
     * @param stock       cantidad disponible en stock.
     * @param precio      precio unitario.
     */
    public Producto(int id, String descripcion, int stock, double precio) {
        this.id          = id;
        this.descripcion = descripcion;
        this.stock       = stock;
        this.precio      = precio;
    }

    // -------------------------------------------------------------------------
    // Getters y setters
    // -------------------------------------------------------------------------

    /**
     * Devuelve el identificador del producto asignado por la BBDD.
     *
     * @return id del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el identificador generado por la BBDD tras un INSERT.
     * No debe usarse en ningún otro contexto.
     *
     * @param id identificador generado por Oracle.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve la descripción del producto.
     *
     * @return descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve el stock disponible del producto.
     *
     * @return stock actual.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Actualiza el stock del producto. No acepta valores negativos.
     *
     * @param stock nuevo valor de stock.
     * @throws IllegalArgumentException si el stock es negativo.
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    /**
     * Devuelve el precio unitario del producto.
     *
     * @return precio en euros.
     */
    public double getPrecio() {
        return precio;
    }

    // -------------------------------------------------------------------------
    // Métodos de Object
    // -------------------------------------------------------------------------

    /**
     * Dos productos son iguales si tienen el mismo {@code id}.
     *
     * @param obj objeto con el que comparar.
     * @return {@code true} si ambos tienen el mismo id.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto other = (Producto) obj;
        return id == other.id;
    }

    /**
     * Código hash basado en el {@code id} del producto.
     *
     * @return hash del id.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Representación textual del producto.
     *
     * @return cadena con id, descripción, stock y precio.
     */
    @Override
    public String toString() {
        return "Producto [id=" + id + ", descripcion=" + descripcion +
               ", stock=" + stock + ", precio=" + precio + " €]";
    }
}