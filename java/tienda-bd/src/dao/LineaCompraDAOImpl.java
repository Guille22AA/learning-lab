package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import connection.ConexionDB;
import entities.LineaCompra;

/**
 * Implementación del acceso a datos para las líneas de compra del carrito.
 * <p>
 * Cada {@link LineaCompra} representa una fila en la tabla {@code LINEA_COMPRA},
 * que relaciona un usuario con un producto y una cantidad.
 * </p>
 *
 * @version 1.0
 */
public class LineaCompraDAOImpl implements LineaCompraDAO {

    private static final String SQL_INSERTAR =
        "INSERT INTO LINEA_COMPRA (ID_USUARIO, ID_PRODUCTO, CANTIDAD) VALUES (?, ?, ?)";
    private static final String SQL_OBTENER  =
        "SELECT ID_USUARIO, ID_PRODUCTO, CANTIDAD FROM LINEA_COMPRA WHERE ID_USUARIO = ?";
    private static final String SQL_VACIAR   =
        "DELETE FROM LINEA_COMPRA WHERE ID_USUARIO = ?";

    /**
     * Inserta una nueva línea de compra en la BBDD.
     *
     * @param lc línea de compra a insertar.
     */
    @Override
    public void insertar(LineaCompra lc) {
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {

            ps.setInt(1, lc.getIdUsuario());
            ps.setInt(2, lc.getIdProducto());
            ps.setInt(3, lc.getCantidad());
            ps.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            System.out.println("Error al insertar línea de compra: " + e.getMessage());
        }
    }

    /**
     * Devuelve todas las líneas de compra de un usuario, es decir, su carrito completo.
     *
     * @param idUsuario identificador del usuario.
     * @return lista de {@link LineaCompra}, vacía si el carrito está vacío.
     */
    @Override
    public List<LineaCompra> obtenerPorUsuario(int idUsuario) {
        List<LineaCompra> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener carrito: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina todas las líneas de compra de un usuario, vaciando su carrito.
     * Se llama al finalizar una compra.
     *
     * @param idUsuario identificador del usuario cuyo carrito se vacía.
     */
    @Override
    public void vaciar(int idUsuario) {
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_VACIAR)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            System.out.println("Error al vaciar carrito: " + e.getMessage());
        }
    }

    /**
     * Convierte una fila del {@link ResultSet} en un objeto {@link LineaCompra}.
     *
     * @param rs resultado de la consulta posicionado en una fila.
     * @return objeto {@link LineaCompra} con los datos de esa fila.
     * @throws SQLException si hay error al leer el ResultSet.
     */
    private LineaCompra mapear(ResultSet rs) throws SQLException {
        return new LineaCompra(
            rs.getInt("ID_USUARIO"),
            rs.getInt("ID_PRODUCTO"),
            rs.getInt("CANTIDAD")
        );
    }
}