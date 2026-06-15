package entities;

/**
 * Representa a un usuario con rol de administrador.
 * <p>
 * Extiende {@link Usuario} sin añadir atributos propios; el rol se infiere
 * por el tipo en tiempo de ejecución. El administrador tiene acceso completo
 * a la gestión de productos, usuarios y estadísticas de ganancia a través
 * de {@link MenuAdministrador}.
 * </p>
 *
 * @version 2.0
 * @see Usuario
 */
public class Administrador extends Usuario {

    /**
     * Construye un administrador nuevo antes de insertarlo en la BBDD.
     *
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del administrador.
     */
    public Administrador(String username, String contrasena) {
        super(username, contrasena);
    }

    /**
     * Construye un administrador recuperado de la BBDD, incluyendo su {@code id}.
     *
     * @param id         identificador asignado por la base de datos.
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del administrador.
     */
    public Administrador(int id, String username, String contrasena) {
        super(id, username, contrasena);
    }
}