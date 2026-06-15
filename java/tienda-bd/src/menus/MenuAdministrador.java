package menus;

import main.Programa;
import util.Utilidades;

/**
 * Menú de administrador de la aplicación.
 * <p>
 * Muestra las opciones disponibles para un usuario con rol de administrador
 * y delega la lógica de cada acción en {@link Programa}.
 * Usa el {@link java.util.Scanner} único gestionado por {@link Programa}.
 * </p>
 *
 * @version 2.0
 */
public class MenuAdministrador {
 
    /**
     * Muestra el menú principal del administrador en un bucle hasta que
     * el usuario elija salir (opción 9).
     */
    public static void mostrarMenu() {
        int opcion = 0;
        while (opcion != 9) {
            System.out.println("""
                    MENÚ DE ADMINISTRADOR
                    1. Mostrar todos los productos
                    2. Mostrar producto por ID
                    3. Modificar stock
                    4. Mostrar ganancia
                    5. Dar de alta a un usuario
                    6. Añadir producto
                    7. Eliminar producto
                    8. Desconectar
                    9. Salir
                    """);
            try {
                opcion = Utilidades.leerEnteroLimites(1, 9, Programa.sc);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            switch (opcion) {
                case 1 -> Programa.mostrarProductos();
                case 2 -> Programa.mostrarProductoPorId();
                case 3 -> Programa.modificarStock();
                case 4 -> Programa.mostrarGanancia();
                case 5 -> Programa.crearUsuario();
                case 6 -> Programa.anyadirProducto();
                case 7 -> Programa.eliminarProducto();
                case 8 -> Programa.desconectar();
            }
        }
        System.out.println("Saliendo de la aplicación...");
    }
}