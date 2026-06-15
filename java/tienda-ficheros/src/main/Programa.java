package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entities.Administrador;
import entities.Cliente;
import entities.Producto;
import entities.Usuario;
import menus.MenuAdministrador;
import menus.MenuCliente;
import menus.MenuInvitado;
import util.Utilidades;

/**
 * Clase principal de la aplicación de gestión de tienda.
 * <p>
 * Centraliza el estado global (usuarios, productos, ganancia) y toda la lógica
 * de negocio: inicio de sesión, registro, gestión de productos y carrito.
 * Los menús ({@link MenuAdministrador}, {@link MenuCliente}, {@link MenuInvitado})
 * solo se encargan de mostrar opciones y delegar en esta clase.
 * </p>
 * <p>
 * El guardado de datos usa escritura segura: primero se escribe en un fichero
 * temporal ({@code .tmp}) y solo si tiene éxito se reemplaza el fichero real,
 * evitando pérdida de datos ante un fallo a mitad de escritura.
 * </p>
 *
 * @version 1.0
 */
public class Programa implements Serializable {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------------------
    // Constantes — ficheros principales y temporales
    // -------------------------------------------------------------------------

    private static final String FICHERO_USUARIOS   = "usuarios.bin";
    private static final String FICHERO_PRODUCTOS  = "productos.bin";
    private static final String FICHERO_GANANCIA   = "ganancia.txt";

    private static final String TMP_USUARIOS       = "usuarios.tmp";
    private static final String TMP_PRODUCTOS      = "productos.tmp";
    private static final String TMP_GANANCIA       = "ganancia.tmp";

    // -------------------------------------------------------------------------
    // Estado global
    // -------------------------------------------------------------------------

    /** Scanner único compartido por toda la aplicación. */
    public static final Scanner sc = new Scanner(System.in);

    /** Usuario con la sesión activa, o {@code null} si no hay sesión. */
    private static Usuario usuario = null;

    private static List<Usuario>  usuarios  = new ArrayList<>();
    private static List<Producto> productos = new ArrayList<>();
    private static double gananciaTotal = 0.0;

    // =========================================================================
    // PUNTO DE ENTRADA
    // =========================================================================

