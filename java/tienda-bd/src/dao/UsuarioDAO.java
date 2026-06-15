package dao;

import java.util.List;
import entities.Usuario;

/**
 * Interfaz de acceso a datos para los usuarios del sistema.
 *
 * @version 1.0
 */
public interface UsuarioDAO {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * El tipo se infiere según si el objeto es {@link entities.Administrador}
     * o {@link entities.Cliente}.
     *
     * @param u usuario a insertar.
     * @return el mismo usuario con el {@code id} asignado por la BBDD,
     *         o {@code null} si falla.
     */
    Usuario insertar(Usuario u);

    /**
     * Busca un usuario por su nombre de usuario.
     * Usado principalmente en el flujo de inicio de sesión.
     *
     * @param username nombre de usuario a buscar.
     * @return el {@link Usuario} encontrado, o {@code null} si no existe.
     */
    Usuario buscarPorUsername(String username);

    /**
     * Devuelve todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link Usuario}, vacía si no hay ninguno.
     */
    List<Usuario> listarTodos();
}