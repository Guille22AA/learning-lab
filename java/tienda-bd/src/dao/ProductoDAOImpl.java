package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConexionDB;
import entities.Producto;

/**
 * Implementación del acceso a datos para los productos del catálogo.
 *
 * @version 1.0
 */

public class ProductoDAOImpl implements ProductoDAO{
	
	private static final String SQL_INSERTAR = "INSERT INTO PRDOCUTO DESCRIPCION, STOCK, PRECIO) VALUES (?, ?, ?)";
	private static final String SQL_BUSCAR_ID = "SELECT * FROM PRODUCTO WHERE ID = ?";
	private static final String SQL_LISTAR = "SELECT * FROM PRODUCTO";
	private static final String SQL_ACT_STOCK = "UPDATE PRODUCTO SET STOCK = ? WHERE ID = ?";
	private static final String SQL_ELIMINAR = "DELETE FROM PRODUCTO WHERE ID = ?";


	
	/**
     * Inserta un nuevo producto y devuelve el objeto con el ID asignado por la BBDD.
     *
     * @param p producto a insertar.
     * @return el mismo producto con el ID actualizado, o {@code null} si falla.
     */
	@Override
	public Producto insertar(Producto p) {
		try(Connection con = ConexionDB.getConnection();
				PreparedStatement ps = con.prepareStatement(SQL_INSERTAR, new String[] {"ID"})) {
			
			ps.setString(1, p.getDescripcion());
			ps.setInt(2, p.getStock());
			ps.setDouble(3, p.getPrecio());
			ps.executeUpdate();
			
			con.commit();
			return p;
			
		} catch (SQLException e) {
			System.out.println("Error al insertar producto: " + e.getMessage());
            return null;
		}
	}

	
	/**
     * Busca un producto por su ID.
     *
     * @param id identificador del producto.
     * @return el {@link Producto} encontrado, o {@code null} si no existe.
     */
    @Override
	public Producto buscarPorId(int id) {
		try (Connection con = ConexionDB.getConnection();
				PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ID)) {
			
			ps.setInt(1, id);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Producto(
	                        rs.getInt("id"),
	                        rs.getString("nombre"),
	                        rs.getInt("stock"),
	                        rs.getDouble("precio")  
	                );
				}
			}
			
		} catch (SQLException e) {
			 System.out.println("Error al buscar producto: " + e.getMessage());
		}
		return null;
	}
    

    /**
     * Devuelve todos los productos del catálogo.
     *
     * @return lista de {@link Producto}, vacía si no hay ninguno.
     */
	public List<Producto> listarTodos() {
		List<Producto> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
	}

	
	/**
     * Actualiza el stock de un producto.
     *
     * @param id         identificador del producto.
     * @param nuevoStock nuevo valor de stock.
     * @return {@code true} si se actualizó correctamente, {@code false} si no.
     */
    @Override
    public boolean actualizarStock(int id, int nuevoStock) {
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_ACT_STOCK)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            con.commit();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
    
    
    /**
     * Elimina un producto del catálogo por su identificador.
     * <p>
     * Si el producto tiene líneas de compra activas en el carrito de algún cliente,
     * no se eliminará y el método devolverá {@code false}, preservando la
     * integridad referencial de la base de datos.
     * </p>
     *
     * @param id identificador del producto a eliminar.
     * @return {@code true} si el producto se eliminó correctamente,
     *         {@code false} si no existe o si hay clientes que lo tienen
     *         en su carrito.
     */
    @Override
    public boolean eliminar(int id) {
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            con.commit();
            return filas > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("integrity constraint")) {
                System.out.println("No se puede eliminar: hay clientes con este producto en su carrito.");
            } else {
                System.out.println("Error al eliminar producto: " + e.getMessage());
            }
            return false;
        }
    }
    
    
    /**
     * Convierte una fila del {@link ResultSet} en un objeto {@link Producto}.
     *
     * @param rs resultado de la consulta posicionado en una fila.
     * @return objeto {@link Producto} con los datos de esa fila.
     * @throws SQLException si hay error al leer el ResultSet.
     */
    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt("ID"),
            rs.getString("DESCRIPCION"),
            rs.getInt("STOCK"),
            rs.getDouble("PRECIO")
        );
    }




    

}
