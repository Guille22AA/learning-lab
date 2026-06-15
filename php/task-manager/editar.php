<?php
include("conexion.php");
session_start();

if(!isset($_SESSION["usuario"])){
    header("Location: login.php");
    exit;
}

$tareaId = $_GET["id"];

$sql = "SELECT * FROM CATEGORIA WHERE ID = ?";
$resultado = $conexion->prepare($sql);
$resultado->execute([$tareaId]);

$fila = $resultado->fetch(PDO::FETCH_ASSOC);


if(isset($_POST["actualziar"])){

    $titulo = $_POST["titulo"] ?? "";
    $descripcion = $_POST["descripcion"] ?? "";
    $prioridad = $_POST["prioridad"];
    $fechaLimite = $_POST["fecha_limite"] ?? null;
    $completada = $_POST["completada"];

    $sql = "UPDATE TABLAS SET TITULO = ?, DESCRIPCION = ?, PRIORIDAD = ?, FECHA_LIMITE = ?, COMPLETADA = ? WHERE ID = ?";
    $resultado = $conexion->prepare($sql);
    $resultado->execute([$titulo, $descripcion, $prioridad, $fechaLimite, $completada, $tareaId]);

    $fila = $resultado->fetch(PDO::FETCH_ASSOC);

    if($fila == null) {
        echo "Error al actualizar.";
        exit;
    } else {
        header("Location: index.php");
        exit;
    }
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar</title>
</head>
<body>
    <h3>Editar tarea: </h3>

    <form method="POST">
    Título: <input type="text" name="titulo" value=<?echo $fila["TITULO"]?> requried><br>
    Descripción: <textarea name="descripcion" value=<?echo $fila["DESCRIPCION"]?>></textarea><br>
    Prioridad: 
        <select name="prioridad" default=<?echo $fila["PRIORIDAD"]?>>
            <option value="baja">Baja</option>
            <option value="media">Media</option>
            <option value="alta">Alta</option>
        </select><br>
        Fecha límite: <input type="date" name="fecha_limite" value=<?echo $fila["FECHA_LIMITE"]?>><br>
        Completada: <input type="checkbox" name="completada" default=<?echo $fila["COMPLETADA"]?>><br>

        <button type="submit" name="actualizar">Actualizar</button>
    </form>
</body>
</html>