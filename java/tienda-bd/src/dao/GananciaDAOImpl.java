package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.ConexionDB;

/**
 * Implementación del acceso a datos para la ganancia total acumulada.
 * <p>
 * Opera sobre una tabla {@code GANANCIA} de una sola fila que se actualiza
 * con cada compra finalizada. Nunca inserta filas nuevas.
 * </p>
 *
 * @version 1.0
 */
public class GananciaDAOImpl implements GananciaDAO{
	
	private static final String SQL_OBTENER = "SELECT TOTAL FROM GANANCIA";
	private static final String SQL_ACTUALIZAR = "UPDATE GANANCIA TOTAL = ?, ULTIMA_MODIFICACION = SYSDATE";
	
	
	/**
     * Devuelve la ganancia total acumulada almacenada en la base de datos.
     *
     * @return ganancia total en euros, o {@code 0.0} si no hay datos.
     */
    @Override
	public double obtener() {
		try (Connection con = ConexionDB.getConnection();
				PreparedStatement ps = con.prepareStatement(SQL_OBTENER);
				ResultSet rs = ps.executeQuery()) {
			
			if (rs.next()) {
				return rs.getDouble("TOTAL");
			}
			
		} catch (SQLException e) {
			System.out.println("Error al obtener la ganancia: " + e.getMessage());
		}
		return 0.0;
	}
    
    
    /**
     * Actualiza la ganancia total en la base de datos y registra la fecha
     * de modificación.
     *
     * @param nuevaGanancia nuevo valor de la ganancia en euros.
     */
	@Override
	public void actualizar(double nuevaGanancia) {
		try(Connection con = ConexionDB.getConnection();
				PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
			
			ps.setDouble(1, nuevaGanancia);
			ps.executeUpdate();
			con.commit();
			
		} catch (SQLException e) {
			 System.out.println("Error al actualizar la ganancia: " + e.getMessage());
		}
		
	}
	

}