    /**
     * Punto de entrada de la aplicación.
     * Intenta cargar los datos persistidos; si no existen, arranca el asistente
     * de primer uso para crear el primer administrador.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        buscarFicheroUsuarios();
    }

    // =========================================================================
    // CARGA Y GUARDADO DE DATOS
    // =========================================================================

    /**
     * Comprueba si existe el fichero de usuarios.
     * <ul>
     *   <li>Si existe, carga todos los ficheros y muestra el menú de invitado.</li>
     *   <li>Si no existe, lanza el asistente de creación del primer administrador.</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    public static void buscarFicheroUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHERO_USUARIOS))) {
            usuarios = (List<Usuario>) ois.readObject();
            cargarFicheros();
            MenuInvitado.mostrarMenu();
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no existente. Iniciando configuración...");
            crearUsuario();
        } catch (IOException e) {
            System.out.println("Error al leer el fichero de usuarios: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error de deserialización: " + e.getMessage());
        }
    }

    /**
     * Carga productos y ganancia total desde sus ficheros correspondientes.
     * Los usuarios ya se han cargado en {@link #buscarFicheroUsuarios()}.
     * Si algún fichero no existe se muestra un aviso y se continúa con valores vacíos.
     */
    @SuppressWarnings("unchecked")
    public static void cargarFicheros() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHERO_PRODUCTOS))) {
            productos = (List<Producto>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Fichero de productos no encontrado. Se iniciará vacío.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar productos: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_GANANCIA))) {
            gananciaTotal = Double.parseDouble(br.readLine());
        } catch (FileNotFoundException e) {
            System.out.println("Fichero de ganancia no encontrado. Se iniciará en 0.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar la ganancia: " + e.getMessage());
        }
    }

    /**
     * Persiste el estado actual (usuarios, productos y ganancia) usando escritura segura:
     * escribe primero en un fichero temporal y, si tiene éxito, lo reemplaza
     * atómicamente por el fichero real. Así un fallo a mitad nunca corrompe los datos.
     */
    public static void guardarArchivos() {
        guardarObjetoSeguro(usuarios,  TMP_USUARIOS,  FICHERO_USUARIOS);
        guardarObjetoSeguro(productos, TMP_PRODUCTOS, FICHERO_PRODUCTOS);
        guardarGananciaSeguro();
    }

    /**
     * Serializa un objeto en un fichero temporal y, si tiene éxito, lo mueve
     * al fichero definitivo reemplazándolo.
     *
     * @param objeto    objeto serializable a guardar.
     * @param temporal  ruta del fichero temporal.
     * @param definitivo ruta del fichero definitivo.
     */
    private static void guardarObjetoSeguro(Object objeto, String temporal, String definitivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(temporal))) {
            oos.writeObject(objeto);
            moverFichero(temporal, definitivo);
        } catch (IOException e) {
            System.out.println("Error al guardar " + definitivo + ": " + e.getMessage());
        }
    }

    /**
     * Guarda la ganancia total en un fichero temporal y, si tiene éxito, lo mueve
     * al fichero definitivo.
     */
    private static void guardarGananciaSeguro() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TMP_GANANCIA))) {
            bw.write(String.valueOf(gananciaTotal));
            bw.flush();
            moverFichero(TMP_GANANCIA, FICHERO_GANANCIA);
        } catch (IOException e) {
            System.out.println("Error al guardar la ganancia: " + e.getMessage());
        }
    }

    /**
     * Mueve (reemplaza atómicamente) un fichero temporal al destino definitivo.
     *
     * @param origen  ruta del fichero temporal.
     * @param destino ruta del fichero definitivo.
     * @throws IOException si el movimiento falla.
     */
    private static void moverFichero(String origen, String destino) throws IOException {
        Path src  = Paths.get(origen);
        Path dest = Paths.get(destino);
        Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    // =========================================================================
    // AUTENTICACIÓN Y REGISTRO
    // =========================================================================

    /**
     * Muestra el flujo de inicio de sesión.
     * Permite autenticarse como administrador o cliente, o crear una cuenta nueva.
     */
    public static void iniciarSesion() {
        System.out.println("""
                INICIO DE SESIÓN
                1. Iniciar sesión como administrador
                2. Iniciar sesión como cliente
                3. Crear usuario cliente
                """);

        int opcion;
        try {
            opcion = Utilidades.leerEnteroLimites(1, 3, sc);
        } catch (IllegalArgumentException e) {
            System.out.println("Opción incorrecta. Inténtelo de nuevo.");
            return;
        }

        switch (opcion) {
            case 1 -> autenticar("administrador");
            case 2 -> autenticar("cliente");
            case 3 -> registrarCliente();
        }
    }

    /**
     * Intenta autenticar a un usuario buscándolo por nombre y contraseña.
     * Si la autenticación tiene éxito, redirige al menú correspondiente
     * según el tipo indicado.
     *
     * @param tipo {@code "administrador"} o {@code "cliente"}.
     */
    private static void autenticar(String tipo) {
        System.out.println("Nombre de usuario:");
        String nombre = sc.nextLine();

        for (Usuario u : usuarios) {
            if (u.getUsername().equals(nombre)) {
                System.out.println("Contraseña:");
                String contrasena = sc.nextLine();

                if (u.getContrasena().equals(contrasena)) {
                    usuario = u;
                    System.out.println("Sesión iniciada. Bienvenido, " + nombre + ".");
                    if (tipo.equals("administrador")) {
                        MenuAdministrador.mostrarMenu();
                    } else {
                        MenuCliente.mostrarMenu();
                    }
                } else {
                    System.out.println("Contraseña incorrecta.");
                }
                return;
            }
        }
        System.out.println("Usuario no encontrado.");
    }

    /**
     * Registra un nuevo cliente desde el menú de invitado.
     * Valida que el nombre de usuario no esté ya en uso antes de crearlo.
     */
    public static void registrarCliente() {
        System.out.println("CREACIÓN DE NUEVO CLIENTE\nIntroduzca el nombre de usuario:");
        String nombreCliente = Utilidades.validarCredencial(sc.nextLine());

        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(nombreCliente)) {
                System.out.println("Error: nombre de usuario en uso. Escoja otro.");
                return;
            }
        }

        System.out.println("Contraseña para " + nombreCliente + ":");
        String contrasena = Utilidades.validarCredencial(sc.nextLine());

        Cliente clienteNuevo = new Cliente(nombreCliente, contrasena);
        usuarios.add(clienteNuevo);
        usuario = clienteNuevo;
        System.out.println("Cliente registrado correctamente.");
        MenuCliente.mostrarMenu();
    }

    /**
     * Cierra la sesión del usuario actual y vuelve al menú de invitado.
     */
    public static void desconectar() {
        System.out.println("Desconectando...");
        usuario = null;
        System.out.println("Sesión cerrada. Bienvenido.");
        MenuInvitado.mostrarMenu();
    }

    /**
     * Crea un nuevo usuario (administrador o cliente) desde el panel de administración.
     * Si es el primer uso de la aplicación también puede llamarse desde el arranque.
     */
    public static void crearUsuario() {
        System.out.println("¿Desea dar de alta a un administrador o a un cliente?");
        String tipo = Utilidades.validarTipoUsuario(sc.nextLine());

        System.out.println("Nombre de usuario:");
        String nombre = Utilidades.validarCredencial(sc.nextLine());

        System.out.println("Contraseña:");
        String contrasena = Utilidades.validarCredencial(sc.nextLine());

        if (tipo.equals("administrador")) {
            Administrador admin = new Administrador(nombre, contrasena);
            usuarios.add(admin);
            usuario = admin;
            MenuAdministrador.mostrarMenu();
        } else {
            Cliente cliente = new Cliente(nombre, contrasena);
            usuarios.add(cliente);
            usuario = cliente;
            MenuCliente.mostrarMenu();
        }
    }

    // =========================================================================
    // GESTIÓN DE PRODUCTOS
    // =========================================================================

    /**
     * Muestra por pantalla todos los productos disponibles.
     * Si no hay productos, informa de ello.
     */
    public static void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos existentes.");
        } else {
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
    }

    /**
     * Busca y muestra un producto por su código.
     * Si no se encuentra ningún producto con ese código, informa de ello.
     */
    public static void mostrarProductoPorCodigo() {
        System.out.println("Código del producto:");
        int codigo = Utilidades.leerEntero(sc);

        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                System.out.println(p);
                return;
            }
        }
        System.out.println("Producto no encontrado.");
    }

    /**
     * Permite al administrador modificar el stock de un producto dado su código.
     * El nuevo valor es validado por el setter de {@link Producto}, que rechaza
     * valores negativos.
     */
    public static void modificarStock() {
        System.out.println("Código del producto:");
        int codigo = Utilidades.leerEntero(sc);

        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                System.out.println("Nuevo stock:");
                try {
                    int nuevoStock = Utilidades.leerEntero(sc);
                    p.setStock(nuevoStock);
                    System.out.println("Stock actualizado correctamente.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Valor de stock no válido: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Producto no encontrado.");
    }

    /**
     * Añade un nuevo producto al catálogo solicitando descripción, stock y precio.
     */
    public static void anyadirProducto() {
        System.out.println("Descripción:");
        String descripcion = sc.nextLine();

        System.out.println("Stock:");
        int stock = Utilidades.leerEntero(sc);

        System.out.println("Precio:");
        double precio = Utilidades.leerDouble(sc);

        productos.add(new Producto(descripcion, stock, precio));
        System.out.println("Producto añadido correctamente.");
    }

    /**
     * Muestra la ganancia total acumulada desde el inicio de la aplicación.
     */
    public static void mostrarGanancia() {
        System.out.println("Ganancia total: " + gananciaTotal + " €");
    }

    // =========================================================================
    // GESTIÓN DEL CARRITO
    // =========================================================================

    /**
     * Permite al cliente añadir un producto a su carrito indicando el código
     * y la cantidad deseada. Verifica que haya stock suficiente.
     */
    public static void anadirACesta() {
        Cliente cliente = (Cliente) usuario;

        System.out.println("Código del producto:");
        int codigo = Utilidades.leerEntero(sc);

        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                System.out.println("Cantidad:");
                int cantidad = Utilidades.leerEntero(sc);

                if (p.getStock() >= cantidad) {
                    cliente.getCarrito().put(p, cantidad);
                    System.out.println("Añadido al carrito.");
                } else {
                    System.out.println("No hay stock suficiente.");
                }
                return;
            }
        }
        System.out.println("Producto no encontrado.");
    }

    /**
     * Muestra el contenido del carrito del cliente con el total a pagar.
     * Ofrece la opción de finalizar la compra, lo que descuenta el stock
     * y acumula la ganancia total.
     */
    public static void verCarrito() {
        Cliente cliente = (Cliente) usuario;

        if (cliente.getCarrito().isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        System.out.println("CARRITO:");
        double total = 0.0;

        for (Map.Entry<Producto, Integer> entry : cliente.getCarrito().entrySet()) {
            Producto p   = entry.getKey();
            int cantidad = entry.getValue();
            System.out.println(p + " x" + cantidad);
            total += p.getPrecio() * cantidad;
        }

        System.out.println("Total actual: " + total + " €");
        System.out.println("¿Finalizar compra? (si/no)");
        String respuesta = sc.nextLine();

        if (respuesta.equalsIgnoreCase("si")) {
            for (Map.Entry<Producto, Integer> entry : cliente.getCarrito().entrySet()) {
                entry.getKey().setStock(entry.getKey().getStock() - entry.getValue());
            }
            gananciaTotal += total;
            cliente.getCarrito().clear();
            System.out.println("Compra finalizada. Total: " + total + " €");
        } else {
            System.out.println("Volviendo al menú...");
        }
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    /**
     * Devuelve el scanner único de la aplicación.
     *
     * @return instancia compartida de {@link Scanner}.
     */
    public static Scanner getSc() {
        return sc;
    }

    /**
     * Devuelve el usuario con la sesión activa.
     *
     * @return el {@link Usuario} activo, o {@code null} si no hay sesión.
     */
    public static Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario con la sesión activa.
     *
     * @param usuario el {@link Usuario} que inicia sesión, o {@code null} para cerrarla.
     */
    public static void setUsuario(Usuario usuario) {
        Programa.usuario = usuario;
    }

    /**
     * Devuelve la lista de todos los usuarios registrados.
     *
     * @return lista de {@link Usuario}.
     */
    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Devuelve el catálogo de productos disponibles.
     *
     * @return lista de {@link Producto}.
     */
    public static List<Producto> getProductos() {
        return productos;
    }

    /**
     * Devuelve la ganancia total acumulada.
     *
     * @return ganancia total en euros.
     */
    public static double getGananciaTotal() {
        return gananciaTotal;
    }

    /**
     * Establece la ganancia total acumulada.
     *
     * @param gananciaTotal nuevo valor de la ganancia en euros.
     */
    public static void setGananciaTotal(double gananciaTotal) {
        Programa.gananciaTotal = gananciaTotal;
    }
}