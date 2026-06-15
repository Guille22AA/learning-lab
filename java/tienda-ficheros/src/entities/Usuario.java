package entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase base abstracta que representa a un usuario del sistema.
 * <p>
 * Define las credenciales comunes (nombre de usuario y contraseña) y la
 * igualdad entre usuarios basada únicamente en el nombre de usuario,
 * que actúa como identificador único.
 * </p>
 *
 * @version 1.0
 * @see Administrador
 * @see Cliente
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String contrasena;

    /**
     * Construye un usuario con las credenciales indicadas.
     *
     * @param username   nombre de usuario único.
     * @param contrasena contraseña del usuario.
     */
    public Usuario(String username, String contrasena) {
        this.username  = username;
        this.contrasena = contrasena;
    }

    // -------------------------------------------------------------------------
    // Getters y setters
    // -------------------------------------------------------------------------

    /**
     * Devuelve el nombre de usuario.
     *
     * @return nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return contraseña.
     */
    public String getContrasena() {
        return contrasena;
    }

    // -------------------------------------------------------------------------
    // Métodos de Object
    // -------------------------------------------------------------------------

    /**
     * Dos usuarios son iguales si tienen el mismo nombre de usuario,
     * independientemente de su tipo o contraseña.
     *
     * @param obj objeto con el que comparar.
     * @return {@code true} si ambos usuarios tienen el mismo {@code username}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario other = (Usuario) obj;
        return Objects.equals(username, other.username);
    }

    /**
     * Código hash basado en el nombre de usuario.
     *
     * @return hash del {@code username}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Representación textual del usuario. No expone la contraseña.
     *
     * @return cadena con el nombre de usuario.
     */
    @Override
    public String toString() {
        return "Usuario [username=" + username + "]";
    }
}