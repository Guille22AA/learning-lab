package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import connection.ConexionDB;
import entities.Administrador;
import entities.Cliente;
import entities.Usuario;

/**
 * Implementación del acceso a datos para los usuarios del sistema.
 * <p>
 * El método {@link #mapear(ResultSet)} reconstruye un {@link Administrador}
 * o un {@link Cliente} según el campo {@code TIPO} almacenado en la BBDD.
 * </p>
 *
 * @version 1.0
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    private static final String SQL_INSERTAR  =
        "INSERT INTO USUARIO (USERNAME, CONTRASENA, TIPO) VALUES (?, ?, ?)";
    private static final String SQL_BUSCAR    =
        "SELECT ID, USERNAME, CONTRASENA, TIPO FROM USUARIO WHERE USERNAME = ?";
    private static final String SQL_LISTAR    =
        "SELECT ID, USERNAME, CONTRASENA, TIPO FROM USUARIO";

    /**
     * Inserta un nuevo usuario en la BBDD y devuelve el objeto con el ID asignado.
     * El tipo se infiere automáticamente según si el objeto es {@link Administrador}
     * o {@link Cliente}.
     *
     * @param u usuario a insertar.
     * @return el mismo usuario con el {@code id} actualizado, o {@code null} si falla.
     */
    @Override
    public Usuario insertar(Usuario u) {
        String tipo = (u instanceof Administrador) ? "administrador" : "cliente";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 SQL_INSERTAR, new String[]{"ID"})) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getContrasena());
            ps.setString(3, tipo);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
            con.commit();
            return u;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * Usado principalmente en el flujo de inicio de sesión.
     *
     * @param username nombre de usuario a buscar.
     * @return el {@link Usuario} encontrado ({@link Administrador} o {@link Cliente}),
     *         o {@code null} si no existe.
     */
    @Override
    public Usuario buscarPorUsername(String username) {
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link Usuario}, vacía si no hay ninguno.
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Convierte una fila del {@link ResultSet} en un {@link Administrador}
     * o un {@link Cliente} según el campo {@code TIPO}.
     *
     * @param rs resultado de la consulta posicionado en una fila.
     * @return objeto {@link Usuario} concreto.
     * @throws SQLException si hay error al leer el ResultSet.
     */
    private Usuario mapear(ResultSet rs) throws SQLException {
        int    id         = rs.getInt("ID");
        String username   = rs.getString("USERNAME");
        String contrasena = rs.getString("CONTRASENA");
        String tipo       = rs.getString("TIPO");

        if (tipo.equals("administrador")) {
            return new Administrador(id, username, contrasena);
        } else {
            return new Cliente(id, username, contrasena);
        }
    }


}
