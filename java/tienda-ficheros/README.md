# TiendaFicheros

Aplicación de gestión de tienda en Java con persistencia mediante ficheros binarios y de texto.

---

## Descripción

Ejercicio extenso de clase diseñado para repasar y consolidar múltiples conceptos del lenguaje Java. La aplicación simula una tienda con tres tipos de usuario (invitado, cliente y administrador), gestión de productos y un carrito de compra, todo con persistencia de datos entre sesiones.

El grueso del código está desarrollado a mano. Se ha usado IA como apoyo para debug, orientación técnica y generación de los comentarios Javadoc.

---

## Estructura del proyecto

```
TiendaFicheros/
├── src/
│   ├── entities/
│   │   ├── Usuario.java          # Clase abstracta base
│   │   ├── Administrador.java
│   │   ├── Cliente.java          # Incluye carrito de compra
│   │   └── Producto.java
│   ├── menus/
│   │   ├── MenuInvitado.java
│   │   ├── MenuAdministrador.java
│   │   └── MenuCliente.java
│   ├── main/
│   │   └── Programa.java         # Lógica central y punto de entrada
│   └── util/
│       └── Utilidades.java       # Lectura y validación de datos
└── docs/
    └── jerarquia_paquetes.png
```

### Diagrama de paquetes y clases

![Diagrama de paquetes](docs/jerarquia_paquetes.png)

---

## Conceptos trabajados

- **Persistencia con ficheros** — serialización binaria con `ObjectInputStream` / `ObjectOutputStream` y lectura de texto con `BufferedReader`. Escritura segura mediante ficheros temporales `.tmp` para evitar pérdida de datos ante fallos.
- **Colecciones y genéricos** — uso de `List<Usuario>`, `List<Producto>` y `Map<Producto, Integer>` para el carrito de compra.
- **POO** — herencia (`Administrador` y `Cliente` extienden la clase abstracta `Usuario`), sobreescritura de `equals`, `hashCode` y `toString`.
- **Manejo de excepciones** — control de errores en todas las operaciones de I/O y validación de entrada por teclado.
- **Separación de responsabilidades** — lógica de negocio centralizada en `Programa`; los menús actúan únicamente como capa de presentación.

---

## Funcionalidades

**Invitado**
- Consultar el catálogo de productos

**Cliente**
- Registro e inicio de sesión
- Añadir productos al carrito y finalizar la compra
- El carrito persiste entre sesiones

**Administrador**
- Gestión de productos (añadir, consultar, modificar stock)
- Dar de alta nuevos usuarios
- Consultar la ganancia total acumulada

---

## Requisitos

- Java 15 o superior (se usan *text blocks*)
- No requiere dependencias externas

---

## Próxima versión

**TiendaBBDD** — misma aplicación sustituyendo la capa de ficheros por una base de datos relacional con JDBC y operaciones CRUD.
