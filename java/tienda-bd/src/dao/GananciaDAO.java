package dao;

/**
 * Interfaz de acceso a datos para la ganancia total acumulada.
 * <p>
 * Opera sobre una tabla {@code GANANCIA} de una sola fila que se actualiza
 * con cada compra finalizada.
 * </p>
 *
 * @version 1.0
 */
public interface GananciaDAO {

    /**
     * Devuelve la ganancia total acumulada almacenada en la base de datos.
     *
     * @return ganancia total en euros.
     */
    double obtener();

    /**
     * Actualiza la ganancia total en la base de datos.
     *
     * @param nuevaGanancia nuevo valor de la ganancia en euros.
     */
    void actualizar(double nuevaGanancia);
}