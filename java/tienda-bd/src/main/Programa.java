package main;

import java.util.List;
import java.util.Scanner;
import dao.GananciaDAOImpl;
import dao.LineaCompraDAOImpl;
import dao.ProductoDAOImpl;
import dao.UsuarioDAOImpl;
import entities.Administrador;
import entities.Cliente;
import entities.LineaCompra;
import entities.Producto;
import entities.Usuario;
import menus.MenuAdministrador;
import menus.MenuCliente;
import menus.MenuInvitado;
import util.Utilidades;


/**
 * Clase principal de la aplicación TiendaBBDD.
 * <p>
 * Centraliza el estado de la sesión activa y toda la lógica de negocio:
 * autenticación, registro, gestión de productos y carrito. El acceso a datos
 * se delega en las clases DAO correspondientes.
 * Los menús ({@link MenuAdministrador}, {@link MenuCliente}, {@link MenuInvitado})
 * solo se encargan de mostrar opciones y delegar en esta clase.
 * </p>
 *
 * @version 1.0
 */
public class Programa {

    // -------------------------------------------------------------------------
    // Estado de sesión y DAOs
    // -------------------------------------------------------------------------

    /** Scanner único compartido por toda la aplicación. */
    public static final Scanner sc = new Scanner(System.in);

    /** Usuario con la sesión activa, o {@code null} si no hay sesión. */
    private static Usuario usuario = null;

    private static final UsuarioDAOImpl     usuarioDAO     = new UsuarioDAOImpl();
    private static final ProductoDAOImpl    productoDAO    = new ProductoDAOImpl();
    private static final LineaCompraDAOImpl lineaCompraDAO = new LineaCompraDAOImpl();
    private static final GananciaDAOImpl    gananciaDAO    = new GananciaDAOImpl();

    // =========================================================================
    // PUNTO DE ENTRADA
    // =========================================================================

