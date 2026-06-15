package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión con la base de datos Oracle XE.
 * <p>
 * Proporciona un método estático para obtener una nueva conexión JDBC al
 * contenedor {@code XEPDB1}. Cada llamada a {@link #getConnection()} abre
 * una conexión nueva, por lo que debe usarse siempre dentro de un
 * try-with-resources para garantizar su cierre automático.
 * </p>
 *
 * @version 1.0
 */
public class ConexionDB {

    private static final String URL  = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String USER = "TIENDAJAVA";
    private static final String PASS = "oracle123";

    /** Constructor privado: esta clase no debe instanciarse. */
    private ConexionDB() {}

    /**
     * Abre y devuelve una nueva conexión JDBC a la base de datos.
     *
     * @return conexión activa lista para usar.
     * @throws SQLException si no se puede establecer la conexión.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}