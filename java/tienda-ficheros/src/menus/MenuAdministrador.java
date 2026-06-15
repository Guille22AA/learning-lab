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
 * @version 1.0
 */
public class MenuAdministrador {

    /**
     * Muestra el menú principal del administrador en un bucle hasta que
     * el usuario elija salir (opción 8), momento en que se persisten los datos.
     */
    public static void mostrarMenu() {

        int opcion = 0;

        while (opcion != 8) {

            System.out.println("""
                    MENÚ DE ADMINISTRADOR
                    1. Mostrar todos los productos
                    2. Mostrar producto por código
                    3. Modificar stock
                    4. Mostrar ganancia
                    5. Dar de alta a un usuario
                    6. Añadir producto
                    7. Desconectar
                    8. Salir
                    """);

            try {
                opcion = Utilidades.leerEnteroLimites(1, 8, Programa.sc);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

            switch (opcion) {
                case 1 -> Programa.mostrarProductos();
                case 2 -> Programa.mostrarProductoPorCodigo();
                case 3 -> Programa.modificarStock();
                case 4 -> Programa.mostrarGanancia();
                case 5 -> Programa.crearUsuario();
                case 6 -> Programa.anyadirProducto();
                case 7 -> Programa.desconectar();
            }
        }

        System.out.println("Saliendo de la aplicación...");
        Programa.guardarArchivos();
    }
}