    /**
     * Punto de entrada de la aplicación.
     * Muestra directamente el menú de invitado; la BBDD ya está inicializada.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        MenuInvitado.mostrarMenu();
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
     * Intenta autenticar a un usuario buscándolo por nombre y contraseña en la BBDD.
     * Si la autenticación tiene éxito, redirige al menú correspondiente.
     * En caso de ser cliente, carga su carrito desde la BBDD.
     *
     * @param tipo {@code "administrador"} o {@code "cliente"}.
     */
    private static void autenticar(String tipo) {
        System.out.println("Nombre de usuario:");
        String nombre = sc.nextLine();

        Usuario u = usuarioDAO.buscarPorUsername(nombre);

        if (u == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        System.out.println("Contraseña:");
        String contrasena = sc.nextLine();

        if (!u.getContrasena().equals(contrasena)) {
            System.out.println("Contraseña incorrecta.");
            return;
        }

        if (tipo.equals("administrador") && !(u instanceof Administrador)) {
            System.out.println("Este usuario no es administrador.");
            return;
        }
        if (tipo.equals("cliente") && !(u instanceof Cliente)) {
            System.out.println("Este usuario no es cliente.");
            return;
        }

        usuario = u;

        if (usuario instanceof Cliente cliente) {
            cliente.getCarrito().addAll(
                lineaCompraDAO.obtenerPorUsuario(cliente.getId())
            );
        }

        System.out.println("Sesión iniciada. Bienvenido, " + nombre + ".");

        if (tipo.equals("administrador")) {
            MenuAdministrador.mostrarMenu();
        } else {
            MenuCliente.mostrarMenu();
        }
    }

    /**
     * Registra un nuevo cliente comprobando que el nombre de usuario no exista ya.
     */
    public static void registrarCliente() {
        System.out.println("CREACIÓN DE NUEVO CLIENTE\nIntroduzca el nombre de usuario:");
        String nombreCliente = Utilidades.validarCredencial(sc.nextLine());

        if (usuarioDAO.buscarPorUsername(nombreCliente) != null) {
            System.out.println("Error: nombre de usuario en uso. Escoja otro.");
            return;
        }

        System.out.println("Contraseña para " + nombreCliente + ":");
        String contrasena = Utilidades.validarCredencial(sc.nextLine());

        usuario = usuarioDAO.insertar(new Cliente(nombreCliente, contrasena));
        System.out.println("Cliente registrado correctamente.");
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
     * Comprueba que el nombre de usuario no esté ya en uso antes de crearlo.
     */
    public static void crearUsuario() {
        System.out.println("¿Desea dar de alta a un administrador o a un cliente?");
        String tipo = Utilidades.validarTipoUsuario(sc.nextLine());

        System.out.println("Nombre de usuario:");
        String nombre = Utilidades.validarCredencial(sc.nextLine());

        if (usuarioDAO.buscarPorUsername(nombre) != null) {
            System.out.println("Error: nombre de usuario en uso. Escoja otro.");
            return;
        }

        System.out.println("Contraseña:");
        String contrasena = Utilidades.validarCredencial(sc.nextLine());

        if (tipo.equals("administrador")) {
            usuarioDAO.insertar(new Administrador(nombre, contrasena));
            System.out.println("Administrador creado correctamente.");
        } else {
            usuarioDAO.insertar(new Cliente(nombre, contrasena));
            System.out.println("Cliente creado correctamente.");
        }
    }

    // =========================================================================
    // GESTIÓN DE PRODUCTOS
    // =========================================================================

    /**
     * Muestra por pantalla todos los productos disponibles en el catálogo.
     */
    public static void mostrarProductos() {
        List<Producto> productos = productoDAO.listarTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos existentes.");
        } else {
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
    }

    /**
     * Busca y muestra un producto por su ID.
     */
    public static void mostrarProductoPorId() {
        System.out.println("ID del producto:");
        int id = Utilidades.leerEntero(sc);

        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("Producto no encontrado.");
        } else {
            System.out.println(p);
        }
    }

    /**
     * Permite al administrador modificar el stock de un producto dado su ID.
     */
    public static void modificarStock() {
        System.out.println("ID del producto:");
        int id = Utilidades.leerEntero(sc);

        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Nuevo stock:");
        try {
            int nuevoStock = Utilidades.leerEntero(sc);
            if (productoDAO.actualizarStock(id, nuevoStock)) {
                System.out.println("Stock actualizado correctamente.");
            } else {
                System.out.println("No se pudo actualizar el stock.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Valor de stock no válido: " + e.getMessage());
        }
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

        Producto nuevo = productoDAO.insertar(new Producto(descripcion, stock, precio));
        if (nuevo != null) {
            System.out.println("Producto añadido correctamente con ID: " + nuevo.getId());
        }
    }

    /**
     * Elimina un producto del catálogo dado su ID.
     * No permite eliminar un producto si algún cliente lo tiene en su carrito.
     */
    public static void eliminarProducto() {
        System.out.println("ID del producto a eliminar:");
        int id = Utilidades.leerEntero(sc);

        if (productoDAO.eliminar(id)) {
            System.out.println("Producto eliminado correctamente.");
        }
    }

    /**
     * Muestra la ganancia total acumulada consultando la BBDD.
     */
    public static void mostrarGanancia() {
        System.out.println("Ganancia total: " + gananciaDAO.obtener() + " €");
    }

    // =========================================================================
    // GESTIÓN DEL CARRITO
    // =========================================================================

    /**
     * Permite al cliente añadir un producto a su carrito indicando el ID
     * y la cantidad deseada. Verifica que haya stock suficiente.
     */
    public static void anadirACesta() {
        Cliente cliente = (Cliente) usuario;

        System.out.println("ID del producto:");
        int id = Utilidades.leerEntero(sc);

        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Cantidad:");
        int cantidad = Utilidades.leerEntero(sc);

        if (p.getStock() < cantidad) {
            System.out.println("No hay stock suficiente.");
            return;
        }

        LineaCompra lc = new LineaCompra(cliente.getId(), p.getId(), cantidad);
        lineaCompraDAO.insertar(lc);
        cliente.getCarrito().add(lc);
        System.out.println("Añadido al carrito.");
    }

    /**
     * Muestra el contenido del carrito del cliente con el total a pagar.
     * Ofrece la opción de finalizar la compra, lo que descuenta el stock,
     * vacía el carrito y acumula la ganancia en la BBDD.
     */
    public static void verCarrito() {
        Cliente cliente = (Cliente) usuario;

        if (cliente.getCarrito().isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        System.out.println("CARRITO:");
        double total = 0.0;

        for (LineaCompra lc : cliente.getCarrito()) {
            Producto p = productoDAO.buscarPorId(lc.getIdProducto());
            if (p != null) {
                System.out.println(p.getDescripcion() + " x" + lc.getCantidad() +
                                   " — " + (p.getPrecio() * lc.getCantidad()) + " €");
                total += p.getPrecio() * lc.getCantidad();
            }
        }

        System.out.println("Total: " + total + " €");
        System.out.println("¿Finalizar compra? (si/no)");
        String respuesta = sc.nextLine();

        if (respuesta.equalsIgnoreCase("si")) {
            for (LineaCompra lc : cliente.getCarrito()) {
                Producto p = productoDAO.buscarPorId(lc.getIdProducto());
                if (p != null) {
                    productoDAO.actualizarStock(p.getId(), p.getStock() - lc.getCantidad());
                }
            }
            gananciaDAO.actualizar(gananciaDAO.obtener() + total);
            lineaCompraDAO.vaciar(cliente.getId());
            cliente.getCarrito().clear();
            System.out.println("Compra finalizada. Total: " + total + " €");
        } else {
            System.out.println("Volviendo al menú...");
        }
    }

    // =========================================================================
    // GETTER DE SESIÓN
    // =========================================================================

    /**
     * Devuelve el usuario con la sesión activa.
     *
     * @return el {@link Usuario} activo, o {@code null} si no hay sesión.
     */
    public static Usuario getUsuario() {
        return usuario;
    }
}