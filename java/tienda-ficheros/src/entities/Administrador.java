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
 * @version 1.0
 * @see Usuario
 * @see MenuAdministrador
 */
public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;

    /**
     * Construye un administrador con las credenciales indicadas.
     *
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del administrador.
     */
    public Administrador(String username, String contrasena) {
        super(username, contrasena);
    }
}