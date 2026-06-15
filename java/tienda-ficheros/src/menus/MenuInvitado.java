package menus;

import main.Programa;
import util.Utilidades;

/**
 * Menú de invitado de la aplicación.
 * <p>
 * Es el punto de entrada para usuarios no autenticados. Permite explorar el
 * catálogo, registrarse como cliente o iniciar sesión.
 * Delega toda la lógica en {@link Programa} y usa su {@link java.util.Scanner}
 * único para la lectura de datos.
 * </p>
 *
 * @version 1.0
 */
public class MenuInvitado {

    /**
     * Muestra el menú principal del invitado en un bucle hasta que
     * el usuario elija salir (opción 5), momento en que se persisten los datos.
     */
    public static void mostrarMenu() {

        int opcion = 0;

        while (opcion != 5) {

            System.out.println("""
                    MENÚ DE INVITADO
                    1. Mostrar todos los productos
                    2. Mostrar un producto por código
                    3. Registrarse
                    4. Iniciar sesión
                    5. Salir
                    """);

            try {
                opcion = Utilidades.leerEnteroLimites(1, 5, Programa.sc);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }

            switch (opcion) {
                case 1 -> Programa.mostrarProductos();
                case 2 -> Programa.mostrarProductoPorCodigo();
                case 3 -> Programa.registrarCliente();
                case 4 -> Programa.iniciarSesion();
            }
        }

        System.out.println("Saliendo de la aplicación...");
        Programa.guardarArchivos();
    }
}