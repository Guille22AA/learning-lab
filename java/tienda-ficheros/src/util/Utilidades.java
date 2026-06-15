package util;

import java.util.Scanner;

/**
 * Clase de utilidades con métodos estáticos de validación y lectura de datos.
 * <p>
 * Agrupa la lógica de entrada por teclado y validación de cadenas para
 * mantener los menús y la clase principal libres de código repetitivo.
 * No está pensada para instanciarse.
 * </p>
 *
 * @version 1.0
 */
public class Utilidades {

    /** Constructor privado: esta clase no debe instanciarse. */
    private Utilidades() {}

    // -------------------------------------------------------------------------
    // Validación de cadenas
    // -------------------------------------------------------------------------

    /**
     * Valida que una cadena sea una credencial correcta (entre 3 y 20 caracteres).
     *
     * @param s cadena a validar.
     * @return la misma cadena si es válida.
     * @throws IllegalArgumentException si la cadena es {@code null}, está vacía
     *                                  o no cumple el rango de longitud.
     */
    public static String validarCredencial(String s) {
        if (s == null || s.length() < 3 || s.length() > 20) {
            throw new IllegalArgumentException(
                "El nombre de usuario y la contraseña deben tener entre 3 y 20 caracteres.");
        }
        return s;
    }

    /**
     * Valida que una cadena corresponda a un tipo de usuario reconocido
     * ({@code "administrador"} o {@code "cliente"}, sin distinguir mayúsculas).
     *
     * @param s cadena introducida por el usuario.
     * @return el tipo en minúsculas ({@code "administrador"} o {@code "cliente"}).
     * @throws IllegalArgumentException si la cadena no coincide con ningún tipo válido.
     */
    public static String validarTipoUsuario(String s) {
        if (s.equalsIgnoreCase("administrador") || s.equalsIgnoreCase("cliente")) {
            return s.toLowerCase();
        }
        throw new IllegalArgumentException("Por favor, elija entre 'administrador' o 'cliente'.");
    }

    // -------------------------------------------------------------------------
    // Lectura de datos por teclado
    // -------------------------------------------------------------------------

    /**
     * Lee un número entero desde el {@link Scanner} indicado.
     * Repite la solicitud hasta que el usuario introduzca un valor válido.
     *
     * @param sc scanner de entrada.
     * @return entero leído.
     */
    public static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor incorrecto. Introduce un número entero:");
            }
        }
    }

    /**
     * Lee un número entero dentro de un rango cerrado [{@code inferior}, {@code superior}].
     * Repite la solicitud mientras el valor esté fuera del rango.
     *
     * @param inferior límite inferior (inclusive).
     * @param superior límite superior (inclusive).
     * @param sc       scanner de entrada.
     * @return entero leído dentro del rango.
     */
    public static int leerEnteroLimites(int inferior, int superior, Scanner sc) {
        int numero;
        do {
            numero = leerEntero(sc);
            if (numero < inferior || numero > superior) {
                System.out.println("El número debe estar entre " + inferior + " y " + superior + ".");
            }
        } while (numero < inferior || numero > superior);
        return numero;
    }

    /**
     * Lee un número decimal positivo desde el {@link Scanner} indicado.
     * Repite la solicitud hasta que el usuario introduzca un valor válido y no negativo.
     *
     * @param sc scanner de entrada.
     * @return double leído, mayor o igual a cero.
     */
    public static double leerDouble(Scanner sc) {
        while (true) {
            try {
                double numero = Double.parseDouble(sc.nextLine().trim());
                if (numero < 0) {
                    System.out.println("El valor no puede ser negativo. Inténtalo de nuevo:");
                } else {
                    return numero;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor incorrecto. Introduce un número decimal:");
            }
        }
    }
}