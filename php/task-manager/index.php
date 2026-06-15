<?php
include("conexion.php");
session_start();

if(!isset($_SESSION["usuario"])){
    header("Location: login.php");
    exit;
}


$sql = "SELECT ID, TITULO, DESCRIPCION, PRIORIDAD, FECHA_LIMITE, COMPLETA FROM TAREAS WHERE ID_USUARIO = ?";
$resultado = $conexion->prepare($sql);
$resultado->execute($_SESSION["id"]);

$fila = $resultado->fetch(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Index</title>
</head>
<body>
    <h2>Bienvenido <?echo $_SESSION["usuario"]?> </h2>
    <h3>Lista de tareas: </h3>

    <form method="POST">
    <ul>
        <?php foreach($resultado as $fila) :?>
            
                <li>
                    <?echo $fila["titulo"]?> . - .
                    <?echo $fila["descripcion"]?> . - .
                    <?echo $fila["prioridad"]?> . - .
                    <?echo $fila["fecha_limite"]?> . - .
                    <?php
                    echo "Completada: " ;
                    if($fila["completada"]): ?> <input type="checkbox" name="completada" checked>
                    <?php
                    else:?> <input type="checkbox" name="completada">
                    <?php endif; ?>    
                </li>

                <li>
                    <button type="submit" name="editar" value=<?echo $fila["id"]?>>Editar</button>
                    <button type="submit" name="eliminar" value=<?echo $fila["id"]?>>Eliminar</button>
                </li>
                
                <hr>
        <?php 
        if(isset($_POST["ediar"])){
            header("Location: editar.php?id=" . $fila["id"]);
            exit;
        }
        
        if(isset($_POST["eliminar"])){
            header("Location: eliminar.php?id=" . $fila["id"]);
            exit;
        }
        
        endforeach; ?>
        </ul>
        <br><br>
        <button type="submit" name="insertar">Insertar nuevo libro</button><br>
        <button type="submit" name="salir">Cerrar sesión</button>
        <?php
            if(isset($_POST["insertar"])){
                header("Location: crear.php");
                exit;
            }
            
            if(isset($_POST["salir"])){
                header("Location: logout.php");
                exit;
            }
        ?>
    </form>
</body>
</html>