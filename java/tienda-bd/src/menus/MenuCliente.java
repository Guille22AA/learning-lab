package menus;

import main.Programa;
import util.Utilidades;

/**
 * Menú de cliente de la aplicación.
 * <p>
 * Muestra las opciones disponibles para un usuario con rol de cliente
 * y delega la lógica de cada acción en {@link Programa}.
 * Usa el {@link java.util.Scanner} único gestionado por {@link Programa}.
 * </p>
 *
 * @version 2.0
 */
public class MenuCliente {
 
    /**
     * Muestra el menú principal del cliente en un bucle hasta que
     * el usuario elija salir (opción 6).
     */
    public static void mostrarMenu() {
        int opcion = 0;
        while (opcion != 6) {
            System.out.println("""
                    MENÚ DE CLIENTE
                    1. Mostrar todos los productos
                    2. Mostrar un producto por ID
                    3. Añadir a la cesta
                    4. Ver carrito
                    5. Desconectar
                    6. Salir
                    """);
            try {
                opcion = Utilidades.leerEnteroLimites(1, 6, Programa.sc);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
            switch (opcion) {
                case 1 -> Programa.mostrarProductos();
                case 2 -> Programa.mostrarProductoPorId();
                case 3 -> Programa.anadirACesta();
                case 4 -> Programa.verCarrito();
                case 5 -> Programa.desconectar();
            }
        }
        System.out.println("Saliendo de la aplicación...");
    }
}